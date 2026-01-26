package io.xone.chain.onenft.service;

import io.xone.chain.onenft.entity.NftListingEvent;
import com.baomidou.mybatisplus.extension.service.IService;
import java.math.BigDecimal;

/**
 * <p>
 * nft上架事件 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-26
 */
public interface INftListingEventService extends IService<NftListingEvent> {

    void handleNFTListingEvent(String txHash, String walletAddress, String eventType, String kioskId, String nftObjectId, BigDecimal price, Long timestampMs);

}
