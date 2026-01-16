package io.xone.chain.onenft.service;

import io.xone.chain.onenft.dto.UserLoginDto;
import io.xone.chain.onenft.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

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
     * @param loginDto Login DTO
     * @return Token
     */
    String login(UserLoginDto loginDto);
}