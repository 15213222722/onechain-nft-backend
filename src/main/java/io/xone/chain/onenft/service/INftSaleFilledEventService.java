package io.xone.chain.onenft.service;

import io.xone.chain.onenft.entity.NftSaleFilledEvent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 售卖事件表 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-11
 */
public interface INftSaleFilledEventService extends IService<NftSaleFilledEvent> {

	void handleNFTSaleFilledEvent(String txDigest, String listingObjectId, String taker, String lister, String nftId, Long paymentAmount,
			Long feeAmount, Long sellerAmount, String coinType, Long timestampMs);

}