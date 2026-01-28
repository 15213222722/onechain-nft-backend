package io.xone.chain.onenft.service;

import io.xone.chain.onenft.entity.Listings;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * NFT 挂单表 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
public interface IListingsService extends IService<Listings> {

    void handleListingCreated(String txDigest, String listingObjectId, String owner, String nftObjectId, Integer listingType, 
            Long timestampMs);
    
    void handleListingCancelled(String txDigest, String listingObjectId, Long timestampMs);

    void handleListingFilled(String listingObjectId);
}