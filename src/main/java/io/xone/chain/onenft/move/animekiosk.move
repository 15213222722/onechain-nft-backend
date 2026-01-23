module anime::market_kiosk;

use one::balance::{Self, Balance};
use one::coin::{Self, Coin};
use one::event;
use one::kiosk::{Self, Kiosk, KioskOwnerCap};
use one::oct::OCT;
use one::package::{Publisher, claim};
use one::transfer_policy::{Self, TransferPolicy, TransferRequest};
use std::ascii::String;
use std::type_name::{Self, TypeName};

/*********************************
     * 常量
     *********************************/
const PLATFORM_FEE_BPS: u64 = 250;
// const E_NOT_ADMIN: u64 = 0;
const E_NOT_ALLOWED_GAME: u64 = 1;

/*********************************
     * 管理员
     *********************************/
public struct AdminCap has key { id: UID }


/*********************************
     * 平台收益
     *********************************/
public struct PlatformProfit has key {
  id: UID,
  profit: Balance<OCT>,
}

/*********************************
     * TransferPolicy Rule
     *********************************/
public struct PlatformFeeRule has drop {}

public struct PlatformFeeConfig has drop, store {
  fee_bps: u64,
  recipient: address,
}

/*********************************
     * 事件
     *********************************/

public struct NFTPlaced has copy, drop {
  nft_id: ID,
  kiosk_id: ID,
}

public struct NFTTaken has copy, drop {
  nft_id: ID,
  kiosk_id: ID,
}

public struct NFTListed has copy, drop {
  nft_id: ID,
  price: u64,
  kiosk_id: ID,
}

public struct NFTDelisted has copy, drop {
  nft_id: ID,
  kiosk_id: ID,
}

public struct NFTSold has copy, drop {
  nft_id: ID,
  price: u64,
  fee_paid: u64,
  buyer: address,
  seller: address,
}

public struct KioskCreated has copy, drop {
  kiosk_id: ID,
}

public struct MARKET_KIOSK has drop {}

/*********************************
     * 初始化
     *********************************/
fun init(otw: MARKET_KIOSK, ctx: &mut TxContext) {
  transfer::transfer(
    AdminCap { id: object::new(ctx) },
    tx_context::sender(ctx),
  );

  let publisher = claim(otw, ctx);

  transfer::public_transfer(publisher, tx_context::sender(ctx));

  transfer::share_object(PlatformProfit {
    id: object::new(ctx),
    profit: balance::zero(),
  });
}


/*********************************
     * 创建 Kiosk
     *********************************/
public fun create_kiosk(ctx: &mut TxContext) {
  let (kiosk, cap) = kiosk::new(ctx);
  let kiosk_id = object::id(&kiosk);
  transfer::public_share_object(kiosk);
  transfer::public_transfer(cap, tx_context::sender(ctx));
  // 新增事件：KioskCreated
  event::emit(KioskCreated { kiosk_id });
}

/*********************************
     * Transfer Policy
     *********************************/
public fun create_transfer_policy<T: key + store>(
  publisher: &Publisher,
  platform: address,
  ctx: &mut TxContext,
) {
  let (mut policy, cap) = transfer_policy::new<T>(publisher, ctx);

  transfer_policy::add_rule(
    PlatformFeeRule {},
    &mut policy,
    &cap,
    PlatformFeeConfig {
      fee_bps: PLATFORM_FEE_BPS,
      recipient: platform,
    },
  );

  transfer::public_share_object(policy);
  transfer::public_transfer(cap, tx_context::sender(ctx));
}

public fun place_nft<T: key + store>(
  kiosk: &mut Kiosk,
  cap: &KioskOwnerCap,
  nft: T,
) {
  let nft_id = object::id(&nft);
  let kiosk_id = object::id(kiosk);
  
  kiosk::place(kiosk, cap, nft);
  event::emit(NFTPlaced { nft_id, kiosk_id });
}

// Take NFT from Kiosk
public fun take_nft<T: key + store>(
  kiosk: &mut Kiosk,
  cap: &KioskOwnerCap,
  nft_id: ID,
): T {
  let nft = kiosk::take<T>(kiosk, cap, nft_id);
  let kiosk_id = object::id(kiosk);
  event::emit(NFTTaken { nft_id, kiosk_id });
  nft
}

// List NFT for sale
public fun list_nft<T: key + store>(
  kiosk: &mut Kiosk,
  cap: &KioskOwnerCap,
  nft_id: ID,
  price: u64,
) {
  kiosk::list<T>(kiosk, cap, nft_id, price);
  let kiosk_id = object::id(kiosk);
  event::emit(NFTListed { nft_id, price, kiosk_id });
}

// Delist NFT
public fun delist_nft<T: key + store>(
  kiosk: &mut Kiosk,
  cap: &KioskOwnerCap,
  nft_id: ID,
) {
  kiosk::delist<T>(kiosk, cap, nft_id);
  let kiosk_id = object::id(kiosk);
  event::emit(NFTDelisted { nft_id, kiosk_id });
}


/*********************************
 * 购买 NFT（正确版）
 *********************************/
public fun buy_nft<T: key + store>(
  kiosk: &mut Kiosk,
  nft_id: ID,
  payment: Coin<OCT>,
): (T, TransferRequest<T>) {
  let (nft, request) = kiosk::purchase<T>(kiosk, nft_id,  payment);
  (nft, request)
}



/*********************************
     * Rule Hook
     *********************************/
public fun on_transfer<T: key + store>(
  _rule: &PlatformFeeRule,
  _policy: &TransferPolicy<T>,
  cfg: &PlatformFeeConfig,
  _nft: &T,
  seller: address,
  mut paid: Coin<OCT>,
  ctx: &mut TxContext,
) {
  let price = coin::value(&paid);
  let fee = price * cfg.fee_bps / 10_000;

  let fee_coin = coin::split(&mut paid, fee, ctx);

  transfer::public_transfer(fee_coin, cfg.recipient);
  transfer::public_transfer(paid, seller);

  event::emit(NFTSold {
    nft_id: object::id(_nft),
    price,
    fee_paid: fee,
    buyer: tx_context::sender(ctx),
    seller,
  });
}
