package io.xone.chain.onenft.service;

import io.xone.chain.onenft.entity.NftLikes;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 喜欢表 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-20
 */
public interface INftLikesService extends IService<NftLikes> {

    /**
     * toggle like
     * @param userId
     * @param nftObjectId
     * @return true if liked, false if unliked
     */
    boolean toggleLike(Integer userId, String nftObjectId);
}