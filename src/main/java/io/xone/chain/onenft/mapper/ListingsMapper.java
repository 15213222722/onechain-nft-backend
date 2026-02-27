package io.xone.chain.onenft.mapper;

import io.xone.chain.onenft.entity.Listings;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * <p>
 * NFT 挂单表 Mapper 接口
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
public interface ListingsMapper extends BaseMapper<Listings> {

    @Select("SELECT l.*, COUNT(t.id) as trade_count " +
            "FROM listings l " +
            "LEFT JOIN trades t ON l.nft_object_id = t.nft_object_id_out " +
            "WHERE l.status = 0 " +
            "GROUP BY l.id " +
            "ORDER BY trade_count DESC " +
            "LIMIT 4")
    List<Listings> getHotListings();
}