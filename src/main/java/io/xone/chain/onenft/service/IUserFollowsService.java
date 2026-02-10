package io.xone.chain.onenft.service;

import io.xone.chain.onenft.entity.UserFollows;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 关注表 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-20
 */
public interface IUserFollowsService extends IService<UserFollows> {

    /**
     * toggle follow
     * @param followerId
     * @param followingId
     * @return true if followed, false if unfollowed
     */
    boolean toggleFollow(Integer followerId, Integer followingId);
}