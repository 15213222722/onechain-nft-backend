package io.xone.chain.onenft.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import io.xone.chain.onenft.entity.UserActivities;
import io.xone.chain.onenft.entity.Users;
import io.xone.chain.onenft.mapper.UserActivitiesMapper;
import io.xone.chain.onenft.request.UserActivityQueryRequest;
import io.xone.chain.onenft.resp.UserActivityResp;
import io.xone.chain.onenft.service.IUserActivitiesService;
import io.xone.chain.onenft.service.IUsersService;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 用户活动记录表 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-10
 */
@Service
@RequiredArgsConstructor
public class UserActivitiesServiceImpl extends ServiceImpl<UserActivitiesMapper, UserActivities>
		implements IUserActivitiesService {
	private final IUsersService usersService;

	@Override
	public IPage<UserActivityResp> queryUserActivities(UserActivityQueryRequest request) {
		Page<UserActivities> page = new Page<>(request.getCurrent(), request.getSize());
		LambdaQueryWrapper<UserActivities> wrapper = new LambdaQueryWrapper<>();

		if (StrUtil.isNotBlank(request.getWalletAddress())) {
			wrapper.eq(UserActivities::getActorAddress, request.getWalletAddress());
		}

		if (StrUtil.isNotBlank(request.getActivityType())) {
			wrapper.eq(UserActivities::getActivityType, request.getActivityType());
		}

		wrapper.orderByDesc(UserActivities::getCreatedAt);

		IPage<UserActivities> result = this.page(page, wrapper);

		List<UserActivityResp> respList = result.getRecords().stream().map(this::mapToResp)
				.collect(Collectors.toList());

		Page<UserActivityResp> respPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
		respPage.setRecords(respList);

		return respPage;
	}

	private UserActivityResp mapToResp(UserActivities activity) {
		UserActivityResp resp = new UserActivityResp();
		resp.setId(activity.getId());
		resp.setActorAddress(activity.getActorAddress());
		resp.setActivityType(activity.getActivityType());
		resp.setTargetType(activity.getTargetType());
		resp.setTargetId(activity.getTargetId());
		resp.setCreatedAt(activity.getCreatedAt());
		resp.setMetadata(activity.getMetadata());
		resp.setTxDigest(activity.getTxDigest());
		Users actorUser = usersService.queryUserNameByAddress(activity.getActorAddress());
		if (actorUser != null) {
			resp.setActorName(actorUser.getName());
		}
		return resp;
	}

}