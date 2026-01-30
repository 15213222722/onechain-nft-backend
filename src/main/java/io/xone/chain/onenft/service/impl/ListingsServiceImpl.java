package io.xone.chain.onenft.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.onechain.OneChain;
import io.xone.chain.onenft.common.entity.ListingNft;
import io.xone.chain.onenft.common.service.OneChainService;
import io.xone.chain.onenft.entity.Listings;
import io.xone.chain.onenft.entity.Users;
import io.xone.chain.onenft.mapper.ListingsMapper;
import io.xone.chain.onenft.mapper.UsersMapper;
import io.xone.chain.onenft.request.MyListingNftRequest;
import io.xone.chain.onenft.resp.ListingResp;
import io.xone.chain.onenft.service.IListingsService;
import io.xone.chain.onenft.service.INftsService;
import io.xone.chain.onenft.service.IProcessedEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * NFT 挂单表 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ListingsServiceImpl extends ServiceImpl<ListingsMapper, Listings> implements IListingsService {

	private final IProcessedEventService processedEventService;
	private final INftsService nftsService;
	private final UsersMapper usersMapper;

	private final OneChain oneChain;

	private final OneChainService oneChainService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handleListingCreated(String txDigest, String listingObjectId, String owner, String nftObjectId,
			String nftType, Integer listingType, Long price, String coinType, String expectedNftType, Long timestampMs) {
		if (processedEventService.isProcessed(txDigest, "ListingCreated" + listingObjectId)) {
			log.info("ListingCreated already processed: {}", txDigest);
			return;
		}

		Listings listing = new Listings();
		listing.setListingObjectId(listingObjectId);
		listing.setOwnerAddress(owner);
		listing.setNftObjectId(nftObjectId);
		listing.setListingType(listingType);
		listing.setStatus(0); // Active
		listing.setCreateTxDigest(txDigest);
		listing.setNftType(nftType);
		listing.setPrice(price);
		listing.setCoinType(coinType);
		listing.setExpectedNftType(expectedNftType);
		if (timestampMs != null) {
			listing.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMs), ZoneId.systemDefault()));
		}
		this.save(listing);
		processedEventService.markProcessed(txDigest, "ListingCreated" + listingObjectId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handleListingCancelled(String txDigest, String listingObjectId, Long timestampMs) {
		if (processedEventService.isProcessed(txDigest, "ListingCancelled" + listingObjectId)) {
			log.info("ListingCancelled already processed: {}", txDigest);
			return;
		}

		QueryWrapper<Listings> query = new QueryWrapper<>();
		query.eq("listing_object_id", listingObjectId);
		Listings listing = this.getOne(query);

		if (listing != null) {
			listing.setStatus(2); // Cancelled
			listing.setCancelTxDigest(txDigest);
			listing.setCancelTimestampMs(timestampMs);
			this.updateById(listing);

			// Update NFT status
			nftsService.syncNftFromChain(listing.getNftObjectId(), nft -> {
				nft.setIsListed(false);
				nft.setListingPrice(null);
			});
		}

		processedEventService.markProcessed(txDigest, "ListingCancelled" + listingObjectId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handleListingFilled(String listingObjectId) {
		QueryWrapper<Listings> query = new QueryWrapper<>();
		query.eq("listing_object_id", listingObjectId);
		Listings listing = this.getOne(query);

		if (listing != null) {
			listing.setStatus(1); // Filled
			this.updateById(listing);
		}
	}

	@Override
	public IPage<ListingResp> getMyListings(MyListingNftRequest request) {
		LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Users::getWalletAddress, request.getWalletAddress());
		Users user = usersMapper.selectOne(queryWrapper);

		if (user == null || StrUtil.isBlank(user.getWalletAddress())) {
			return new Page<>(request.getCurrent(), request.getSize());
		}

		Page<Listings> page = new Page<>(request.getCurrent(), request.getSize());
		LambdaQueryWrapper<Listings> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Listings::getOwnerAddress, user.getWalletAddress());
		wrapper.eq(Listings::getStatus, 0); // ACTIVE
		wrapper.orderByDesc(Listings::getCreatedAt);
		IPage<Listings> listingPage = this.page(page, wrapper);
		List<String> listingNftObjectIds = listingPage.getRecords().stream().map(Listings::getListingObjectId)
				.collect(Collectors.toList());
		if (CollUtil.isEmpty(listingNftObjectIds)) {
			Page<ListingResp> resultPage = new Page<>(request.getCurrent(), request.getSize(), listingPage.getTotal());
			resultPage.setRecords(List.of());
			return resultPage;

		}
		List<ListingNft> queryListingNfts = oneChainService.queryMyListingNfts(listingNftObjectIds);
		log.info("Queried listing NFTs from OneChain: {}", JSONUtil.toJsonStr(queryListingNfts));
		List<ListingResp> collect = listingPage.getRecords().stream().map(listing -> {
			ListingResp copyProperties = BeanUtil.copyProperties(listing, ListingResp.class);
			queryListingNfts.stream().filter(lisingNft -> lisingNft.getObjectId().equals(listing.getNftObjectId())).findFirst()
					.ifPresent(nft -> {
						copyProperties.setListingNft(nft);
					});
			return copyProperties;
		}).collect(Collectors.toList());
		Page<ListingResp> resultPage = new Page<>(request.getCurrent(), request.getSize(), listingPage.getTotal());
		resultPage.setRecords(collect);
		return resultPage;
	}
}