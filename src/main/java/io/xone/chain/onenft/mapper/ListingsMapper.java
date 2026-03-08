package io.xone.chain.onenft.mapper;

import io.xone.chain.onenft.entity.Listings;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
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

    // 分页查询所有不同的collection_slug（直接传offset和limit参数，不用Page对象）
    @Select("SELECT DISTINCT collection_slug FROM listings WHERE collection_slug IS NOT NULL and status = 0 LIMIT #{offset}, #{limit}")
    List<String> selectDistinctCollectionSlugs(@Param("offset") long offset, @Param("limit") long limit);

    // 查询collection名称
    @Select("SELECT collection_name FROM listings WHERE collection_slug = #{slug} and status = 0 LIMIT 1")
    String selectCollectionNameBySlug(@Param("slug") String slug);

    // 查询collection下NFT数量
    @Select("SELECT COUNT(DISTINCT nft_object_id) FROM listings WHERE collection_slug = #{slug} and status = 0")
    Long selectCountByCollectionSlug(@Param("slug") String slug);

    // 查询collection下拥有者数量
    @Select("SELECT COUNT(DISTINCT owner_address) FROM listings WHERE collection_slug = #{slug} and status = 0")
    Long selectHolderCountByCollectionSlug(@Param("slug") String slug);

    // 查询collection下第一个NFT的objectId
    @Select("SELECT listing_object_id FROM listings WHERE collection_slug = #{slug} and status = 0 LIMIT 1")
    String selectFirstListingObjectIdByCollectionSlug(@Param("slug") String slug);

    // 查询所有不同collection_slug数量
    @Select("SELECT COUNT(DISTINCT collection_slug) FROM listings WHERE collection_slug IS NOT NULL and status = 0")
    Long countDistinctCollectionSlugs();

    // 动态条件分页查询所有不同的collection_slug
    /**
     * 动态条件分页查询所有不同的collection_slug
     * @param wrapper Listings表条件
     * @param offset 分页偏移
     * @param limit 分页大小
     * @return collection_slug列表
     */
    List<String> selectDistinctCollectionSlugsByWrapper(@Param("ew") com.baomidou.mybatisplus.core.conditions.Wrapper<Listings> wrapper, @Param("offset") long offset, @Param("limit") long limit);

    /**
     * 动态条件统计所有不同的collection_slug数量
     * @param wrapper Listings表条件
     * @return 数量
     */
    Long countDistinctCollectionSlugsByWrapper(@Param("ew") com.baomidou.mybatisplus.core.conditions.Wrapper<Listings> wrapper);
}