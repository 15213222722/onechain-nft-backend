package io.xone.chain.onenft.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;

import io.xone.chain.onenft.common.Result;
import io.xone.chain.onenft.request.UserActivityQueryRequest;
import io.xone.chain.onenft.resp.UserActivityResp;
import io.xone.chain.onenft.service.IUserActivitiesService;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 用户活动记录表 前端控制器
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-10
 */
@RestController
@RequestMapping("/userActivities")
@RequiredArgsConstructor
public class UserActivitiesController {

    private final IUserActivitiesService userActivitiesService;

    @PostMapping("/query")
    public Result<IPage<UserActivityResp>> query(@Validated @RequestBody UserActivityQueryRequest request) {
        return Result.success(userActivitiesService.queryUserActivities(request));
    }
}