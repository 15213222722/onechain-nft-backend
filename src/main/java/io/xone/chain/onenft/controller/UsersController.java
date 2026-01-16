package io.xone.chain.onenft.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.xone.chain.onenft.common.Result;
import io.xone.chain.onenft.dto.UserLoginDto;
import io.xone.chain.onenft.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result<String> login(@RequestBody UserLoginDto loginDto) {
        String token = usersService.login(loginDto);
        return Result.success(token);
    }
}