package io.xone.chain.onenft.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.xone.chain.onenft.common.entity.Result;
import io.xone.chain.onenft.request.TradeQueryRequest;
import io.xone.chain.onenft.resp.TradeResp;
import io.xone.chain.onenft.service.ITradesService;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * NFT 成交记录表 前端控制器
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
@RestController
@RequestMapping("/trades")
@RequiredArgsConstructor
public class TradesController {

    private final ITradesService tradesService;

    @PostMapping("/list")
    public Result<Page<TradeResp>> list(@RequestBody TradeQueryRequest request) {
        return Result.success(tradesService.queryTrades(request));
    }
}