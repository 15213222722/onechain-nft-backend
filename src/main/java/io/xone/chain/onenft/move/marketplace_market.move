module marketplace::market;

use one::coin::{Self, Coin};
use one::event;
use std::type_name::{Self, TypeName};

#[allow(unused_const)]
const E_NOT_OWNER: u64 = 1001; // Caller does not own the listing
const E_LISTING_NOT_ACTIVE: u64 = 1002; // Listing is no longer active
const E_INSUFFICIENT_PAYMENT: u64 = 1004; // Payment amount is insufficient
const E_INVALID_FEE_BPS: u64 = 1005; // Fee BPS exceeds limit
const E_WRONG_FEE_CONFIG: u64 = 1006; // Fee config does not match market
const E_NFT_NOT_FOUND: u64 = 1007; // NFT missing in listing
const E_MARKET_PAUSED: u64 = 1008; // Market is paused
const E_INVALID_PRICE: u64 = 1009; // Price must be greater than 0
const E_WRONG_LISTING_TYPE: u64 = 1010; // Operation not supported for this listing type
const E_WRONG_COIN_TYPE: u64 = 1011; // Coin type does not match listing
const E_WRONG_NFT_TYPE: u64 = 1012; // NFT type does not match listing

/// Constants representing listing types.
const LISTING_SWAP: u8 = 0; // Listing for swapping NFTs
const LISTING_SALE: u8 = 1; // Listing for selling NFTs

/// Constants representing listing statuses.
const ACTIVE: u8 = 0; // The listing is active and available
const FILLED: u8 = 1; // The listing has been fulfilled
const CANCELLED: u8 = 2; // The listing has been cancelled

/// Configuration for marketplace fees.
public struct FeeConfig has key {
  id: UID, // Unique identifier for the config
  fee_bps: u64, // Fee in basis points (BPS), e.g., 100 = 1%
  fee_recipient: address, // Address to receive fees
}

/// Administrative capability for managing the marketplace.
public struct AdminCap has key {
  id: UID, // Unique identifier for admin capability
}

/// Global market state for enabling/disallowing activity.
public struct MarketState has key {
  id: UID, // Unique identifier for the global state
  fee_config_id: ID, // ID of the authorized fee configuration
  paused: bool, // Whether the market is paused
}

/// Represents a marketplace listing.
public struct Listing<T: key + store> has key, store {
  id: UID,
  owner: address,
  listing_type: u8,
  status: u8,
  nft: Option<T>,
  nft_id: ID,
  nft_type: TypeName,
  expected_nft_type: option::Option<TypeName>,
  price: option::Option<u64>,
  coin_type: option::Option<TypeName>,
}

/// Event emitted when a new listing is created.
public struct ListingCreated has copy, drop {
  listing_id: ID,
  owner: address,
  nft_id: ID,
  nft_type: TypeName,
  price: option::Option<u64>,
  listing_type: u8,
  expected_nft_type: option::Option<TypeName>,
  coin_type: option::Option<TypeName>,
}

/// Event emitted when a listing is filled (either purchase or swap).
/// 出售（固定价买 NFT）成交时发出的事件
public struct ListingSaleFilled has copy, drop {
  listing_id: ID,
  taker: address, // 买家地址
  lister: address, // 卖家（挂单人）地址
  nft_id: ID, // 被购买的 NFT ID
  payment_amount: u64, // 买家实际支付的 Coin 数量（原始 value）
  fee_amount: u64, // 实际扣除的手续费
  seller_amount: u64, // 卖家实际收到金额
  coin_type: TypeName, // 支付的币种
  timestamp_ms: u64, // 可选：交易发生的时间戳（方便排序/审计）
}

/// NFT ↔ NFT 交换成交时发出的事件
public struct ListingSwapFilled has copy, drop {
  listing_id: ID,
  taker: address, // 提供换入 NFT 的人
  lister: address, // 原挂单人（收到换入 NFT 的人）
  nft_id_out: ID, // listing 中原有的 NFT（给 taker）
  nft_id_in: ID, // taker 提供的换入 NFT（给 lister）
  timestamp_ms: u64, // 可选：交易发生的时间戳
}

/// Event emitted when a listing is cancelled.
public struct ListingCancelled has copy, drop {
  listing_id: ID,
  owner: address, // 冗余但方便查询
  nft_id: ID,
  listing_type: u8, // 方便前端区分
  cancel_time: u64, // 新增：区块时间戳 tx_context::epoch_timestamp_ms(ctx)
}

/// Event emitted when fee configuration is updated.
public struct FeeUpdated has copy, drop {
  fee_config_id: ID,
  new_fee_bps: u64,
  new_recipient: address,
}

/// =============================
/// HELPER FUNCTIONS
/// =============================

/// Ensures the marketplace is not paused.
fun assert_not_paused(state: &MarketState) {
  assert!(!state.paused, E_MARKET_PAUSED);
}

