package io.xone.chain.onenft.controller;

import io.xone.chain.onenft.dto.CollectionSimpleStatsDTO;
import io.xone.chain.onenft.service.ICollectionSimpleStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.xone.chain.onenft.common.entity.Result;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p>
 * NFT 集合统计汇总表（用于集合排行榜、地板价展示、交易量趋势等） 前端控制器
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
@RestController
@RequestMapping("/collectionStats")
public class CollectionStatsController {
}

@RestController
@RequestMapping("/api/collection")
class CollectionSimpleStatsController {
	@Autowired
	private ICollectionSimpleStatsService collectionSimpleStatsService;

	@PostMapping("/stats")
	public Result<CollectionSimpleStatsDTO> getCollectionStats(@RequestBody CollectionStatsRequest req) {
		return Result.success(collectionSimpleStatsService.getCollectionSimpleStats(req.getCollectionSlug()));
	}

	public static class CollectionStatsRequest {
		private String collectionSlug;

		public String getCollectionSlug() {
			return collectionSlug;
		}

		public void setCollectionSlug(String collectionSlug) {
			this.collectionSlug = collectionSlug;
		}
	}
}