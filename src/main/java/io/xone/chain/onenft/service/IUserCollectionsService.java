package io.xone.chain.onenft.service;

import io.xone.chain.onenft.entity.UserCollections;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 收藏表 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-19
 */
public interface IUserCollectionsService extends IService<UserCollections> {
    /**
     * Get user collections
     * @param request
     * @return
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<io.xone.chain.onenft.resp.UserCollectionResp> getUserCollections(io.xone.chain.onenft.request.UserCollectionQueryRequest request);
    
    /**
     * Add collection
     * @param walletAddress
     * @param nftObjectId
     * @return
     */
    boolean addCollection(String walletAddress, String nftObjectId);
    
    /**
     * Remove collection
     * @param walletAddress
     * @param nftObjectId
     * @return
     */
    boolean removeCollection(String walletAddress, String nftObjectId);
    
    /**
     * Is collected
     * @param walletAddress
     * @param nftObjectId
     * @return
     */
    boolean isCollected(String walletAddress, String nftObjectId);
}