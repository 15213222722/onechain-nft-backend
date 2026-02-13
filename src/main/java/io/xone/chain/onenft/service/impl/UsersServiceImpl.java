package io.xone.chain.onenft.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import io.xone.chain.onenft.common.ApiException;
import io.xone.chain.onenft.entity.Collections;
import io.xone.chain.onenft.entity.UserActivities;
import io.xone.chain.onenft.entity.UserActivityStats;
import io.xone.chain.onenft.entity.UserFollows;
import io.xone.chain.onenft.entity.Users;
import io.xone.chain.onenft.enums.ActivityTypeEnum;
import io.xone.chain.onenft.mapper.CollectionsMapper;
import io.xone.chain.onenft.mapper.UserActivitiesMapper;
import io.xone.chain.onenft.mapper.UserActivityStatsMapper;
import io.xone.chain.onenft.mapper.UserFollowsMapper;
import io.xone.chain.onenft.mapper.UsersMapper;
import io.xone.chain.onenft.request.UserInfoRequest;
import io.xone.chain.onenft.request.UserLoginRequest;
import io.xone.chain.onenft.request.UserUpdateRequest;
import io.xone.chain.onenft.resp.UserResp;
import io.xone.chain.onenft.resp.UserStatsResp;
import io.xone.chain.onenft.service.IUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-16
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

	private final UserFollowsMapper userFollowsMapper;
	private final CollectionsMapper collectionsMapper;
	private final UserActivityStatsMapper userActivityStatsMapper;
	private final UserActivitiesMapper userActivitiesMapper;

	@Override
	public String login(UserLoginRequest request) {
		LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Users::getWalletAddress, request.getWalletAddress());
		Users user = this.getOne(queryWrapper);

		if (user == null) {
			user = new Users();
			user.setWalletAddress(request.getWalletAddress());
			user.setCreatedAt(LocalDateTime.now());
			user.setUpdatedAt(LocalDateTime.now());
			this.save(user);
		} else {
			user.setLastSignedIn(LocalDateTime.now());
			this.updateById(user);
		}

		// Use StpUtil for login to be compatible with LoginInterceptor
		StpUtil.login(user.getId(), request.getLoginDevice());
		return StpUtil.getTokenValue();
	}

	@Override
	public boolean updateUser(UserUpdateRequest request) {
		if (StringUtils.isEmpty(request.getWalletAddress())) {
			throw new ApiException("Wallet address cannot be empty");
		}

		LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Users::getWalletAddress, request.getWalletAddress());
		Users user = this.getOne(queryWrapper);

		if (user == null) {
			throw new ApiException("User not found");
		}

		// Security check: Ensure the logged-in user is updating their own profile
		int userId = StpUtil.getLoginIdAsInt();
		if (userId != user.getId()) {
			throw new ApiException("Cannot update other user's profile");
		}

		if (request.getName() != null)
			user.setName(request.getName());
		if (request.getEmail() != null)
			user.setEmail(request.getEmail());
		if (request.getTwitter() != null)
			user.setTwitter(request.getTwitter());
		if (request.getAvatarUrl() != null)
			user.setAvatarUrl(request.getAvatarUrl());
		if (request.getDescription() != null)
			user.setDescription(request.getDescription());

		user.setUpdatedAt(LocalDateTime.now());
		return this.updateById(user);
	}

	@Override
	public UserResp getCurrentUser() {
		int userId = StpUtil.getLoginIdAsInt();
		log.info("Getting current user info for userId: {}", userId);
		LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Users::getId, userId);
		Users user = this.getOne(queryWrapper);
		if (user == null) {
			return null;
		}
		UserResp userInfoNoStats = BeanUtil.copyProperties(user, UserResp.class);
		buildUserRespWithStats(userInfoNoStats);
		return userInfoNoStats;
	}

	private UserResp getUserInfoNoStats(UserInfoRequest request) {
		LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Users::getWalletAddress, request.getWalletAddress());
		Users user = this.getOne(queryWrapper);
		if (user == null) {
			return null;
		}
		return BeanUtil.copyProperties(user, UserResp.class);
	}

	@Override
	public UserResp getUserInfo(UserInfoRequest request) {
		UserResp userInfoNoStats = getUserInfoNoStats(request);
		if (userInfoNoStats == null) {
			return null;
		}
		buildUserRespWithStats(userInfoNoStats);
		return userInfoNoStats;
	}

	private void buildUserRespWithStats(UserResp userInfoNoStats) {

		// Fill Stats
		UserStatsResp stats = new UserStatsResp();

		// 1. Follows
		Integer followersCount = userFollowsMapper.selectCount(
				new LambdaQueryWrapper<UserFollows>().eq(UserFollows::getFollowingWalletAddress, userInfoNoStats.getWalletAddress()));
		stats.setFollowersCount(followersCount);

		Integer followingCount = userFollowsMapper.selectCount(
				new LambdaQueryWrapper<UserFollows>().eq(UserFollows::getFollowerWalletAddress, userInfoNoStats.getWalletAddress()));
		stats.setFollowingCount(followingCount);

		// 2. Collected (Count items in Collections table for this user)
		Integer collectedCount = collectionsMapper
				.selectCount(new LambdaQueryWrapper<Collections>().eq(Collections::getUserId, userInfoNoStats.getId()));
		stats.setCollectedCount(collectedCount);

		// 3. Created (Count NFT_MINTED activities)
		Integer createdCount = userActivitiesMapper.selectCount(new LambdaQueryWrapper<UserActivities>()
				.eq(UserActivities::getActorAddress, userInfoNoStats.getWalletAddress())
				.eq(UserActivities::getActivityType, ActivityTypeEnum.NFT_MINTED.getValue()));
		stats.setCreatedCount(createdCount);

		// 4. Volume
		UserActivityStats activityStats = userActivityStatsMapper.selectById(userInfoNoStats.getWalletAddress());
		if (activityStats != null && activityStats.getTotalVolumeRaw() != null) {
			stats.setTotalVolume(activityStats.getTotalVolumeRaw());
		} else {
			stats.setTotalVolume(0L);
		}
		userInfoNoStats.setStats(stats);
	}

	@Override
	public Users queryUserNameByAddress(String walletAddress) {
		LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Users::getWalletAddress, walletAddress);
		return this.getOne(queryWrapper);
	}

	@Override
	public boolean isValidUser(String walletAddress) {
		LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Users::getWalletAddress, walletAddress);
		Users user = this.getOne(queryWrapper);
		if (user == null) {
			return false;
		}
		return true;
	}
}