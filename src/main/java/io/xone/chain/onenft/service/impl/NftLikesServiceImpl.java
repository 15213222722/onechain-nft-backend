package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.entity.NftLikes;
import io.xone.chain.onenft.entity.Users;
import io.xone.chain.onenft.enums.ActivityTargetTypeEnum;
import io.xone.chain.onenft.enums.ActivityTypeEnum;
import io.xone.chain.onenft.event.ActivityEvent;
import io.xone.chain.onenft.mapper.NftLikesMapper;
import io.xone.chain.onenft.service.INftLikesService;
import io.xone.chain.onenft.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.time.LocalDateTime;

/**
 * <p>
 * 喜欢表 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-20
 */
@Service
@RequiredArgsConstructor
public class NftLikesServiceImpl extends ServiceImpl<NftLikesMapper, NftLikes> implements INftLikesService {

    private final ApplicationEventPublisher eventPublisher;
    private final IUsersService usersService;

    @Override
    public boolean toggleLike(Integer userId, String nftObjectId) {
        LambdaQueryWrapper<NftLikes> query = new LambdaQueryWrapper<>();
        query.eq(NftLikes::getUserId, userId)
             .eq(NftLikes::getNftObjectId, nftObjectId);
        
        long count = count(query);
        String actorAddress = getActorAddress(userId);

        if (count > 0) {
            remove(query);
            eventPublisher.publishEvent(ActivityEvent.builder(this)
                    .activityType(ActivityTypeEnum.NFT_UNFAVORITED)
                    .actorAddress(actorAddress)
                    .targetType(ActivityTargetTypeEnum.NFT)
                    .targetId(nftObjectId)
                    .addMetadata("description", "User unliked NFT " + nftObjectId)
                    .build());
            return false;
        } else {
            NftLikes like = new NftLikes();
            like.setUserId(userId);
            like.setNftObjectId(nftObjectId);
            like.setCreatedAt(LocalDateTime.now());
            like.setUpdatedAt(LocalDateTime.now());
            boolean success = save(like);
            
            if (success) {
                eventPublisher.publishEvent(ActivityEvent.builder(this)
                        .activityType(ActivityTypeEnum.NFT_FAVORITED)
                        .actorAddress(actorAddress)
                        .targetType(ActivityTargetTypeEnum.NFT)
                        .targetId(nftObjectId)
                        .addMetadata("description", "User liked NFT " + nftObjectId)
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