package io.xone.chain.onenft.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.xone.chain.onenft.common.entity.Result;
import io.xone.chain.onenft.request.UserCollectionQueryRequest;
import io.xone.chain.onenft.request.UserCollectionRequest;
import io.xone.chain.onenft.resp.UserCollectionResp;
import io.xone.chain.onenft.service.IUserCollectionsService;
import io.xone.chain.onenft.service.IUsersService;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 收藏表 前端控制器
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-19
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/userCollections")
public class UserCollectionsController {

	private final IUserCollectionsService userCollectionsService;
    private final IUsersService usersService;

    @PostMapping("/list")
    public Result<Page<UserCollectionResp>> list(@RequestBody UserCollectionQueryRequest request) {
        if (request.getWalletAddress() == null) {
            String currentWalletAddress = usersService.getCurrentUser().getWalletAddress();
            request.setWalletAddress(currentWalletAddress);
        }
        return Result.success(userCollectionsService.getUserCollections(request));
    }

    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody UserCollectionRequest request) {
        String currentWalletAddress = usersService.getCurrentUser().getWalletAddress();
        return Result.success(userCollectionsService.addCollection(currentWalletAddress, request.getNftObjectId()));
    }

    @PostMapping("/remove")
    public Result<Boolean> remove(@RequestBody UserCollectionRequest request) {
        String currentWalletAddress = usersService.getCurrentUser().getWalletAddress();
        return Result.success(userCollectionsService.removeCollection(currentWalletAddress, request.getNftObjectId()));
    }

    @PostMapping("/isCollected")
    public Result<Boolean> isCollected(@RequestBody UserCollectionRequest request) {
        String currentWalletAddress = usersService.getCurrentUser().getWalletAddress();
        return Result.success(userCollectionsService.isCollected(currentWalletAddress, request.getNftObjectId()));
    }
}
