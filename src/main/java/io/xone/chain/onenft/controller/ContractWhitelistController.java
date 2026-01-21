package io.xone.chain.onenft.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.xone.chain.onenft.common.Result;
import io.xone.chain.onenft.entity.ContractWhitelist;
import io.xone.chain.onenft.listener.AnimeKioskEventListener;
import io.xone.chain.onenft.service.IContractWhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 合约白名单表 前端控制器
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-21
 */
@Api(tags = "Contract Whitelist Management")
@RestController
@RequestMapping("/contractWhitelist")
public class ContractWhitelistController {

	@Autowired
	private IContractWhitelistService contractWhitelistService;

	@Autowired
	private AnimeKioskEventListener animeKioskEventListener;

	@ApiOperation("Add Contract")
	@PostMapping("/add")
	public Result<Boolean> add(@RequestBody ContractWhitelist contractWhitelist) {
		boolean saved = contractWhitelistService.save(contractWhitelist);
		if (saved) {
			animeKioskEventListener.refreshSubscriptions();
		}
		return Result.success(saved);
	}

	@ApiOperation("Delete Contract")
	@DeleteMapping("/{packageId}")
	public Result<Boolean> delete(@PathVariable String packageId) {
		boolean removed = contractWhitelistService
				.remove(new LambdaQueryWrapper<ContractWhitelist>().eq(ContractWhitelist::getPackageId, packageId));
		if (removed) {
			animeKioskEventListener.refreshSubscriptions();
		}
		return Result.success(removed);
	}

	@ApiOperation("Enable Contract")
	@PostMapping("/{packageId}/enable")
	public Result<Boolean> enable(@PathVariable String packageId) {
		boolean updated = contractWhitelistService.update(new LambdaUpdateWrapper<ContractWhitelist>()
				.eq(ContractWhitelist::getPackageId, packageId).set(ContractWhitelist::getIsActive, true));
		if (updated) {
			animeKioskEventListener.refreshSubscriptions();
		}
		return Result.success(updated);
	}

	@ApiOperation("Disable Contract")
	@PostMapping("/{packageId}/disable")
	public Result<Boolean> disable(@PathVariable String packageId) {
		boolean updated = contractWhitelistService.update(new LambdaUpdateWrapper<ContractWhitelist>()
				.eq(ContractWhitelist::getPackageId, packageId).set(ContractWhitelist::getIsActive, false));
		if (updated) {
			animeKioskEventListener.refreshSubscriptions();
		}
		return Result.success(updated);
	}

	@ApiOperation("Manual Refresh Subscriptions")
	@PostMapping("/refresh")
	public Result<Boolean> refresh() {
		animeKioskEventListener.refreshSubscriptions();
		return Result.success(true);
	}
}