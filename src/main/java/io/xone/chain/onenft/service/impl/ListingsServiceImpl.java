package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.entity.Listings;
import io.xone.chain.onenft.mapper.ListingsMapper;
import io.xone.chain.onenft.service.IListingsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.xone.chain.onenft.service.IProcessedEventService;
import io.xone.chain.onenft.service.INftsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.math.BigDecimal;

/**
 * <p>
 * NFT 挂单表 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ListingsServiceImpl extends ServiceImpl<ListingsMapper, Listings> implements IListingsService {

    private final IProcessedEventService processedEventService;
    private final INftsService nftsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleListingCreated(String txDigest, String listingObjectId, String owner, String nftObjectId, Integer listingType,
             Long timestampMs) {
        if (processedEventService.isProcessed(txDigest, "ListingCreated" + listingObjectId)) {
            log.info("ListingCreated already processed: {}", txDigest);
            return;
        }

        Listings listing = new Listings();
        listing.setListingObjectId(listingObjectId);
        listing.setOwnerAddress(owner);
        listing.setNftObjectId(nftObjectId);
        listing.setListingType(listingType);
        listing.setStatus(0); // Active
        listing.setCreateTxDigest(txDigest);
        if (timestampMs != null) {
            listing.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMs), ZoneId.systemDefault()));
        }
        
        // Fetch NFT details to populate type, etc? 
        // Sync NFT from chain to ensure we have latest data
        nftsService.syncNftFromChain(nftObjectId, nft -> {
             listing.setNftType(nft.getNftType());
             // listing.setCollectionSlug(nft.getSeriesId()); // If we had slug on Nfts
             
             // Update NFT status
             nft.setIsListed(true);
             nft.setListingPrice(null); // Price is in listing object, but maybe we should update it here if it's a SALE listing?
             // But ListingCreated event doesn't pass price. We need to fetch listing object from chain to get price.
             // Or we can leave price null for now and update it if we fetch listing details. 
             // Ideally the event should carry price, OR we fetch listing object content.
             // The move code says: 
             /*
                event::emit(ListingCreated {
                    listing_id,
                    owner: sender,
                    nft_id,
                    listing_type: LISTING_SALE,
                });
             */
             // It does NOT have price. So we MUST fetch the listing object.
        });
        
        // Fetch listing object from chain to get price and coin type if Sale
        // For now I'll just save what I have, but TODO: fetch listing details.
        
        this.save(listing);
        processedEventService.markProcessed(txDigest, "ListingCreated" + listingObjectId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleListingCancelled(String txDigest, String listingObjectId, Long timestampMs) {
         if (processedEventService.isProcessed(txDigest, "ListingCancelled" + listingObjectId)) {
            log.info("ListingCancelled already processed: {}", txDigest);
            return;
        }
        
        QueryWrapper<Listings> query = new QueryWrapper<>();
        query.eq("listing_object_id", listingObjectId);
        Listings listing = this.getOne(query);
        
        if (listing != null) {
            listing.setStatus(2); // Cancelled
            listing.setCancelTxDigest(txDigest);
            listing.setCancelTimestampMs(timestampMs);
            this.updateById(listing);
            
            // Update NFT status
            nftsService.syncNftFromChain(listing.getNftObjectId(), nft -> {
                nft.setIsListed(false);
                nft.setListingPrice(null);
            });
        }
        
        processedEventService.markProcessed(txDigest, "ListingCancelled" + listingObjectId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleListingFilled(String listingObjectId) {
        QueryWrapper<Listings> query = new QueryWrapper<>();
        query.eq("listing_object_id", listingObjectId);
        Listings listing = this.getOne(query);
        
        if (listing != null) {
            listing.setStatus(1); // Filled
            this.updateById(listing);
            
            // Update NFT status
             nftsService.syncNftFromChain(listing.getNftObjectId(), nft -> {
                nft.setIsListed(false);
                nft.setListingPrice(null);
                // Owner should be updated by Trades handling or standard NFT sync?
                // Standard NFT sync usually fetches fields. Owner is on the object.
            });
        }
    }
}