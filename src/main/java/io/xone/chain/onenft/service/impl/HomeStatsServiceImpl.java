package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.dto.HomeStatsDTO;
import io.xone.chain.onenft.mapper.UsersMapper;
import io.xone.chain.onenft.mapper.ListingsMapper;
import io.xone.chain.onenft.mapper.TradesMapper;
import io.xone.chain.onenft.service.IHomeStatsService;
import io.xone.chain.onenft.entity.Listings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@Service
public class HomeStatsServiceImpl implements IHomeStatsService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private ListingsMapper listingsMapper;
    @Autowired
    private TradesMapper tradesMapper;

    @Override
    public HomeStatsDTO getHomeStats() {
        HomeStatsDTO dto = new HomeStatsDTO();
        // 用户数
        dto.setUserCount(usersMapper.selectCount(null));
        // 在售NFT数量（status=0）
        QueryWrapper<Listings> onSaleWrapper = new QueryWrapper<>();
        onSaleWrapper.eq("status", 0);
        dto.setNftOnSaleCount(listingsMapper.selectCount(onSaleWrapper));
        // 总成交单数（trades表记录数）
        dto.setTotalTradeCount(tradesMapper.selectCount(null));
        return dto;
    }
}