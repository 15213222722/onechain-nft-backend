package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.entity.NftSaleFilledEvent;
import io.xone.chain.onenft.mapper.NftSaleFilledEventMapper;
import io.xone.chain.onenft.service.INftSaleFilledEventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * <p>
 * 售卖事件表 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-11
 */
@Service
public class NftSaleFilledEventServiceImpl extends ServiceImpl<NftSaleFilledEventMapper, NftSaleFilledEvent> implements INftSaleFilledEventService {

	@Override
	public void handleNFTSaleFilledEvent(String txDigest, String listingObjectId, String taker, String lister, String nftId, Long paymentAmount,
			Long feeAmount, Long sellerAmount, String coinType, Long timestampMs) {
		NftSaleFilledEvent event = new NftSaleFilledEvent();
		event.setTxHash(txDigest);
		event.setListingObjectId(listingObjectId);
		event.setTaker(taker);
		event.setLister(lister);
		event.setNftObjectId(nftId);
		event.setPaymentAmount(paymentAmount);
		event.setFeeAmount(feeAmount);
		event.setSellerAmount(sellerAmount);
		event.setCoinType(coinType);
		event.setEventType("ListingSaleFilled");
		event.setCreatedAt(LocalDateTime.now());
		event.setUpdatedAt(LocalDateTime.now());
		if (timestampMs != null) {
			event.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMs), ZoneId.systemDefault()));
		}
		this.save(event);
	}
}