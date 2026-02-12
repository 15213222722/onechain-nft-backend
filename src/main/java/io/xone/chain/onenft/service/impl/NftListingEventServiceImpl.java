package io.xone.chain.onenft.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.xone.chain.onenft.entity.NftListingEvent;
import io.xone.chain.onenft.mapper.NftListingEventMapper;
import io.xone.chain.onenft.service.INftListingEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 上架事件表 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-26
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NftListingEventServiceImpl extends ServiceImpl<NftListingEventMapper, NftListingEvent> implements INftListingEventService {@Override
	
	
	
	
	public void handleNFTListEvent(String txHash, String walletAddress, String eventType, String listingObjectId,
			String nftObjectId, Long listingPrice, Long timestampMs) {
        // Check duplicate
        QueryWrapper<NftListingEvent> query = new QueryWrapper<>();
        query.eq("tx_hash", txHash);
        if (this.count(query) > 0) {
            log.info("NFT Listing Event already exists: {}", txHash);
            return;
        }
        NftListingEvent entity = new NftListingEvent();
        entity.setTxHash(txHash);
        entity.setWalletAddress(walletAddress);
        entity.setEventType(eventType);
        entity.setListingObjectId(listingObjectId);
        entity.setNftObjectId(nftObjectId);
        entity.setListingPrice(listingPrice);
        if (timestampMs != null) {
            entity.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMs), ZoneId.systemDefault()));
        }
        this.save(entity);
        log.info("Saved NFTListing event: {}", txHash);
        
    }


}