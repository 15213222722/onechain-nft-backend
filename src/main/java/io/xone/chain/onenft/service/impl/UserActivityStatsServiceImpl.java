package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.entity.UserActivityStats;
import io.xone.chain.onenft.mapper.UserActivityStatsMapper;
import io.xone.chain.onenft.service.IUserActivityStatsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户交易活动汇总表（用于用户排行榜、个人交易面板、鲸鱼榜等） 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-10
 */
@Service
public class UserActivityStatsServiceImpl extends ServiceImpl<UserActivityStatsMapper, UserActivityStats> implements IUserActivityStatsService {

}
