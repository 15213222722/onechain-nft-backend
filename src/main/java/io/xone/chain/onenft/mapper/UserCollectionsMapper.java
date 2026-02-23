package io.xone.chain.onenft.mapper;

import io.xone.chain.onenft.entity.UserCollections;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import io.xone.chain.onenft.resp.UserCollectionRankResp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 收藏表 Mapper 接口
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-19
 */
public interface UserCollectionsMapper extends BaseMapper<UserCollections> {

    @Select("<script>" +
            "SELECT uc.wallet_address as walletAddress, u.name, u.avatar_url as avatarUrl, COUNT(uc.id) as collectionCount " +
            "FROM user_collections uc " +
            "LEFT JOIN users u ON uc.wallet_address = u.wallet_address " +
            "<where>" +
            "  <if test='startTime != null'>" +
            "    uc.created_at &gt;= #{startTime} " +
            "  </if>" +
            "</where>" +
            "GROUP BY uc.wallet_address, u.name, u.avatar_url " +
            "ORDER BY collectionCount DESC " +
            "LIMIT 100" +
            "</script>")
    List<UserCollectionRankResp> getCollectionRanking(@Param("startTime") LocalDateTime startTime);
}