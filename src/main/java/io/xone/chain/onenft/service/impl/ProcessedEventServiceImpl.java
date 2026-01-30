package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.entity.ProcessedEvent;
import io.xone.chain.onenft.mapper.ProcessedEventMapper;
import io.xone.chain.onenft.service.IProcessedEventService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 已处理事件表 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-24
 */
@Service
public class ProcessedEventServiceImpl extends ServiceImpl<ProcessedEventMapper, ProcessedEvent> implements IProcessedEventService {

    @Override
    public boolean isProcessed(String txHash, String eventType) {
        QueryWrapper<ProcessedEvent> query = new QueryWrapper<>();
        query.eq("tx_hash", txHash);
        query.eq("event_type", eventType);
        return this.count(query) > 0;
    }

    @Override
    public void markProcessed(String txHash, String eventType) {
        ProcessedEvent processedEvent = new ProcessedEvent();
        processedEvent.setTxHash(txHash);
        processedEvent.setEventType(eventType);
        processedEvent.setProcessedAt(LocalDateTime.now());
        this.save(processedEvent);
    }
}