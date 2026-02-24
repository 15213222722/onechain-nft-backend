package io.xone.chain.onenft.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.collection.CollUtil;
import io.xone.chain.onenft.common.entity.ListingNft;
import io.xone.chain.onenft.common.service.OneChainService;
import io.xone.chain.onenft.entity.Listings;
import io.xone.chain.onenft.entity.UserCollections;
import io.xone.chain.onenft.enums.NotificationType;
import io.xone.chain.onenft.mapper.UserCollectionsMapper;
import io.xone.chain.onenft.request.UserCollectionQueryRequest;
import io.xone.chain.onenft.resp.UserCollectionResp;
import io.xone.chain.onenft.service.IListingsService;
import io.xone.chain.onenft.service.INotificationsService;
import io.xone.chain.onenft.service.IUserCollectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 收藏表 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-02-19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCollectionsServiceImpl extends ServiceImpl<UserCollectionsMapper, UserCollections> implements IUserCollectionsService {

    private final IListingsService listingsService;
    private final OneChainService oneChainService;
    private final INotificationsService notificationsService;

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
        boolean success = save(collection);
        if (success) {
        	LambdaQueryWrapper<Listings> listingQuery = new LambdaQueryWrapper<>();
            listingQuery.eq(Listings::getNftObjectId, nftObjectId);
            listingQuery.eq(Listings::getStatus, 0); 
            List<Listings> listings = listingsService.list(listingQuery);
        	String ownerAddress = null;
        	String name = null;
        	if(CollUtil.isNotEmpty(listings)) {
        		ownerAddress = listings.get(0).getOwnerAddress();
        		name = oneChainService.queryMyListingNfts(Arrays.asList(listings.get(0).getNftObjectId())).get(0).getName();
        	}
             ListingNft nft = oneChainService.queryNftDetail(nftObjectId);
             if(nft != null) {
            	 ownerAddress = nft.getOwnerAddress();
            	 name = nft.getName();
             }
             log.info("Fetched NFT details for {}, owner: {}", nftObjectId, nft != null ? nft.getOwnerAddress() : "null");
             if (ownerAddress != null && !ownerAddress.equals(walletAddress)) {
                 String nftName = (name != null) ? name : "Unknown";
                 String shortId = nftObjectId.substring(nftObjectId.length() - 4);
                 String arg0 = nftName + " #" + shortId;
                 String arg1 = walletAddress.substring(0, 6) + "..." + walletAddress.substring(walletAddress.length() - 4);
                 notificationsService.createNotification(NotificationType.NFT_COLLECTED, 
                         walletAddress, ownerAddress, "NFT", nftObjectId, arg0, arg1);
             }
        }
        return success;
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

    @Override
    public java.util.List<io.xone.chain.onenft.resp.UserCollectionRankResp> getCollectionRanking(io.xone.chain.onenft.request.UserCollectionRankRequest request) {
        LocalDateTime startTime = null;
        if (request.getTimeRange() != null) {
            switch (request.getTimeRange().toUpperCase()) {
                case "24H":
                    startTime = LocalDateTime.now().minusHours(24);
                    break;
                case "7D":
                    startTime = LocalDateTime.now().minusDays(7);
                    break;
                case "30D":
                    startTime = LocalDateTime.now().minusDays(30);
                    break;
                case "ALL":
                default:
                    startTime = null;
                    break;
            }
        }
        return baseMapper.getCollectionRanking(startTime);
    }
}
