module anime::nft;

use anime::market_kiosk::{Self, GameRegistry};
use one::kiosk::{Self, Kiosk, KioskOwnerCap};
use one::package::claim;

public struct NFT has drop {}

public struct TestNFT has key, store {
  id: UID,
}

// 原本的簡單 mint（保持不變，適合測試或不走 Kiosk 的場景）
public fun mint(ctx: &mut TxContext) {
  let nft = TestNFT {
    id: object::new(ctx),
  };
  transfer::public_transfer(nft, tx_context::sender(ctx));
}

// 推薦：鑄造後自動放入呼叫者的 Kiosk
public fun mint_and_place_to_kiosk(
  kiosk: &mut Kiosk,
  cap: &KioskOwnerCap,
  registry: &GameRegistry,
  ctx: &mut TxContext,
) {
  let nft = TestNFT {
    id: object::new(ctx),
  };

  // 檢查是否在白名單內（若不在會 abort）
  market_kiosk::register_nft<TestNFT>(registry);

  // 自動放入 Kiosk
  kiosk::place(kiosk, cap, nft);
}

// 可選：一鍵鑄造 + 直接上架（價格由呼叫者決定）
public fun mint_and_list(
  price: u64, // 以 mist 為單位，例如 1 OCT = 1_000_000_000
  kiosk: &mut Kiosk,
  cap: &KioskOwnerCap,
  registry: &GameRegistry,
  ctx: &mut TxContext,
) {
  let nft = TestNFT {
    id: object::new(ctx),
  };

  market_kiosk::register_nft<TestNFT>(registry);

  let nft_id = object::id(&nft);

  kiosk::place(kiosk, cap, nft);
  kiosk::list<TestNFT>(kiosk, cap, nft_id, price);
}

fun init(otw: NFT, ctx: &mut TxContext) {
  let publisher = claim(otw, ctx);
  transfer::public_transfer(publisher, tx_context::sender(ctx));
}
