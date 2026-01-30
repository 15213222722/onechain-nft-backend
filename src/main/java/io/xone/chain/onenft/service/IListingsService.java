package io.xone.chain.onenft.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import io.xone.chain.onenft.entity.Listings;
import io.xone.chain.onenft.request.MyListingNftRequest;
import io.xone.chain.onenft.resp.ListingResp;

/**
 * <p>
 * NFT 挂单表 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
public interface IListingsService extends IService<Listings> {

    void handleListingCreated(String txDigest, String listingObjectId, String owner, String nftObjectId, String nftType, Integer listingType, 
            Long price, String coinType, String expectedNftType, Long timestampMs);
    
    void handleListingCancelled(String txDigest, String listingObjectId, Long timestampMs);

    void handleListingFilled(String listingObjectId);

    IPage<ListingResp> getMyListings(MyListingNftRequest request);
}