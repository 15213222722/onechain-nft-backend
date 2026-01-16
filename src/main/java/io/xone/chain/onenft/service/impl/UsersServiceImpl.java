package io.xone.chain.onenft.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.dev33.satoken.stp.StpUtil;
import io.xone.chain.onenft.common.ApiException;
import io.xone.chain.onenft.dto.UserLoginDto;
import io.xone.chain.onenft.entity.Users;
import io.xone.chain.onenft.mapper.UsersMapper;
import io.xone.chain.onenft.service.IUsersService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-16
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

	@Override
	public String login(UserLoginDto loginDto) {
		if (StringUtils.isEmpty(loginDto.getWalletAddress())) {
			throw new ApiException("Wallet address cannot be empty");
		}

		LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Users::getWalletAddress, loginDto.getWalletAddress());
		Users user = this.getOne(queryWrapper);

		if (user == null) {
			user = new Users();
			user.setWalletAddress(loginDto.getWalletAddress());
			user.setCreatedAt(LocalDateTime.now());
			user.setUpdatedAt(LocalDateTime.now());
			user.setRole("user");
			this.save(user);
		} else {
			user.setLastSignedIn(LocalDateTime.now());
			this.updateById(user);
		}
		StpUtil.login(loginDto.getWalletAddress());
		return StpUtil.getTokenValue();
	}
}