package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.entity.NftDelistEvent;
import io.xone.chain.onenft.mapper.NftDelistEventMapper;
import io.xone.chain.onenft.service.INftDelistEventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.xone.chain.onenft.service.INftsService;
import io.xone.chain.onenft.service.IProcessedEventService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 下架事件表 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-26
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NftDelistEventServiceImpl extends ServiceImpl<NftDelistEventMapper, NftDelistEvent> implements INftDelistEventService {

    private final INftsService nftsService;
    private final IProcessedEventService processedEventService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleNFTDelistEvent(String txHash, String walletAddress, String eventType, String kioskId, String nftObjectId, Long timestampMs) {
        // Check processed
        if (processedEventService.isProcessed(txHash, eventType)) {
            log.info("NFT Delist Event already processed: {}", txHash);
            return;
        }

        // Update NFT using common service
        nftsService.syncNftFromChain(nftObjectId, nft -> {
            nft.setIsListed(false);
            // Updating listing price to null
            nft.setListingPrice(null);
        });

        // Check duplicate
        QueryWrapper<NftDelistEvent> query = new QueryWrapper<>();
        query.eq("txHash", txHash);
        if (this.count(query) > 0) {
            log.info("NFT Delist Event already exists: {}", txHash);
            return;
        }

        NftDelistEvent entity = new NftDelistEvent();
        entity.setTxHash(txHash);
        entity.setWalletAddress(walletAddress);
        entity.setEventType(eventType);
        entity.setKioskId(kioskId);
        entity.setNftObjectId(nftObjectId);
        if (timestampMs != null) {
            entity.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMs), ZoneId.systemDefault()));
        }
        this.save(entity);
        log.info("Saved NFTDelist event: {}", txHash);

        processedEventService.markProcessed(txHash, eventType);
    }
}