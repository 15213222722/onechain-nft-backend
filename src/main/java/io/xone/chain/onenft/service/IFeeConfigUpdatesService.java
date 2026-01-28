package io.xone.chain.onenft.service;

import io.xone.chain.onenft.entity.FeeConfigUpdates;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 手续费配置变更记录（FeeUpdated 事件对应的历史记录） 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
public interface IFeeConfigUpdatesService extends IService<FeeConfigUpdates> {

    void handleFeeUpdated(String txDigest, String feeConfigId, Integer newFeeBps, String newRecipient, Long timestampMs);

}