/// Ensures the listing is active.
fun assert_active_listing<T: key + store>(listing: &Listing<T>) {
  assert!(listing.status == ACTIVE, E_LISTING_NOT_ACTIVE);
}

/// =============================
/// SALE LISTING (NFT <-> Coin)
/// =============================

/// Creates a new sale listing.
public fun create_sale_listing<T: key + store, C>(
  state: &MarketState,
  nft: T,
  price: u64,
  ctx: &mut TxContext,
) {
  assert_not_paused(state);
  assert!(price > 0, E_INVALID_PRICE);

  let sender = tx_context::sender(ctx);
  let nft_id = object::id(&nft);

  let listing = Listing<T> {
    id: object::new(ctx),
    owner: sender,
    listing_type: LISTING_SALE,
    status: ACTIVE,
    nft: option::some(nft),
    nft_id,
    nft_type: type_name::with_original_ids<T>(),
    expected_nft_type: option::none(),
    price: option::some(price),
    coin_type: option::some(type_name::with_original_ids<C>()),
  };

  let listing_id = object::id(&listing);

  transfer::share_object(listing);

  event::emit(ListingCreated {
    listing_id,
    owner: sender,
    nft_id,
    nft_type: type_name::with_original_ids<T>(),
    price: option::some(price),
    listing_type: LISTING_SALE,
    expected_nft_type: option::none(),
    coin_type: option::some(type_name::with_original_ids<C>())
  });
}

/// Buys an NFT from a sale listing.
public fun buy_nft<T: key + store, C>(
  state: &MarketState,
  listing: &mut Listing<T>,
  mut payment: Coin<C>,
  fee_config: &FeeConfig,
  ctx: &mut TxContext,
) {
  // 基本检查
  assert!(object::id(fee_config) == state.fee_config_id, E_WRONG_FEE_CONFIG);
  assert!(listing.listing_type == LISTING_SALE, E_WRONG_LISTING_TYPE);

  let price = *option::borrow(&listing.price);
  let original_payment_value = coin::value(&payment);
  assert!(original_payment_value == price, E_INSUFFICIENT_PAYMENT);

  assert_not_paused(state);
  assert_active_listing(listing);

  // 立即标记为已完成，防止重入
  listing.status = FILLED;

  // 验证币种匹配
  assert!(
    type_name::with_original_ids<C>() == *option::borrow(&listing.coin_type),
    E_WRONG_COIN_TYPE,
  );

  // === 关键修改：先提取 NFT ===
  let nft = extract_nft(listing);

  // 计算费用
  let fee = (((price as u128) * (fee_config.fee_bps as u128) / 10_000) as u64);
  let seller_amount = price - fee;

  // 拆分 payment
  let fee_coin = coin::split(&mut payment, fee, ctx);
  let seller_coin = coin::split(&mut payment, seller_amount, ctx);

  // 执行转账（coin 部分）
  transfer::public_transfer(fee_coin, fee_config.fee_recipient);
  transfer::public_transfer(seller_coin, listing.owner);

  // 清理剩余 payment（理论上应该为 0）
  coin::destroy_zero(payment);

  // 最后再转 NFT 给买家
  transfer::public_transfer(nft, tx_context::sender(ctx));

  // 发出事件
  event::emit(ListingSaleFilled {
    listing_id: object::id(listing),
    taker: tx_context::sender(ctx),
    lister: listing.owner,
    nft_id: listing.nft_id,
    payment_amount: original_payment_value,
    fee_amount: fee,
    seller_amount,
    coin_type: type_name::with_original_ids<C>(),
    timestamp_ms: tx_context::epoch_timestamp_ms(ctx),
  });
}

/// =============================
/// SWAP LISTING (NFT <-> NFT)
/// =============================

/// Creates a new swap listing.
public fun create_swap_listing<T: key + store, U: key + store>(
  state: &MarketState,
  nft: T,
  ctx: &mut TxContext,
) {
  assert_not_paused(state);

  let sender = tx_context::sender(ctx);
  let nft_id = object::id(&nft);

  let listing = Listing<T> {
    id: object::new(ctx),
    owner: sender,
    listing_type: LISTING_SWAP,
    status: ACTIVE,
    nft: option::some(nft),
    nft_id,
    nft_type: type_name::with_original_ids<T>(),
    expected_nft_type: option::some(type_name::with_original_ids<U>()),
    price: option::none(),
    coin_type: option::none(),
  };

  let listing_id = object::id(&listing);

  transfer::share_object(listing);

  event::emit(ListingCreated {
    listing_id,
    owner: sender,
    nft_id,
    nft_type: type_name::with_original_ids<T>(),
    price: option::none(),
    listing_type: LISTING_SWAP,
    expected_nft_type: option::some(type_name::with_original_ids<U>()),
    coin_type: option::none(),
  });
}

