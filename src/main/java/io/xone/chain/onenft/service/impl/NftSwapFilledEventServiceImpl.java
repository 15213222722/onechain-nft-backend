package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.entity.NftSwapFilledEvent;
import io.xone.chain.onenft.mapper.NftSwapFilledEventMapper;
import io.xone.chain.onenft.service.INftSwapFilledEventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * <p>
 * 交换事件表 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-11
 */
@Service
public class NftSwapFilledEventServiceImpl extends ServiceImpl<NftSwapFilledEventMapper, NftSwapFilledEvent> implements INftSwapFilledEventService {

	@Override
	public void handleNFTSwapFilledEvent(String txDigest, String listingObjectId, String taker, String lister, String nftIdOut, String nftIdIn,
			Long timestampMs) {
		NftSwapFilledEvent event = new NftSwapFilledEvent();
		event.setTxHash(txDigest);
		event.setListingObjectId(listingObjectId);
		event.setTaker(taker);
		event.setLister(lister);
		event.setNftObjectIdOut(nftIdOut);
		event.setNftObjectIdIn(nftIdIn);
		event.setEventType("ListingSwapFilled");
		event.setCreatedAt(LocalDateTime.now());
		event.setUpdatedAt(LocalDateTime.now());
		if (timestampMs != null) {
			event.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMs), ZoneId.systemDefault()));
		}
		this.save(event);
	}
}