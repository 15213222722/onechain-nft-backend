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

        FeeConfigUpdates update = new FeeConfigUpdates();
        update.setFeeConfigObjectId(feeConfigId);
        update.setNewFeeBps(newFeeBps);
        update.setNewRecipient(newRecipient);
         update.setOldFeeBps(100); 
         update.setOldRecipient("@0x1");
        
        this.save(update);

        processedEventService.markProcessed(txDigest, "FeeUpdated" + feeConfigId);
    }
}