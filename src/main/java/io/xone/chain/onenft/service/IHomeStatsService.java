package io.xone.chain.onenft.service;

import io.xone.chain.onenft.dto.HomeStatsDTO;

public interface IHomeStatsService {
    /**
     * 获取首页统计数据
     * @return 首页统计数据DTO
     */
    HomeStatsDTO getHomeStats();
}