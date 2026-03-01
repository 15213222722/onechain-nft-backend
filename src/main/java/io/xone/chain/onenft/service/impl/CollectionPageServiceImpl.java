package io.xone.chain.onenft.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.xone.chain.onenft.common.service.OneChainService;
import io.xone.chain.onenft.dto.CollectionPageDTO;
import io.xone.chain.onenft.entity.Listings;
import io.xone.chain.onenft.mapper.ListingsMapper;
import io.xone.chain.onenft.request.CollectionPageQueryRequest;
import io.xone.chain.onenft.service.ICollectionPageService;

@Service
public class CollectionPageServiceImpl implements ICollectionPageService {
    @Autowired
    private ListingsMapper listingsMapper;
    @Autowired
    private OneChainService oneChainService;

    @Override
    public IPage<CollectionPageDTO> pageCollections(CollectionPageQueryRequest request) {
        int pageNum = request.getPageNum();
        int pageSize = request.getPageSize();
        int offset = (pageNum - 1) * pageSize;
        // 分页查询所有不同的collection_slug
        List<String> collectionSlugs = listingsMapper.selectDistinctCollectionSlugs(offset, pageSize);
        List<CollectionPageDTO> dtoList = collectionSlugs.stream().map(slug -> {
            CollectionPageDTO dto = new CollectionPageDTO();
            dto.setCollectionSlug(slug);
            // 查询collection名称
            String collection = listingsMapper.selectCollectionNameBySlug(slug);
            dto.setCollection(collection);
            // NFT数量
            Long nftCount = listingsMapper.selectCountByCollectionSlug(slug);
            dto.setNftCount(nftCount);
            // 拥有者数量
            Long holderCount = listingsMapper.selectHolderCountByCollectionSlug(slug);
            dto.setHolderCount(holderCount);
            // 第一个NFT图片（链上查询）
            String firstListingObjectId = listingsMapper.selectFirstListingObjectIdByCollectionSlug(slug);
            String firstNftImage = null;
            if (firstListingObjectId != null) {
                // 调用链上服务获取图片URL
                firstNftImage = oneChainService.queryMyListingNfts(Arrays.asList(firstListingObjectId)).get(0).getImageUrl();
            }
            dto.setFirstNftImage(firstNftImage);
            return dto;
        }).collect(Collectors.toList());
        Page<CollectionPageDTO> resultPage = new Page<>(pageNum, pageSize);
        resultPage.setRecords(dtoList);
        resultPage.setTotal(listingsMapper.countDistinctCollectionSlugs());
        return resultPage;
    }
}