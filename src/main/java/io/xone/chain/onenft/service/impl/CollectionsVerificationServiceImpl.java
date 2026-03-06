package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.entity.CollectionsVerification;
import io.xone.chain.onenft.mapper.CollectionsVerificationMapper;
import io.xone.chain.onenft.service.ICollectionsVerificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 集合验证状态表，记录每个集合的验证结果与历史关联申请 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-03-05
 */
@Service
public class CollectionsVerificationServiceImpl extends ServiceImpl<CollectionsVerificationMapper, CollectionsVerification> implements ICollectionsVerificationService {
    @Override
    public CollectionsVerification getVerificationByCollectionType(String collectionType) {
        return this.lambdaQuery().eq(CollectionsVerification::getCollectionType, collectionType).one();
    }
    @Override
    public Map<String, CollectionsVerification> getVerificationMapByCollectionTypes(List<String> collectionTypes) {
        if (collectionTypes == null || collectionTypes.isEmpty()) return java.util.Collections.emptyMap();
        List<CollectionsVerification> list = this.lambdaQuery().in(CollectionsVerification::getCollectionType, collectionTypes).list();
        return list.stream().collect(Collectors.toMap(CollectionsVerification::getCollectionType, v -> v));
    }
}