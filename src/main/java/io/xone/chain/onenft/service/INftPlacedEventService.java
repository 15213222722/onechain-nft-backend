package io.xone.chain.onenft.service;

import io.xone.chain.onenft.entity.NftPlacedEvent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * nft放入市场事件 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-24
 */
public interface INftPlacedEventService extends IService<NftPlacedEvent> {

	void handleNFTPlacedEvent(String txHash, String walletAddress, String eventType, String kioskId, String nftObjectId,
			Long timestampMs);

}
