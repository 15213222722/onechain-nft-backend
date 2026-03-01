package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.dto.CollectionSimpleStatsDTO;
import io.xone.chain.onenft.entity.Listings;
import io.xone.chain.onenft.mapper.ListingsMapper;
import io.xone.chain.onenft.mapper.TradesMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.xone.chain.onenft.service.ICollectionSimpleStatsService;

@Service
public class CollectionSimpleStatsServiceImpl implements ICollectionSimpleStatsService {
    @Autowired
    private ListingsMapper listingsMapper;
    @Autowired
    private TradesMapper tradesMapper;

    @Override
    public CollectionSimpleStatsDTO getCollectionSimpleStats(String collectionSlug) {
        CollectionSimpleStatsDTO dto = new CollectionSimpleStatsDTO();
        dto.setCollectionSlug(collectionSlug);
        // 统计该集合的NFT数量（唯一nft_object_id数量）
        Long nftCount = listingsMapper.selectObjs(new QueryWrapper<Listings>()
                .eq("collection_slug", collectionSlug)
                .select("COUNT(DISTINCT nft_object_id)")).stream()
                .filter(x -> x != null)
                .mapToLong(x -> x instanceof Number ? ((Number)x).longValue() : 0L)
                .sum();
        dto.setNftCount(nftCount);
        // 统计该集合的交易笔数
        Long tradeCount = tradesMapper.selectCount(new QueryWrapper<io.xone.chain.onenft.entity.Trades>()
                .eq("collection_slug", collectionSlug)).longValue();
        dto.setTradeCount(tradeCount);
        // 统计持有者数量（唯一 owner_address 数量）
        Long holderCount = listingsMapper.selectObjs(new QueryWrapper<Listings>()
                .eq("collection_slug", collectionSlug)
                .select("COUNT(DISTINCT owner_address)")).stream()
                .filter(x -> x != null)
                .mapToLong(x -> x instanceof Number ? ((Number)x).longValue() : 0L)
                .sum();
        dto.setHolderCount(holderCount);
        return dto;
    }
}