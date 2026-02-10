package io.xone.chain.onenft.listener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.json.JSONUtil;
import io.xone.chain.onenft.entity.UserActivities;
import io.xone.chain.onenft.event.ActivityEvent;
import io.xone.chain.onenft.service.IUserActivitiesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivityEventListener {

    private final IUserActivitiesService userActivitiesService;

    @Async
    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void handleActivityEvent(ActivityEvent event) {
        log.info("Received activity event: {}", event);
        try {
            UserActivities activity = new UserActivities();
            activity.setActorAddress(event.getActorAddress());
            
            if (event.getActivityType() != null) {
                activity.setActivityType(event.getActivityType().getValue());
            }
            
            if (event.getTargetType() != null) {
                activity.setTargetType(event.getTargetType().getValue());
            }
            
            activity.setTargetId(event.getTargetId());
            activity.setTxDigest(event.getTxDigest());

            if (event.getMetadata() != null && !event.getMetadata().isEmpty()) {
                activity.setMetadata(JSONUtil.toJsonStr(event.getMetadata()));
            }

            // Use occurredAt (chain time) if available, otherwise use event creation timestamp
            long timestamp = event.getOccurredAt() != null ? event.getOccurredAt() : event.getTimestamp();
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
            
            activity.setCreatedAt(dateTime);
            activity.setUpdatedAt(dateTime);

            userActivitiesService.save(activity);
            log.info("Saved user activity: {}", activity);
        } catch (Exception e) {
            log.error("Failed to save activity event: {}", event, e);
        }
    }
}