package io.xone.chain.onenft.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.xone.chain.onenft.entity.Nfts;
import com.baomidou.mybatisplus.extension.service.IService;
import io.xone.chain.onenft.request.NftSearchRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-16
 */
public interface INftsService extends IService<Nfts> {

    /**
     * Search NFTs
     * @param request Search Request
     * @return Paginated NFTs
     */
    IPage<Nfts> searchNfts(NftSearchRequest request);

    /**
     * Get NFT by Object ID
     * @param objectId Object ID
     * @return NFT Details
     */
    Nfts getByObjectId(String objectId);
}