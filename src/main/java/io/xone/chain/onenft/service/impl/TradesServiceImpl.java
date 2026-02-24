package io.xone.chain.onenft.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import io.xone.chain.onenft.common.entity.ListingNft;
import io.xone.chain.onenft.common.service.OneChainService;
import io.xone.chain.onenft.entity.Listings;
import io.xone.chain.onenft.entity.Trades;
import io.xone.chain.onenft.entity.Users;
import io.xone.chain.onenft.enums.ActivityTargetTypeEnum;
import io.xone.chain.onenft.enums.ActivityTypeEnum;
import io.xone.chain.onenft.enums.NotificationType;
import io.xone.chain.onenft.event.ActivityEvent;
import io.xone.chain.onenft.mapper.TradesMapper;
import io.xone.chain.onenft.request.TradeQueryRequest;
import io.xone.chain.onenft.resp.TradeResp;
import io.xone.chain.onenft.resp.UserResp;
import io.xone.chain.onenft.service.IListingsService;
import io.xone.chain.onenft.service.INftSaleFilledEventService;
import io.xone.chain.onenft.service.INftSwapFilledEventService;
import io.xone.chain.onenft.service.INotificationsService;
import io.xone.chain.onenft.service.IProcessedEventService;
import io.xone.chain.onenft.service.ITradesService;
import io.xone.chain.onenft.service.IUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private final INftSaleFilledEventService nftSaleFilledEventService;
    private final INftSwapFilledEventService nftSawpFilledEventService;
    private final ApplicationEventPublisher eventPublisher;
    private final IUsersService usersService;
    private final OneChainService oneChainService;
    private final INotificationsService notificationsService;

    private String formatAddress(String address) {
        if (StrUtil.isBlank(address) || address.length() < 10) return address;
        return address.substring(0, 6) + "..." + address.substring(address.length() - 4);
    }
    
    private String formatNftName(String nftId) {
        ListingNft nft = oneChainService.queryNftDetail(nftId);
        String name = (nft != null && nft.getName() != null) ? nft.getName() : "Unknown";
        String shortId = (nftId.length() > 4) ? nftId.substring(nftId.length() - 4) : nftId;
        return name + " #" + shortId;
    }

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
        
        nftSaleFilledEventService.handleNFTSaleFilledEvent(txDigest, listingObjectId, taker, lister, nftId, paymentAmount, feeAmount, sellerAmount, coinType, timestampMs);

        processedEventService.markProcessed(txDigest, "ListingSaleFilled" + listingObjectId);

        Listings listing = listingsService.queryListingByListingObjectId(listingObjectId);
        
        eventPublisher.publishEvent(ActivityEvent.builder(this)
                .activityType(ActivityTypeEnum.NFT_SOLD)
                .actorAddress(lister) // Seller
                .targetType(ActivityTargetTypeEnum.NFT)
                .targetId(nftId)
                .addMetadata("name", listing != null && StrUtil.isNotBlank(listing.getCollectionName())
						? listing.getCollectionName()
						: listingObjectId)
                .addMetadata("buyer", taker)
                .addMetadata("price", paymentAmount)
                .txDigest(txDigest)
                .addMetadata("description", String.format("Sale filled for listing %s", listingObjectId))
                .occurredAt(timestampMs)
                .build());

        eventPublisher.publishEvent(ActivityEvent.builder(this)
                .activityType(ActivityTypeEnum.NFT_BOUGHT)
                .actorAddress(taker) // Buyer
                .targetType(ActivityTargetTypeEnum.NFT)
                .targetId(nftId)
                .addMetadata("seller", lister)
                .addMetadata("price", paymentAmount)
                .txDigest(txDigest)
                .addMetadata("name", StrUtil.isNotBlank(listing.getCollectionName()) ? listing.getCollectionName() : listingObjectId)
                .addMetadata("description", String.format("User bought NFT %s", nftId))
                .occurredAt(timestampMs)
                .build());
        
        // Notifications
        String nftName = formatNftName(nftId);
        String takerAddrFormatted = formatAddress(taker);
        String listerAddrFormatted = formatAddress(lister);

        // Notify Seller (lister): Your NFT {0} sold to {1} for {2} {3}
        notificationsService.createNotification(NotificationType.NFT_SOLD, taker, lister, 
                "NFT", nftId, nftName, takerAddrFormatted, paymentAmount/1000000000, coinType.split("::")[2]);
        
        // Notify Buyer (taker): You bought NFT {0} from {1} for {2} {3}
        notificationsService.createNotification(NotificationType.NFT_BOUGHT, lister, taker, 
                "NFT", nftId, nftName, listerAddrFormatted, paymentAmount/1000000000, coinType.split("::")[2]);
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

        nftSawpFilledEventService.handleNFTSwapFilledEvent(txDigest, listingObjectId, taker, lister, nftIdOut, nftIdIn, timestampMs);	
        
        eventPublisher.publishEvent(ActivityEvent.builder(this)
                .activityType(ActivityTypeEnum.NFT_SWAPPED)
                .actorAddress(lister)
                .targetType(ActivityTargetTypeEnum.NFT)
                .targetId(nftIdOut) // NFT going out
                .addMetadata("taker", taker)
                .addMetadata("lister", lister)
                .addMetadata("nftIdOut", nftIdOut)
                .addMetadata("nftIdIn", nftIdIn)
                .txDigest(txDigest)
                .occurredAt(timestampMs)
                .build());
        
        // Notifications
        String nftOutName = formatNftName(nftIdOut);
        String nftInName = formatNftName(nftIdIn);
        String takerAddrFormatted = formatAddress(taker);
        String listerAddrFormatted = formatAddress(lister);

        // Lister notification: Swapped NFT {0} with {1} (Counterparty)
        notificationsService.createNotification(NotificationType.NFT_SWAPPED, taker, lister, 
                "NFT", nftIdOut, nftOutName, takerAddrFormatted);
        
        // Taker notification: Swapped NFT {0} with {1} (Counterparty)
        notificationsService.createNotification(NotificationType.NFT_SWAPPED, lister, taker, 
                "NFT", nftIdIn, nftInName, listerAddrFormatted);

    }

    @Override
    public Page<TradeResp> queryTrades(TradeQueryRequest request) {
        Page<Trades> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<Trades> query = new LambdaQueryWrapper<>();
        
        long now = System.currentTimeMillis();
        long startTime = 0;
        
        if (StrUtil.isNotBlank(request.getTimeRange())) {
            switch (request.getTimeRange().toUpperCase()) {
                case "24H":
                    startTime = now - (24 * 60 * 60 * 1000L);
                    break;
                case "7D":
                    startTime = now - (7 * 24 * 60 * 60 * 1000L);
                    break;
                case "30D":
                    startTime = now - (30L * 24 * 60 * 60 * 1000L);
                    break;
                case "ALL":
                default:
                    startTime = 0;
                    break;
            }
        }
        
        if (startTime > 0) {
            query.ge(Trades::getTimestampMs, startTime);
        }
        
        if (StrUtil.isNotBlank(request.getNftObjectId())) {
            query.eq(Trades::getNftObjectIdOut, request.getNftObjectId());
        }
        
        // If collectionSlug is provided, we'd need to join with collection table or filter by existing field.
        // Assuming collectionSlug is stored in Trades or we can filter by it.
        // Trades entity has `collection_slug`? Let's check Trades.java again.
        if (StrUtil.isNotBlank(request.getCollectionSlug())) {
             query.eq(Trades::getCollectionSlug, request.getCollectionSlug());
        }
        
        query.orderByDesc(Trades::getTimestampMs);
        
        Page<Trades> pResult = page(page, query);
        Page<TradeResp> respPage = new Page<>();
        BeanUtils.copyProperties(pResult, respPage, "records");
        
        if (pResult.getRecords().isEmpty()) {
            return respPage;
        }
        
        Set<String> userAddresses = new java.util.HashSet<>();
        Set<String> nftObjectIds = new java.util.HashSet<>();
        
        for (Trades trade : pResult.getRecords()) {
            if (StrUtil.isNotBlank(trade.getTakerAddress())) userAddresses.add(trade.getTakerAddress());
            if (StrUtil.isNotBlank(trade.getListerAddress())) userAddresses.add(trade.getListerAddress());
            if (StrUtil.isNotBlank(trade.getNftObjectIdOut())) nftObjectIds.add(trade.getNftObjectIdOut());
        }
        
        Map<String, UserResp> userMap = new java.util.HashMap<>();
        if (!userAddresses.isEmpty()) {
            LambdaQueryWrapper<Users> userQuery = new LambdaQueryWrapper<>();
            userQuery.in(Users::getWalletAddress, userAddresses);
            List<Users> users = usersService.list(userQuery);
            for (Users u : users) {
                UserResp r = new UserResp();
                BeanUtils.copyProperties(u, r);
                userMap.put(u.getWalletAddress(), r);
            }
        }
        
        Map<String, ListingNft> nftMap = new java.util.HashMap<>();
        if (!nftObjectIds.isEmpty()) {
            List<ListingNft> nfts = oneChainService.getListedNfts(new ArrayList<>(nftObjectIds));
            if (nfts != null) {
                for (ListingNft nft : nfts) {
                    nftMap.put(nft.getObjectId(), nft);
                }
            }
        }
        
        List<TradeResp> records = new ArrayList<>();
        for (Trades trade : pResult.getRecords()) {
            TradeResp resp = new TradeResp();
            resp.setId(trade.getId());
            resp.setPrice(trade.getPaymentAmount());
            resp.setCoinType(trade.getCoinType());
            resp.setTimestamp(trade.getTimestampMs());
            resp.setTradeType(trade.getTradeType()); // Assuming tradeType is compatible or need conversion
            
            if (StrUtil.isNotBlank(trade.getTakerAddress())) {
                resp.setBuyer(userMap.getOrDefault(trade.getTakerAddress(), createUnknownUser(trade.getTakerAddress())));
            }
            if (StrUtil.isNotBlank(trade.getListerAddress())) {
                resp.setSeller(userMap.getOrDefault(trade.getListerAddress(), createUnknownUser(trade.getListerAddress())));
            }
            
            if (StrUtil.isNotBlank(trade.getNftObjectIdOut())) {
                resp.setNftInfo(nftMap.get(trade.getNftObjectIdOut()));
                if (resp.getNftInfo() == null) {
                    // Create minimal info if fetch failed
                    ListingNft minimal = new ListingNft();
                    minimal.setObjectId(trade.getNftObjectIdOut());
                    resp.setNftInfo(minimal);
                }
            }
            
            records.add(resp);
        }
        
        respPage.setRecords(records);
        return respPage;
    }
    
    private UserResp createUnknownUser(String address) {
        UserResp u = new UserResp();
        u.setWalletAddress(address);
        u.setName("Unknown");
        return u;
    }
}
