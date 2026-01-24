package io.xone.chain.onenft.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import io.xone.chain.onenft.entity.Nfts;
import io.xone.chain.onenft.request.MyKioskNftRequest;
import io.xone.chain.onenft.request.NftSearchRequest;
import io.xone.chain.onenft.resp.NftResp;

import java.util.function.Consumer;

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

    /**
     * Get NFT Detail with enriched info
     * @param objectId Object ID
     * @return NFT Response
     */
    NftResp getNftDetail(String objectId);

    /**
     * Get My NFTs
     * @return Paginated NFTs
     */
	IPage<Nfts> myKioskNfts(MyKioskNftRequest request);

	Nfts syncNftFromChain(String nftObjectId, Consumer<Nfts> customizer);
}
