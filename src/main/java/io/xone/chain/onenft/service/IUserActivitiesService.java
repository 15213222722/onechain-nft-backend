package io.xone.chain.onenft.service;

import io.xone.chain.onenft.entity.UserActivities;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.xone.chain.onenft.request.UserActivityQueryRequest;
import io.xone.chain.onenft.resp.UserActivityResp;

/**
 * <p>
 * 用户活动记录表 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-10
 */
public interface IUserActivitiesService extends IService<UserActivities> {

    /**
     * Query user activities with pagination
     * @param request
     * @return
     */
    IPage<UserActivityResp> queryUserActivities(UserActivityQueryRequest request);
}