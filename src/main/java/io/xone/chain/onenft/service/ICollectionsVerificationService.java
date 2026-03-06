package io.xone.chain.onenft.service;

import io.xone.chain.onenft.entity.CollectionsVerification;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 集合验证状态表，记录每个集合的验证结果与历史关联申请 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-03-05
 */
public interface ICollectionsVerificationService extends IService<CollectionsVerification> {
    /**
     * 查询单个集合的验证信息
     */
    CollectionsVerification getVerificationByCollectionType(String collectionType);

    /**
     * 批量查询集合的验证信息
     */
    Map<String, CollectionsVerification> getVerificationMapByCollectionTypes(List<String> collectionTypes);
}