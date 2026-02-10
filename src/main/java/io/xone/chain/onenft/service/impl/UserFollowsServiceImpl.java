package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.entity.UserFollows;
import io.xone.chain.onenft.entity.Users;
import io.xone.chain.onenft.enums.ActivityTargetTypeEnum;
import io.xone.chain.onenft.enums.ActivityTypeEnum;
import io.xone.chain.onenft.event.ActivityEvent;
import io.xone.chain.onenft.mapper.UserFollowsMapper;
import io.xone.chain.onenft.service.IUserFollowsService;
import io.xone.chain.onenft.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.time.LocalDateTime;

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
    public boolean toggleFollow(Integer followerId, Integer followingId) {
        LambdaQueryWrapper<UserFollows> query = new LambdaQueryWrapper<>();
        query.eq(UserFollows::getFollowerUserId, followerId)
             .eq(UserFollows::getFollowingId, followingId);
        
        long count = count(query);
        if (count > 0) {
            remove(query);
            return false;
        } else {
            UserFollows follow = new UserFollows();
            follow.setFollowerUserId(followerId);
            follow.setFollowingId(followingId);
            follow.setCreatedAt(LocalDateTime.now());
            follow.setUpdatedAt(LocalDateTime.now());
            boolean success = save(follow);
            
            if (success) {
                String followerAddress = getActorAddress(followerId);
                String followingAddress = getActorAddress(followingId);
                
                eventPublisher.publishEvent(ActivityEvent.builder(this)
                        .activityType(ActivityTypeEnum.USER_FOLLOWED)
                        .actorAddress(followerAddress)
                        .targetType(ActivityTargetTypeEnum.USER)
                        .targetId(followingAddress)
                        .addMetadata("description", "User " + followerId + " followed user " + followingId)
                        .build());
            }
            return true;
        }
    }

    private String getActorAddress(Integer userId) {
        if (userId == null) return null;
        Users user = usersService.getById(userId);
        return user != null ? user.getWalletAddress() : null;
    }
}