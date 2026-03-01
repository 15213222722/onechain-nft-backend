package io.xone.chain.onenft.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;

import io.xone.chain.onenft.common.Result;
import io.xone.chain.onenft.dto.CollectionPageDTO;
import io.xone.chain.onenft.request.CollectionPageQueryRequest;
import io.xone.chain.onenft.request.ListingQueryRequest;
import io.xone.chain.onenft.request.MyListingNftRequest;
import io.xone.chain.onenft.resp.ListingResp;
import io.xone.chain.onenft.service.ICollectionPageService;
import io.xone.chain.onenft.service.IListingsService;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * NFT 挂单表 前端控制器
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
@RestController
@RequestMapping("/listings")
@RequiredArgsConstructor
public class ListingsController {

	private final IListingsService listingsService;
	private final ICollectionPageService collectionPageService;

    @PostMapping("/hot")
    public Result<List<ListingResp>> getHotListings() {
        return Result.success(listingsService.getHotListings());
    }

	@PostMapping("/getListingsByAddress")
	public Result<IPage<ListingResp>> getMyListings(@Validated @RequestBody MyListingNftRequest request) {
		return Result.success(listingsService.getMyListings(request));
	}
	
	@PostMapping("/query")
	public Result<IPage<ListingResp>> query(@Validated @RequestBody ListingQueryRequest request) {
		return Result.success(listingsService.listingsQuery(request));
	}
	
	@PostMapping("/pageCollections")
    public Result<IPage<CollectionPageDTO>> pageCollections(@RequestBody CollectionPageQueryRequest request) {
        return Result.success(collectionPageService.pageCollections(request));
    }
	
	
	
	
}
