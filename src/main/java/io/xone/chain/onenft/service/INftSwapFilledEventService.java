package io.xone.chain.onenft.service;

import io.xone.chain.onenft.entity.NftSwapFilledEvent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 交换事件表 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-11
 */
public interface INftSwapFilledEventService extends IService<NftSwapFilledEvent> {

	void handleNFTSwapFilledEvent(String txDigest, String listingObjectId, String taker, String lister, String nftIdOut, String nftIdIn,
			Long timestampMs);

}