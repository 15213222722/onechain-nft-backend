module anime::market_kiosk;

use one::balance::{Self, Balance};
use one::coin::{Self, Coin};
use one::event;
use one::kiosk;
use one::oct::OCT;
use one::package::{Publisher, claim};
use one::transfer_policy::{Self, TransferPolicy};
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
     * 游戏白名单
     *********************************/
public struct GameRegistry has key {
  id: UID,
  allowed_packages: vector<String>,
}

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
public struct NFTSold has copy, drop {
  nft_id: ID,
  price: u64,
  fee_paid: u64,
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

  transfer::share_object(GameRegistry {
    id: object::new(ctx),
    allowed_packages: vector::empty(),
  });

  transfer::share_object(PlatformProfit {
    id: object::new(ctx),
    profit: balance::zero(),
  });
}

/*********************************
     * 管理
     *********************************/
public fun add_game(
  _admin: &AdminCap,
  registry: &mut GameRegistry,
  pkg: String,
) {
  vector::push_back(&mut registry.allowed_packages, pkg);
}

/*********************************
     * 白名单校验
     *********************************/
fun is_allowed_game(registry: &GameRegistry, nft_type: &TypeName): bool {
  let pkg = type_name::get_address(nft_type);
  vector::contains(&registry.allowed_packages, &pkg)
}

/*********************************
     * 注册 NFT
     *********************************/
public fun register_nft<T: key>(registry: &GameRegistry) {
  let t = type_name::get<T>();
  assert!(is_allowed_game(registry, &t), E_NOT_ALLOWED_GAME);
}

/*********************************
     * 创建 Kiosk
     *********************************/
public fun create_kiosk(ctx: &mut TxContext) {
  let (kiosk, cap) = kiosk::new(ctx);
  transfer::public_share_object(kiosk);
  transfer::public_transfer(cap, tx_context::sender(ctx));
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
  });
}
