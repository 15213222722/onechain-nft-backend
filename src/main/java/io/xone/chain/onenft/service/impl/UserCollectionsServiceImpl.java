package io.xone.chain.onenft.service.impl;

import io.xone.chain.onenft.common.entity.ListingNft;
import io.xone.chain.onenft.common.service.OneChainService;
import io.xone.chain.onenft.entity.Listings;
import io.xone.chain.onenft.entity.UserCollections;
import io.xone.chain.onenft.mapper.UserCollectionsMapper;
import io.xone.chain.onenft.request.UserCollectionQueryRequest;
import io.xone.chain.onenft.resp.UserCollectionResp;
import io.xone.chain.onenft.service.IListingsService;
import io.xone.chain.onenft.service.IUserCollectionsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 收藏表 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-19
 */
@Service
@RequiredArgsConstructor
public class UserCollectionsServiceImpl extends ServiceImpl<UserCollectionsMapper, UserCollections> implements IUserCollectionsService {

    private final IListingsService listingsService;
    private final OneChainService oneChainService;

    @Override
    public Page<UserCollectionResp> getUserCollections(UserCollectionQueryRequest request) {
        Page<UserCollections> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<UserCollections> query = new LambdaQueryWrapper<>();
        if (request.getWalletAddress() != null) {
            query.eq(UserCollections::getWalletAddress, request.getWalletAddress());
        }
        query.orderByDesc(UserCollections::getCreatedAt);

        Page<UserCollections> pResult = page(page, query);
        Page<UserCollectionResp> respPage = new Page<>();
        respPage.setCurrent(pResult.getCurrent());
        respPage.setSize(pResult.getSize());
        respPage.setTotal(pResult.getTotal());

        if (pResult.getRecords().isEmpty()) {
            return respPage;
        }

        List<String> nftObjectIds = pResult.getRecords().stream()
                .map(UserCollections::getNftObjectId)
                .collect(Collectors.toList());

        // Check Listings
        LambdaQueryWrapper<Listings> listingQuery = new LambdaQueryWrapper<>();
        listingQuery.in(Listings::getNftObjectId, nftObjectIds);
        listingQuery.eq(Listings::getStatus, 0); 
          
        List<Listings> listings = listingsService.list(listingQuery);
        Map<String, Listings> listingMap = listings.stream()
                .collect(Collectors.toMap(Listings::getNftObjectId, l -> l, (k1, k2) -> k1)); // handle duplicates safely

        List<String> listingObjectIdsToQuery = new ArrayList<>();
        List<String> nftObjectIdsToQuery = new ArrayList<>();

        for (String nftId : nftObjectIds) {
            if (listingMap.containsKey(nftId)) {
                listingObjectIdsToQuery.add(listingMap.get(nftId).getListingObjectId());
            } else {
                nftObjectIdsToQuery.add(nftId);
            }
        }

        Map<String, ListingNft> nftDetailsMap = new java.util.HashMap<>();

        if (!listingObjectIdsToQuery.isEmpty()) {
            List<ListingNft> listedNfts = oneChainService.queryMyListingNfts(listingObjectIdsToQuery);
            if (listedNfts != null) {
                for (ListingNft nft : listedNfts) {
                    nftDetailsMap.put(nft.getObjectId(), nft); 
                }
            }
        }

        if (!nftObjectIdsToQuery.isEmpty()) {
            List<ListingNft> directNfts = oneChainService.getListedNfts(nftObjectIdsToQuery);
            if (directNfts != null) {
                for (ListingNft nft : directNfts) {
                    nftDetailsMap.put(nft.getObjectId(), nft);
                }
            }
        }

        List<UserCollectionResp> records = new ArrayList<>();
        for (UserCollections uc : pResult.getRecords()) {
            UserCollectionResp resp = new UserCollectionResp();
            String nftId = uc.getNftObjectId();
            ListingNft details = nftDetailsMap.get(nftId);
            
            if (details != null) {
                BeanUtils.copyProperties(details, resp);
            } else {
                resp.setObjectId(nftId); // At least set the ID if details fetch failed
            }
            
            resp.setIsListed(listingMap.containsKey(nftId));
            records.add(resp);
        }
        
        respPage.setRecords(records);
        return respPage;
    }

    @Override
    public boolean addCollection(String walletAddress, String nftObjectId) {
        LambdaQueryWrapper<UserCollections> query = new LambdaQueryWrapper<>();
        query.eq(UserCollections::getWalletAddress, walletAddress);
        query.eq(UserCollections::getNftObjectId, nftObjectId);
        
        long count = count(query);
        if (count > 0) {
            throw new io.xone.chain.onenft.common.ApiException("重复收藏");
        }
        
        UserCollections collection = new UserCollections();
        collection.setWalletAddress(walletAddress);
        collection.setNftObjectId(nftObjectId);
        collection.setCreatedAt(LocalDateTime.now());
        collection.setUpdatedAt(LocalDateTime.now());
        return save(collection);
    }

    @Override
    public boolean removeCollection(String walletAddress, String nftObjectId) {
        LambdaQueryWrapper<UserCollections> query = new LambdaQueryWrapper<>();
        query.eq(UserCollections::getWalletAddress, walletAddress);
        query.eq(UserCollections::getNftObjectId, nftObjectId);
        return remove(query);
    }

    @Override
    public boolean isCollected(String walletAddress, String nftObjectId) {
        LambdaQueryWrapper<UserCollections> query = new LambdaQueryWrapper<>();
        query.eq(UserCollections::getWalletAddress, walletAddress);
        query.eq(UserCollections::getNftObjectId, nftObjectId);
        return count(query) > 0;
    }
}