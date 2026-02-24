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
     * Follow a user
     * @param followerAddress
     * @param followingAddress
     * @return true if successful, throws exception if already following
     */
    boolean follow(String followerAddress, String followingAddress);

    /**
     * Unfollow a user
     * @param followerAddress
     * @param followingAddress
     * @return true if successful
     */
    boolean unfollow(String followerAddress, String followingAddress);

    /**
     * get followings
     * @param walletAddress
     * @param page
     * @return
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<io.xone.chain.onenft.resp.UserResp> getFollowings(String walletAddress, com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserFollows> page);

    /**
     * get followers
     * @param walletAddress
     * @param page
     * @return
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<io.xone.chain.onenft.resp.UserResp> getFollowers(String walletAddress, com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserFollows> page);

    /**
     * check if is following
     * @param followingAddress
     * @return
     */
    boolean isMyFollowing(String followingAddress);
}