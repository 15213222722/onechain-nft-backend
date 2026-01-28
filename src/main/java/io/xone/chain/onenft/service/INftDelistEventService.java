package io.xone.chain.onenft.service;

import io.xone.chain.onenft.entity.NftDelistEvent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 下架事件表 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-26
 */
public interface INftDelistEventService extends IService<NftDelistEvent> {

    void handleNFTDelistEvent(String txHash, String walletAddress, String eventType, String kioskId, String nftObjectId, Long timestampMs);

}