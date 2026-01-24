package io.xone.chain.onenft.service;

import io.xone.chain.onenft.entity.NftTakenEvent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * nft取走事件 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-24
 */
public interface INftTakenEventService extends IService<NftTakenEvent> {

    void handleNFTTakenEvent(String txHash, String walletAddress, String eventType, String kioskId, String nftObjectId, Long timestampMs);

}
