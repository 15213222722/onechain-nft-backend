package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.entity.Trades;
import io.xone.chain.onenft.mapper.TradesMapper;
import io.xone.chain.onenft.service.ITradesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.xone.chain.onenft.service.IProcessedEventService;
import io.xone.chain.onenft.service.IListingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;

/**
 * <p>
 * NFT 成交记录表 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TradesServiceImpl extends ServiceImpl<TradesMapper, Trades> implements ITradesService {

    private final IProcessedEventService processedEventService;
    private final IListingsService listingsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleListingSaleFilled(String txDigest, String listingObjectId, String taker, String lister, String nftId,
                                        Long paymentAmount, Long feeAmount, Long sellerAmount, String coinType, Long timestampMs) {
        if (processedEventService.isProcessed(txDigest, "ListingSaleFilled" + listingObjectId)) {
            log.info("ListingSaleFilled already processed: {}", txDigest);
            return;
        }

        Trades trade = new Trades();
        trade.setListingObjectId(listingObjectId);
        trade.setTradeType((byte) 1); // Sale
        trade.setTakerAddress(taker);
        trade.setListerAddress(lister);
        trade.setNftObjectIdOut(nftId);
        trade.setPaymentAmount(paymentAmount);
        trade.setFeeAmount(feeAmount);
        trade.setSellerAmount(sellerAmount);
        trade.setCoinType(coinType);
        trade.setTxDigest(txDigest);
        trade.setTimestampMs(timestampMs);
        if (timestampMs != null) {
            trade.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMs), ZoneId.systemDefault()));
        }
        this.save(trade);

        // Update listing status
        listingsService.handleListingFilled(listingObjectId);

        processedEventService.markProcessed(txDigest, "ListingSaleFilled" + listingObjectId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleListingSwapFilled(String txDigest, String listingObjectId, String taker, String lister,
                                        String nftIdOut, String nftIdIn, Long timestampMs) {
        if (processedEventService.isProcessed(txDigest, "ListingSwapFilled" + listingObjectId)) {
            log.info("ListingSwapFilled already processed: {}", txDigest);
            return;
        }

        Trades trade = new Trades();
        trade.setListingObjectId(listingObjectId);
        trade.setTradeType((byte) 2); // Swap
        trade.setTakerAddress(taker);
        trade.setListerAddress(lister);
        trade.setNftObjectIdOut(nftIdOut);
        trade.setNftObjectIdIn(nftIdIn);
        trade.setTxDigest(txDigest);
        trade.setTimestampMs(timestampMs);
        if (timestampMs != null) {
            trade.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMs), ZoneId.systemDefault()));
        }
        this.save(trade);

        // Update listing status
        listingsService.handleListingFilled(listingObjectId);

        processedEventService.markProcessed(txDigest, "ListingSwapFilled" + listingObjectId);
    }
}