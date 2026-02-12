package io.xone.chain.onenft.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import io.xone.chain.onenft.common.ApiException;
import io.xone.chain.onenft.entity.Users;
import io.xone.chain.onenft.mapper.UsersMapper;
import io.xone.chain.onenft.request.UserInfoRequest;
import io.xone.chain.onenft.request.UserLoginRequest;
import io.xone.chain.onenft.request.UserUpdateRequest;
import io.xone.chain.onenft.resp.UserResp;
import io.xone.chain.onenft.service.IUsersService;
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
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

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
		return BeanUtil.copyProperties(user, UserResp.class);
	}

	@Override
	public UserResp getUserInfo(UserInfoRequest request) {
		LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Users::getWalletAddress, request.getWalletAddress());
		Users user = this.getOne(queryWrapper);
		if (user == null) {
			return null;
		}
		return BeanUtil.copyProperties(user, UserResp.class);
	}

	@Override
	public Users queryUserNameByAddress(String walletAddress) {
		LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Users::getWalletAddress, walletAddress);
		return this.getOne(queryWrapper);
	}
}
