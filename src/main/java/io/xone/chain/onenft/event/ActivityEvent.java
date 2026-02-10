package io.xone.chain.onenft.event;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationEvent;

import io.xone.chain.onenft.enums.ActivityTargetTypeEnum;
import io.xone.chain.onenft.enums.ActivityTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ActivityEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private String actorAddress;
    private ActivityTypeEnum activityType;
    private ActivityTargetTypeEnum targetType;
    private String targetId;
    private String txDigest;
    private Map<String, Object> metadata = new HashMap<>();
    private Long occurredAt;

    public ActivityEvent(Object source) {
        super(source);
    }
    
    public static Builder builder(Object source) {
        return new Builder(source);
    }

    public static class Builder {
        private ActivityEvent event;

        public Builder(Object source) {
            event = new ActivityEvent(source);
        }

        public Builder actorAddress(String actorAddress) {
            event.actorAddress = actorAddress;
            return this;
        }

        public Builder activityType(ActivityTypeEnum activityType) {
            event.activityType = activityType;
            return this;
        }

        public Builder targetType(ActivityTargetTypeEnum targetType) {
            event.targetType = targetType;
            return this;
        }

        public Builder targetId(String targetId) {
            event.targetId = targetId;
            return this;
        }
        
        public Builder txDigest(String txDigest) {
            event.txDigest = txDigest;
            return this;
        }

        public Builder addMetadata(String key, Object value) {
            event.metadata.put(key, value);
            return this;
        }
        
        public Builder metadata(Map<String, Object> metadata) {
            if (metadata != null) {
                event.metadata.putAll(metadata);
            }
            return this;
        }

        public Builder occurredAt(Long occurredAt) {
            event.occurredAt = occurredAt;
            return this;
        }

        public ActivityEvent build() {
            return event;
        }
    }
}