/// Swaps NFTs in a swap listing.
public fun swap_nft<T: key + store, U: key + store>(
  state: &MarketState,
  listing: &mut Listing<T>,
  incoming_nft: U,
  ctx: &mut TxContext,
) {
  assert_not_paused(state);
  assert_active_listing(listing);
  listing.status = FILLED;

  assert!(listing.listing_type == LISTING_SWAP, E_WRONG_LISTING_TYPE);
  assert!(
    type_name::with_original_ids<U>() == *option::borrow(&listing.expected_nft_type),
    E_WRONG_NFT_TYPE,
  );

  let lister = listing.owner;
  let taker = tx_context::sender(ctx);

  let original_nft = extract_nft(listing);
  let incoming_nft_id = object::id(&incoming_nft);
  // Transfer buyer's NFT to seller (lister)
  transfer::public_transfer(incoming_nft, lister);

  transfer::public_transfer(original_nft, taker);

  // 发出专门的交换成交事件
  event::emit(ListingSwapFilled {
    listing_id: object::id(listing),
    taker,
    lister,
    nft_id_out: listing.nft_id, // 原挂单的 NFT → 给 taker
    nft_id_in: incoming_nft_id, // 换入的 NFT → 给 lister
    timestamp_ms: tx_context::epoch_timestamp_ms(ctx),
  });
}

/// Cancels an active listing.
public fun cancel_listing<T: key + store>(
  state: &MarketState,
  listing: &mut Listing<T>,
  ctx: &mut TxContext,
) {
  assert!(listing.owner == tx_context::sender(ctx), E_NOT_OWNER);

  assert_not_paused(state);

  assert_active_listing(listing);

  listing.status = CANCELLED;

  let nft = extract_nft(listing);

  transfer::public_transfer(nft, listing.owner);

  event::emit(ListingCancelled {
    listing_id: object::id(listing),
    owner: listing.owner,
    nft_id: listing.nft_id,
    listing_type: listing.listing_type,
    cancel_time: tx_context::epoch_timestamp_ms(ctx),
  });
}

/// 从 listing 中安全提取 NFT
fun extract_nft<T: key + store>(listing: &mut Listing<T>): T {
  assert!(option::is_some(&listing.nft), E_NFT_NOT_FOUND);
  option::extract(&mut listing.nft)
}

/// =============================
/// ADMIN FUNCTIONS
/// =============================

/// Initializes the marketplace with admin capabilities.
public fun create_market(
  fee_bps: u64,
  fee_recipient: address,
  ctx: &mut TxContext,
) {
  assert!(fee_bps <= 10_000, E_INVALID_FEE_BPS); // Fee cannot exceed 100%

  let admin = AdminCap { id: object::new(ctx) };
  let fee = FeeConfig { id: object::new(ctx), fee_bps, fee_recipient };
  let state = MarketState {
    id: object::new(ctx),
    fee_config_id: object::id(&fee),
    paused: false,
  };

  transfer::transfer(admin, tx_context::sender(ctx));
  transfer::share_object(fee);
  transfer::share_object(state);
}

/// Initializes the marketplace on deployment.
fun init(ctx: &mut TxContext) {
  let fee_bps = 100; // Default fee 1%
  let fee_recipient = tx_context::sender(ctx);

  let admin = AdminCap { id: object::new(ctx) };
  let fee = FeeConfig { id: object::new(ctx), fee_bps, fee_recipient };
  let state = MarketState {
    id: object::new(ctx),
    fee_config_id: object::id(&fee),
    paused: false,
  };

  transfer::transfer(admin, tx_context::sender(ctx));
  transfer::share_object(fee);
  transfer::share_object(state);
}

#[test_only]
public fun init_for_testing(ctx: &mut TxContext) {
  init(ctx);
}

/// Pauses the marketplace.
public fun pause_market(_admin: &AdminCap, state: &mut MarketState) {
  state.paused = true;
}

/// Unpauses the marketplace.
public fun unpause_market(_admin: &AdminCap, state: &mut MarketState) {
  state.paused = false;
}

/// Updates the fee configuration (modifies existing FeeConfig object directly)
public fun update_fee(
  _admin: &AdminCap,
  state: &mut MarketState,
  fee_config: &mut FeeConfig, // Pass the current FeeConfig
  new_fee_bps: u64,
  new_recipient: address,
) {
  assert!(new_fee_bps <= 10_000, E_INVALID_FEE_BPS);

  // Check that the passed fee_config is indeed the active one
  assert!(object::id(fee_config) == state.fee_config_id, E_WRONG_FEE_CONFIG);

  // Update fields directly
  fee_config.fee_bps = new_fee_bps;
  fee_config.fee_recipient = new_recipient;

  event::emit(FeeUpdated {
    fee_config_id: object::id(fee_config),
    new_fee_bps,
    new_recipient,
  });
}
