package io.xone.chain.onenft.service;

import com.baomidou.mybatisplus.extension.service.IService;

import io.xone.chain.onenft.entity.Users;
import io.xone.chain.onenft.request.UserInfoRequest;
import io.xone.chain.onenft.request.UserLoginRequest;
import io.xone.chain.onenft.request.UserUpdateRequest;
import io.xone.chain.onenft.resp.UserResp;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-16
 */
public interface IUsersService extends IService<Users> {

    /**
     * User Login
     * @param request
     * @return Token
     */
    String login(UserLoginRequest request);

    /**
     * Update User Info
     * @param request
     * @return Boolean
     */
    boolean updateUser(UserUpdateRequest request);

    /**
     * Get Current User Info
     * @return UserResp
     */
    UserResp getCurrentUser();

    /**
     * Get User Info
     * @param request
     * @return UserResp
     */
	UserResp getUserInfo(UserInfoRequest request);

	Users queryUserNameByAddress(String actorAddress);
}