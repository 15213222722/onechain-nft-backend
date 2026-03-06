package io.xone.chain.onenft.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.xone.chain.onenft.common.service.OneChainService;
import io.xone.chain.onenft.dto.CollectionPageDTO;
import io.xone.chain.onenft.entity.CollectionsVerification;
import io.xone.chain.onenft.mapper.ListingsMapper;
import io.xone.chain.onenft.request.CollectionPageQueryRequest;
import io.xone.chain.onenft.service.ICollectionPageService;
import io.xone.chain.onenft.service.ICollectionsVerificationService;

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
        int pageNum = request.getPageNum();
        int pageSize = request.getPageSize();
        int offset = (pageNum - 1) * pageSize;
        // 分页查询所有不同的collection_slug
        List<String> collectionSlugs = listingsMapper.selectDistinctCollectionSlugs(offset, pageSize);
        // 批量查集合验证信息
        Map<String, CollectionsVerification> verificationMap = collectionsVerificationService.getVerificationMapByCollectionTypes(collectionSlugs);
        List<CollectionPageDTO> dtoList = collectionSlugs.stream().map(slug -> {
            CollectionPageDTO dto = new CollectionPageDTO();
            dto.setCollectionSlug(slug);
            // 优先用已验证集合名称
            CollectionsVerification verification = verificationMap.get(slug);
            if(verification != null) {
            	dto.setCategory(verification.getCategory());
				dto.setDescription(verification.getDescription());
				dto.setLogoUrl(verification.getLogoUrl());
				dto.setWebsiteUrl(verification.getWebsiteUrl());
				dto.setSocialLinks(verification.getSocialLinks());
				dto.setIsOriginal(verification.getIsOriginal());
				dto.setCollectionName(verification.getCollectionName()); // 如有更优字段如displayName可替换
				if (Boolean.TRUE.equals(verification.getIsVerified())) {	
					dto.setVerified(true);
				} else {
					dto.setVerified(false);
				}
            } else {
				// 没有验证信息时，查询collection名称
				String collectionName = listingsMapper.selectCollectionNameBySlug(slug);
				dto.setCollectionName(collectionName);
			}
            // NFT数量
            Long nftCount = listingsMapper.selectCountByCollectionSlug(slug);
            dto.setNftCount(nftCount);
            // 拥有者数量
            Long holderCount = listingsMapper.selectHolderCountByCollectionSlug(slug);
            dto.setHolderCount(holderCount);
            if(dto.getLogoUrl() == null) {
            	// 第一个NFT图片（链上查询）
            	String firstListingObjectId = listingsMapper.selectFirstListingObjectIdByCollectionSlug(slug);
            	String firstNftImage = null;
            	if (firstListingObjectId != null) {
            		// 调用链上服务获取图片URL
            		firstNftImage = oneChainService.queryMyListingNfts(Arrays.asList(firstListingObjectId)).get(0).getImageUrl();
            	}
				dto.setLogoUrl(firstNftImage); // 没有验证信息时用第一个NFT图片作为logo
            }
            return dto;
        }).collect(Collectors.toList());
        Page<CollectionPageDTO> resultPage = new Page<>(pageNum, pageSize);
        resultPage.setRecords(dtoList);
        resultPage.setTotal(listingsMapper.countDistinctCollectionSlugs());
        return resultPage;
    }
}