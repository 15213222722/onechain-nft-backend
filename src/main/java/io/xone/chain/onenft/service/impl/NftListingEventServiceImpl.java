package io.xone.chain.onenft.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.xone.chain.onenft.entity.NftListingEvent;
import io.xone.chain.onenft.mapper.NftListingEventMapper;
import io.xone.chain.onenft.service.INftListingEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 上架事件表 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-26
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NftListingEventServiceImpl extends ServiceImpl<NftListingEventMapper, NftListingEvent> implements INftListingEventService {


}