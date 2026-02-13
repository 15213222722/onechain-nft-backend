package io.xone.chain.onenft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.stp.StpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.xone.chain.onenft.common.Result;
import io.xone.chain.onenft.request.UserInfoRequest;
import io.xone.chain.onenft.request.UserLoginRequest;
import io.xone.chain.onenft.request.UserUpdateRequest;
import io.xone.chain.onenft.resp.UserResp;
import io.xone.chain.onenft.service.IUsersService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-16
 */
@Api(tags = "Users Management")
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private IUsersService usersService;

    @ApiOperation("Login with Wallet Address")
    @PostMapping("/login")
    public Result<String> login(@RequestBody @Validated UserLoginRequest request) {
        String token = usersService.login(request);
        return Result.success(token);
    }
    
    @ApiOperation("Logout")
    @PostMapping("/logout")
    public Result<Boolean> logout() {
        StpUtil.logout();
        return Result.success(true);
    }

    @ApiOperation("Update User Info")
    @PostMapping("/update")
    public Result<Boolean> update(@Validated @RequestBody UserUpdateRequest request) {
        return Result.success(usersService.updateUser(request));
    }
    
    @ApiOperation("Get Current User Info")
    @PostMapping("/getCurrentUser")
    public Result<UserResp> getCurrentUser() {
        return Result.success(usersService.getCurrentUser());
    }

    @ApiOperation("Get Current User Info")
    @PostMapping("/info")
    public Result<UserResp> info(@Validated @RequestBody UserInfoRequest request) {
        return Result.success(usersService.getUserInfo(request));
    }
}