package io.xone.chain.onenft.service;

import io.xone.chain.onenft.entity.ProcessedEvent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 已处理事件表 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-24
 */
public interface IProcessedEventService extends IService<ProcessedEvent> {

    boolean isProcessed(String txHash, String eventType);

    void markProcessed(String txHash, String eventType);

}