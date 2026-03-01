package io.xone.chain.onenft.service;

import io.xone.chain.onenft.dto.CollectionSimpleStatsDTO;

public interface ICollectionSimpleStatsService {
    /**
     * 查询指定集合的简单统计数据
     * @param collectionSlug 集合唯一标识
     * @return CollectionSimpleStatsDTO
     */
    CollectionSimpleStatsDTO getCollectionSimpleStats(String collectionSlug);
}
