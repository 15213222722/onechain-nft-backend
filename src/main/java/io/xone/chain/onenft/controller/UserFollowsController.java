package io.xone.chain.onenft.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.xone.chain.onenft.common.entity.Result;
import io.xone.chain.onenft.entity.UserFollows;
import io.xone.chain.onenft.request.UserFollowsRequest;
import io.xone.chain.onenft.request.UserIsMyFollowingRequest;
import io.xone.chain.onenft.request.UserToggleFollowRequest;
import io.xone.chain.onenft.resp.UserResp;

/**
 * <p>
 * 关注表 前端控制器
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-20
 */
@RestController
@RequestMapping("/userFollows")
public class UserFollowsController {

    @org.springframework.beans.factory.annotation.Autowired
    private io.xone.chain.onenft.service.IUserFollowsService userFollowsService;

    @org.springframework.beans.factory.annotation.Autowired
    private io.xone.chain.onenft.service.IUsersService usersService;

    @PostMapping("/follow")
    public Result<Boolean> follow(@RequestBody UserToggleFollowRequest request) {
        String currentWalletAddress = usersService.getCurrentUser().getWalletAddress();
        String followingAddress = request.getFollowingAddress();
        
        if(currentWalletAddress.equals(followingAddress)) {
            return Result.failed("不能关注自己");
        }
        boolean validUser = usersService.isValidUser(followingAddress);
        if(!validUser) {
            return Result.failed("关注用户不存在");
        }
        
        if (Boolean.TRUE.equals(request.getIsFollow())) {
            return Result.success(userFollowsService.follow(currentWalletAddress, followingAddress));
        } else {
            return Result.success(userFollowsService.unfollow(currentWalletAddress, followingAddress));
        }
    }

    @PostMapping("/followings")
    public Result<Page<UserResp>> getFollowings(@RequestBody UserFollowsRequest request) {
        
        String walletAddress = request.getWalletAddress();
        Page<UserFollows> p = new Page<>(request.getCurrent(), request.getSize());
        return Result.success(userFollowsService.getFollowings(walletAddress, p));
    }

    @PostMapping("/followers")
    public Result<Page<UserResp>> getFollowers(@RequestBody UserFollowsRequest request) {
        
        String walletAddress = request.getWalletAddress();
        Page<UserFollows> p = new Page<>(request.getCurrent(), request.getSize());
        return Result.success(userFollowsService.getFollowers(walletAddress, p));
    }
    
    @PostMapping("/isMyFollowing")
    public Result<Boolean> isMyFollowing(@RequestBody UserIsMyFollowingRequest request) {
        return Result.success(userFollowsService.isMyFollowing(request.getWalletAddress()));
    }
    
}