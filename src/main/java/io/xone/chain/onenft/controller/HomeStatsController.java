package io.xone.chain.onenft.controller;

import io.xone.chain.onenft.common.entity.Result;
import io.xone.chain.onenft.dto.HomeStatsDTO;
import io.xone.chain.onenft.service.IHomeStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
public class HomeStatsController {
    @Autowired
    private IHomeStatsService homeStatsService;

    @PostMapping("/stats")
    public Result<HomeStatsDTO> getHomeStats() {
        return Result.success(homeStatsService.getHomeStats());
    }
}
