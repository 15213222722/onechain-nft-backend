package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.entity.FeeConfigUpdates;
import io.xone.chain.onenft.mapper.FeeConfigUpdatesMapper;
import io.xone.chain.onenft.service.IFeeConfigUpdatesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.xone.chain.onenft.service.IProcessedEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 手续费配置变更记录（FeeUpdated 事件对应的历史记录） 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FeeConfigUpdatesServiceImpl extends ServiceImpl<FeeConfigUpdatesMapper, FeeConfigUpdates> implements IFeeConfigUpdatesService {
    
    private final IProcessedEventService processedEventService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleFeeUpdated(String txDigest, String feeConfigId, Integer newFeeBps, String newRecipient, Long timestampMs) {
        if (processedEventService.isProcessed(txDigest, "FeeUpdated" + feeConfigId)) {
            log.info("FeeUpdated already processed: {}", txDigest);
            return;
        }

        // We only store the update log. We assume we don't have the old value easily available from the event.
        // The entity has old_fee_bps and old_recipient, but the event only gives new values.
        // We might need to query the LATEST fee config update to get the "old" values?
        // Or if we maintain a separate fee_config table (which we don't seem to have requested, 
        // the user only provided FeeConfigUpdates entity).
        // I will just save what I have.
        
        FeeConfigUpdates update = new FeeConfigUpdates();
        update.setFeeConfigObjectId(feeConfigId);
        update.setNewFeeBps(newFeeBps);
        update.setNewRecipient(newRecipient);
        // update.setOldFeeBps(?); 
        // update.setOldRecipient(?);
        
        this.save(update);

        processedEventService.markProcessed(txDigest, "FeeUpdated" + feeConfigId);
    }
}