package io.xone.chain.onenft.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.xone.chain.onenft.entity.NftTakenEvent;
import io.xone.chain.onenft.mapper.NftTakenEventMapper;
import io.xone.chain.onenft.service.INftTakenEventService;
import io.xone.chain.onenft.service.IProcessedEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * nft取走事件 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-24
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NftTakenEventServiceImpl extends ServiceImpl<NftTakenEventMapper, NftTakenEvent> implements INftTakenEventService {

    
    private final IProcessedEventService processedEventService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleNFTTakenEvent(String txHash, String walletAddress, String eventType, String kioskId, String nftObjectId, Long timestampMs) {
    	// Check processed
        if (processedEventService.isProcessed(txHash, eventType)) {
            log.info("Event already processed: {}", txHash);
            return;
        }

        // Check duplicate
        QueryWrapper<NftTakenEvent> query = new QueryWrapper<>();
        query.eq("txHash", txHash);
        if (this.count(query) > 0) {
            log.info("NFT Taken Event already exists: {}", txHash);
            return;
        }

        NftTakenEvent entity = new NftTakenEvent();
        entity.setTxHash(txHash);
        entity.setWalletAddress(walletAddress);
        entity.setEventType(eventType);
        entity.setKioskId(kioskId);
        entity.setNftObjectId(nftObjectId);
        if (timestampMs != null) {
            entity.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMs), ZoneId.systemDefault()));
        }
        this.save(entity);
        log.info("Saved NFTTaken event: {}", txHash);
        
        processedEventService.markProcessed(txHash, eventType);
    }
}