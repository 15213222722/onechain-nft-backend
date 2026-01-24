package io.xone.chain.onenft.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.onechain.OneChain;
import io.xone.chain.onenft.entity.NftPlacedEvent;
import io.xone.chain.onenft.mapper.NftPlacedEventMapper;
import io.xone.chain.onenft.service.INftPlacedEventService;
import io.xone.chain.onenft.service.INftsService;
import io.xone.chain.onenft.service.IProcessedEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * nft放入市场事件 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-24
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NftPlacedEventServiceImpl extends ServiceImpl<NftPlacedEventMapper, NftPlacedEvent>
		implements INftPlacedEventService {

	private final INftsService nftsService;

	private final OneChain oneChain;
	
	private final IProcessedEventService processedEventService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handleNFTPlacedEvent(String txHash, String walletAddress, String eventType, String kioskId,
			String nftObjectId, Long timestampMs) {
		 // Check processed
        if (processedEventService.isProcessed(txHash, eventType)) {
            log.info("Event already processed: {}", txHash);
            return;
        }
        
		// Update NFT using common service
        nftsService.syncNftFromChain(nftObjectId, nft -> {
            nft.setKioskId(kioskId);
            nft.setIsListed(false);
            nft.setOwnerAddress(walletAddress);
        });

		// Check duplicate
		QueryWrapper<NftPlacedEvent> query = new QueryWrapper<>();
		query.eq("txHash", txHash);
		if (this.count(query) > 0) {
			log.info("NFT placed Event already exists: {}", txHash);
			return;
		}

		NftPlacedEvent entity = new NftPlacedEvent();
		entity.setTxHash(txHash);
		entity.setWalletAddress(walletAddress);
		entity.setEventType(eventType);
		entity.setKioskId(kioskId);
		entity.setNftObjectId(nftObjectId);
		if (timestampMs != null) {
			entity.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMs), ZoneId.systemDefault()));
		}
		this.save(entity);
		log.info("Saved KioskCreated event: {}", txHash);
		
		processedEventService.markProcessed(txHash, eventType);
	}

}
