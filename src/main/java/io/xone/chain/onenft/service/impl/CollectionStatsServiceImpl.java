package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.entity.CollectionStats;
import io.xone.chain.onenft.mapper.CollectionStatsMapper;
import io.xone.chain.onenft.service.ICollectionStatsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * NFT 集合统计汇总表（用于集合排行榜、地板价展示、交易量趋势等） 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
@Service
public class CollectionStatsServiceImpl extends ServiceImpl<CollectionStatsMapper, CollectionStats> implements ICollectionStatsService {

}
