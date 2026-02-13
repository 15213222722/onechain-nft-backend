package io.xone.chain.onenft.service.impl;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.dev33.satoken.stp.StpUtil;
import io.xone.chain.onenft.entity.UserFollows;
import io.xone.chain.onenft.entity.Users;
import io.xone.chain.onenft.enums.ActivityTargetTypeEnum;
import io.xone.chain.onenft.enums.ActivityTypeEnum;
import io.xone.chain.onenft.event.ActivityEvent;
import io.xone.chain.onenft.mapper.UserFollowsMapper;
import io.xone.chain.onenft.service.IUserFollowsService;
import io.xone.chain.onenft.service.IUsersService;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 关注表 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-20
 */
@Service
@RequiredArgsConstructor
public class UserFollowsServiceImpl extends ServiceImpl<UserFollowsMapper, UserFollows> implements IUserFollowsService {

	private final ApplicationEventPublisher eventPublisher;
	private final IUsersService usersService;

	@Override
	public boolean toggleFollow(String followerAddress, String followingAddress) {
		LambdaQueryWrapper<UserFollows> query = new LambdaQueryWrapper<>();
		query.eq(UserFollows::getFollowerWalletAddress, followerAddress).eq(UserFollows::getFollowingWalletAddress,
				followingAddress);

		long count = count(query);
		if (count > 0) {
			remove(query);
			return false;
		} else {
			UserFollows follow = new UserFollows();
			follow.setFollowerWalletAddress(followerAddress);
			follow.setFollowingWalletAddress(followingAddress);
			follow.setCreatedAt(LocalDateTime.now());
			follow.setUpdatedAt(LocalDateTime.now());
			boolean success = save(follow);

			if (success) {
				eventPublisher.publishEvent(ActivityEvent.builder(this).activityType(ActivityTypeEnum.USER_FOLLOWED)
						.actorAddress(followerAddress).targetType(ActivityTargetTypeEnum.USER)
						.targetId(followingAddress)
						.addMetadata("description", "User " + followerAddress + " followed user " + followingAddress)
						.build());
			}
			return true;
		}
	}

	@Override
	public boolean follow(String followerAddress, String followingAddress) {
		LambdaQueryWrapper<UserFollows> query = new LambdaQueryWrapper<>();
		query.eq(UserFollows::getFollowerWalletAddress, followerAddress).eq(UserFollows::getFollowingWalletAddress,
				followingAddress);

		long count = count(query);
		if (count > 0) {
			throw new io.xone.chain.onenft.common.ApiException("重复关注");
		}

		UserFollows follow = new UserFollows();
		follow.setFollowerWalletAddress(followerAddress);
		follow.setFollowingWalletAddress(followingAddress);
		follow.setCreatedAt(LocalDateTime.now());
		follow.setUpdatedAt(LocalDateTime.now());
		boolean success = save(follow);

		if (success) {
			eventPublisher.publishEvent(ActivityEvent.builder(this).activityType(ActivityTypeEnum.USER_FOLLOWED)
					.actorAddress(followerAddress).targetType(ActivityTargetTypeEnum.USER)
					.targetId(followingAddress)
					.addMetadata("description", "User " + followerAddress + " followed user " + followingAddress)
					.build());
		}
		return true;
	}

	@Override
	public boolean unfollow(String followerAddress, String followingAddress) {
		LambdaQueryWrapper<UserFollows> query = new LambdaQueryWrapper<>();
		query.eq(UserFollows::getFollowerWalletAddress, followerAddress).eq(UserFollows::getFollowingWalletAddress,
				followingAddress);
		return remove(query);
	}

	@Override
	public Page<io.xone.chain.onenft.resp.UserResp> getFollowings(String walletAddress, Page<UserFollows> page) {
		LambdaQueryWrapper<UserFollows> query = new LambdaQueryWrapper<>();
		query.eq(UserFollows::getFollowerWalletAddress, walletAddress);
		query.orderByDesc(UserFollows::getCreatedAt);

		Page<UserFollows> pResult = page(page, query);

		Page<io.xone.chain.onenft.resp.UserResp> respPage = new Page<>();
		respPage.setCurrent(pResult.getCurrent());
		respPage.setSize(pResult.getSize());
		respPage.setTotal(pResult.getTotal());

		if (pResult.getRecords().isEmpty()) {
			return respPage;
		}

		java.util.List<String> followingAddresses = pResult.getRecords().stream()
				.map(UserFollows::getFollowingWalletAddress).collect(java.util.stream.Collectors.toList());

		LambdaQueryWrapper<Users> userQuery = new LambdaQueryWrapper<>();
		userQuery.in(Users::getWalletAddress, followingAddresses);
		java.util.List<Users> users = usersService.list(userQuery);
		java.util.Map<String, Users> userMap = users.stream()
				.collect(java.util.stream.Collectors.toMap(Users::getWalletAddress, u -> u));

		java.util.List<io.xone.chain.onenft.resp.UserResp> records = new java.util.ArrayList<>();
		for (String addr : followingAddresses) {
			Users u = userMap.get(addr);
			if (u != null) {
				io.xone.chain.onenft.resp.UserResp r = new io.xone.chain.onenft.resp.UserResp();
				org.springframework.beans.BeanUtils.copyProperties(u, r);
				records.add(r);
			}
		}
		respPage.setRecords(records);
		return respPage;
	}

	@Override
	public Page<io.xone.chain.onenft.resp.UserResp> getFollowers(String walletAddress, Page<UserFollows> page) {
		LambdaQueryWrapper<UserFollows> query = new LambdaQueryWrapper<>();
		query.eq(UserFollows::getFollowingWalletAddress, walletAddress);
		query.orderByDesc(UserFollows::getCreatedAt);

		Page<UserFollows> pResult = page(page, query);

		Page<io.xone.chain.onenft.resp.UserResp> respPage = new Page<>();
		respPage.setCurrent(pResult.getCurrent());
		respPage.setSize(pResult.getSize());
		respPage.setTotal(pResult.getTotal());

		if (pResult.getRecords().isEmpty()) {
			return respPage;
		}

		java.util.List<String> followerAddresses = pResult.getRecords().stream()
				.map(UserFollows::getFollowerWalletAddress).collect(java.util.stream.Collectors.toList());

		LambdaQueryWrapper<Users> userQuery = new LambdaQueryWrapper<>();
		userQuery.in(Users::getWalletAddress, followerAddresses);
		java.util.List<Users> users = usersService.list(userQuery);
		java.util.Map<String, Users> userMap = users.stream()
				.collect(java.util.stream.Collectors.toMap(Users::getWalletAddress, u -> u));

		java.util.List<io.xone.chain.onenft.resp.UserResp> records = new java.util.ArrayList<>();
		for (String addr : followerAddresses) {
			Users u = userMap.get(addr);
			if (u != null) {
				io.xone.chain.onenft.resp.UserResp r = new io.xone.chain.onenft.resp.UserResp();
				org.springframework.beans.BeanUtils.copyProperties(u, r);
				records.add(r);
			}
		}
		respPage.setRecords(records);
		return respPage;
	}

	@Override
	public boolean isMyFollowing(String followingAddress) {
		int userId = StpUtil.getLoginIdAsInt();
		Users user = usersService.getById(userId);
		if (user == null) {
			return false;
		}
		String followerAddress = user.getWalletAddress();
		LambdaQueryWrapper<UserFollows> query = new LambdaQueryWrapper<>();
		query.eq(UserFollows::getFollowerWalletAddress, followerAddress);
		query.eq(UserFollows::getFollowingWalletAddress, followingAddress);
		return count(query) > 0;
	}
}
