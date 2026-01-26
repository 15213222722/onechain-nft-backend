package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.entity.NftListingEvent;
import io.xone.chain.onenft.mapper.NftListingEventMapper;
import io.xone.chain.onenft.service.INftListingEventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.xone.chain.onenft.entity.Nfts;
import io.xone.chain.onenft.service.INftsService;
import io.xone.chain.onenft.service.IProcessedEventService;
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
public class NftListingEventServiceImpl extends ServiceImpl<NftListingEventMapper, NftListingEvent> implements INftListingEventService {

    private final INftsService nftsService;
    private final IProcessedEventService processedEventService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleNFTListingEvent(String txHash, String walletAddress, String eventType, String kioskId, String nftObjectId, BigDecimal price, Long timestampMs) {
        // Check processed
        if (processedEventService.isProcessed(txHash, eventType)) {
            log.info("NFT Listing Event already processed: {}", txHash);
            return;
        }

        // Update NFT using common service
        nftsService.syncNftFromChain(nftObjectId, nft -> {
            nft.setKioskId(kioskId);
            nft.setIsListed(true);
            // Updating listing price
            nft.setListingPrice(price);
            nft.setOwnerAddress(walletAddress);
        });
        
        // Check duplicate
        QueryWrapper<NftListingEvent> query = new QueryWrapper<>();
        query.eq("txHash", txHash);
        if (this.count(query) > 0) {
            log.info("NFT Listing Event already exists: {}", txHash);
            return;
        }

        NftListingEvent entity = new NftListingEvent();
        entity.setTxHash(txHash);
        entity.setWalletAddress(walletAddress);
        entity.setEventType(eventType);
        entity.setKioskId(kioskId);
        entity.setNftObjectId(nftObjectId);
        entity.setListingPrice(price);
        if (timestampMs != null) {
            entity.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMs), ZoneId.systemDefault()));
        }
        this.save(entity);
        log.info("Saved NFTListing event: {}", txHash);
        
        processedEventService.markProcessed(txHash, eventType);
    }
}