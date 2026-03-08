package io.xone.chain.onenft.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.xone.chain.onenft.common.service.OneChainService;
import io.xone.chain.onenft.dto.CollectionPageDTO;
import io.xone.chain.onenft.entity.CollectionsVerification;
import io.xone.chain.onenft.entity.Listings;
import io.xone.chain.onenft.enums.CategoryEnum;
import io.xone.chain.onenft.mapper.ListingsMapper;
import io.xone.chain.onenft.request.CollectionPageQueryRequest;
import io.xone.chain.onenft.service.ICollectionPageService;
import io.xone.chain.onenft.service.ICollectionsVerificationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CollectionPageServiceImpl implements ICollectionPageService {
	@Autowired
	private ListingsMapper listingsMapper;
	@Autowired
	private OneChainService oneChainService;
	@Autowired
	private ICollectionsVerificationService collectionsVerificationService;

	@Override
	public IPage<CollectionPageDTO> pageCollections(CollectionPageQueryRequest request) {
		long pageNum = request.getCurrent();
		long pageSize = request.getSize();
		long offset = (pageNum - 1) * pageSize;
		// 查询所有已验证集合，构建Map
		List<CollectionsVerification> allVerified = collectionsVerificationService.list();
		Map<String, CollectionsVerification> verifiedMap = allVerified.stream()
				.collect(Collectors.toMap(CollectionsVerification::getCollectionType, v -> v));
		// Listings表动态条件
		QueryWrapper<Listings> wrapper = new QueryWrapper<>();
		wrapper.eq("status", 0);
		// 只加一次collectionSlug条件
		if (request.getCollectionSlug() != null && !request.getCollectionSlug().isEmpty()) {
			wrapper.eq("collection_slug", request.getCollectionSlug());
		}
		if (request.getCollectionName() != null && !request.getCollectionName().isEmpty()) {
			// collection_name模糊查Listings和验证表，避免条件重复
			List<String> verifiedSlugs = allVerified.stream().filter(
					v -> v.getCollectionName() != null && v.getCollectionName().contains(request.getCollectionName()))
					.map(CollectionsVerification::getCollectionType).collect(Collectors.toList());
			if (!verifiedSlugs.isEmpty()) {
				wrapper.and(w -> w.like("collection_name", request.getCollectionName()).or().in("collection_slug",
						verifiedSlugs));
			} else {
				wrapper.like("collection_name", request.getCollectionName());
			}
		}
		if (request.getWalletAddress() != null && !request.getWalletAddress().isEmpty()) {
			wrapper.eq("owner_address", request.getWalletAddress());
		}
		if (request.getNftObjectId() != null && !request.getNftObjectId().isEmpty()) {
			wrapper.eq("nft_object_id", request.getNftObjectId());
		}
		// 只用Listings表字段查collection_slug
		List<String> allSlugs = listingsMapper.selectDistinctCollectionSlugsByWrapper(wrapper, 0, Integer.MAX_VALUE);
		// category和is_original用内存过滤（非Listings表字段）
		List<String> filteredSlugs = allSlugs;
		if (request.getCategoryEnum() != null) {
			filteredSlugs = filteredSlugs.stream().filter(slug -> {
				CollectionsVerification v = verifiedMap.get(slug);
				return v != null && request.getCategoryEnum().name().equals(v.getCategory());
			}).collect(Collectors.toList());
		}
		if (request.getIsOriginal() != null && !request.getIsOriginal().isEmpty()) {
			boolean wantOriginal = Boolean.parseBoolean(request.getIsOriginal());
			filteredSlugs = filteredSlugs.stream().filter(slug -> {
				CollectionsVerification v = verifiedMap.get(slug);
				return v != null && v.getIsOriginal() != null && v.getIsOriginal() == wantOriginal;
			}).collect(Collectors.toList());
		}
		// isVerified条件内存过滤
		if (request.getIsVerified() != null && !request.getIsVerified().isEmpty()) {
			boolean wantVerified = Boolean.parseBoolean(request.getIsVerified());
			filteredSlugs = filteredSlugs.stream().filter(slug -> {
				CollectionsVerification v = verifiedMap.get(slug);
				return wantVerified ? (v != null && Boolean.TRUE.equals(v.getIsVerified()))
						: (v == null || !Boolean.TRUE.equals(v.getIsVerified()));
			}).collect(Collectors.toList());
		}
		// 分页
		List<String> pageSlugs = filteredSlugs.stream().skip(offset).limit(pageSize).collect(Collectors.toList());
		// 批量查集合验证信息
		Map<String, CollectionsVerification> verificationMap = verifiedMap;
		List<CollectionPageDTO> dtoList = pageSlugs.stream().map(slug -> {
			CollectionPageDTO dto = new CollectionPageDTO();
			dto.setCollectionSlug(slug);
			CollectionsVerification verification = verificationMap.get(slug);
			if (verification != null) {
				dto.setCategory(verification.getCategory());
				dto.setDescription(verification.getDescription());
				dto.setLogoUrl(verification.getLogoUrl());
				dto.setWebsiteUrl(verification.getWebsiteUrl());
				dto.setSocialLinks(verification.getSocialLinks());
				dto.setIsOriginal(verification.getIsOriginal());
				dto.setCollectionName(verification.getCollectionName());
				dto.setVerified(Boolean.TRUE.equals(verification.getIsVerified()));
			} else {
				String collectionName = listingsMapper.selectCollectionNameBySlug(slug);
				dto.setCollectionName(collectionName);
				dto.setVerified(false);
			}
			Long nftCount = listingsMapper.selectCountByCollectionSlug(slug);
			dto.setNftCount(nftCount);
			Long holderCount = listingsMapper.selectHolderCountByCollectionSlug(slug);
			dto.setHolderCount(holderCount);
			if (dto.getLogoUrl() == null) {
				String firstListingObjectId = listingsMapper.selectFirstListingObjectIdByCollectionSlug(slug);
				String firstNftImage = null;
				if (firstListingObjectId != null) {
					firstNftImage = oneChainService.queryMyListingNfts(Arrays.asList(firstListingObjectId)).get(0)
							.getImageUrl();
				}
				dto.setLogoUrl(firstNftImage);
			}
			return dto;
		}).collect(Collectors.toList());
		Page<CollectionPageDTO> resultPage = new Page<>(pageNum, pageSize);
		resultPage.setRecords(dtoList);
		resultPage.setTotal(filteredSlugs.size());
		return resultPage;
	}
}