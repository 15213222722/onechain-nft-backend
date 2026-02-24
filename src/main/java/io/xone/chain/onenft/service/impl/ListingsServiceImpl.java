package io.xone.chain.onenft.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
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
import io.xone.chain.onenft.common.entity.ListingNft;
import io.xone.chain.onenft.common.service.OneChainService;
import io.xone.chain.onenft.entity.Listings;
import io.xone.chain.onenft.entity.Users;
import io.xone.chain.onenft.enums.ActivityTargetTypeEnum;
import io.xone.chain.onenft.enums.ActivityTypeEnum;
import io.xone.chain.onenft.event.ActivityEvent;
import io.xone.chain.onenft.mapper.ListingsMapper;
import io.xone.chain.onenft.mapper.UsersMapper;
import io.xone.chain.onenft.request.ListingQueryRequest;
import io.xone.chain.onenft.request.MyListingNftRequest;
import io.xone.chain.onenft.resp.ListingResp;
import io.xone.chain.onenft.service.IListingsService;
import io.xone.chain.onenft.service.INftDelistEventService;
import io.xone.chain.onenft.service.INftListingEventService;
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
	private final UsersMapper usersMapper;

	private final INftListingEventService nftListingEventService;

	private final INftDelistEventService nftDelistEventService;

	private final OneChainService oneChainService;

	private final ApplicationEventPublisher eventPublisher;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handleListingCreated(String txDigest, String listingObjectId, String owner, String nftObjectId,
			String nftType, Integer listingType, Long price, String coinType, String expectedNftType,
			Long timestampMs) {
		if (processedEventService.isProcessed(txDigest, "ListingCreated" + listingObjectId)) {
			log.info("ListingCreated already processed: {}", txDigest);
			return;
		}

		nftListingEventService.handleNFTListEvent(txDigest, owner, "ListingCreated", listingObjectId, nftObjectId,
				price, timestampMs);

		QueryWrapper<Listings> query = new QueryWrapper<>();
		query.eq("listing_object_id", listingObjectId);
		Listings listing = this.getOne(query);
		if (listing != null) {
			log.info("Listing already exists, updating: {}", listingObjectId);
		} else {
			listing = new Listings();
		}
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
		this.saveOrUpdate(listing);

		processedEventService.markProcessed(txDigest, "ListingCreated" + listingObjectId);

		eventPublisher.publishEvent(ActivityEvent.builder(this).activityType(ActivityTypeEnum.NFT_LISTED)
				.actorAddress(owner).targetType(ActivityTargetTypeEnum.NFT).targetId(nftObjectId).txDigest(txDigest)
				.addMetadata("price", price).addMetadata("listing_object_id", listingObjectId)
				.addMetadata("name",
						StrUtil.isNotBlank(listing.getCollectionName()) ? listing.getCollectionName() : listingObjectId)
				.addMetadata("coin_type", coinType).addMetadata("expected_nft_type", expectedNftType)
				.occurredAt(timestampMs).build());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handleListingCancelled(String txDigest, String listingObjectId, Long timestampMs) {
		if (processedEventService.isProcessed(txDigest, "ListingCancelled" + listingObjectId)) {
			log.info("ListingCancelled already processed: {}", txDigest);
			return;
		}

		Listings listing = this
				.getOne(new LambdaQueryWrapper<Listings>().eq(Listings::getListingObjectId, listingObjectId));
		if (listing != null) {
			listing.setStatus(2); // Cancelled
			// We might want to clear price or set to invalid? No, keep history.
			this.updateById(listing);

			nftDelistEventService.handleNFTDelistEvent(txDigest, listing.getOwnerAddress(), "ListingCancelled",
					listingObjectId, listing.getNftObjectId(), timestampMs);

			eventPublisher.publishEvent(ActivityEvent.builder(this).activityType(ActivityTypeEnum.NFT_UNLISTED)
					.actorAddress(listing.getOwnerAddress()).targetType(ActivityTargetTypeEnum.NFT)
					.targetId(listing.getNftObjectId()).addMetadata("listing_object_id", listingObjectId)
					.addMetadata("name", StrUtil.isNotBlank(listing.getCollectionName()) ? listing.getCollectionName()
							: listingObjectId)
					.txDigest(txDigest).occurredAt(timestampMs).build());
		} else {
			log.warn("Listing cancelled but not found in DB: {}", listingObjectId);
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
			listing.setFilledTimestampMs(System.currentTimeMillis());
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
			queryListingNfts.stream().filter(lisingNft -> lisingNft.getObjectId().equals(listing.getNftObjectId()))
					.findFirst().ifPresent(nft -> {
						copyProperties.setListingNft(nft);
					});
			return copyProperties;
		}).collect(Collectors.toList());
		Page<ListingResp> resultPage = new Page<>(request.getCurrent(), request.getSize(), listingPage.getTotal());
		resultPage.setRecords(collect);
		return resultPage;
	}

	@Override
	public IPage<ListingResp> listingsQuery(ListingQueryRequest request) {
		Page<Listings> page = new Page<>(request.getCurrent(), request.getSize());
		LambdaQueryWrapper<Listings> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Listings::getStatus, 0); // ACTIVE

		if (StrUtil.isNotBlank(request.getSearchKey())) {
			String key = request.getSearchKey();
			wrapper.and(w -> w.eq(Listings::getNftObjectId, key).or().eq(Listings::getListingObjectId, key).or()
					.eq(Listings::getOwnerAddress, key).or()
					// Since we don't have NFT name in db, we use collectionName or nftType for
					// partial match
					.like(Listings::getCollectionName, key));
		}

		if (StrUtil.isNotBlank(request.getSort())) {
			String sort = request.getSort();
			boolean isAsc = sort.endsWith("_asc");
			if (sort.startsWith("price")) {
				wrapper.orderBy(true, isAsc, Listings::getPrice);
			} else if (sort.startsWith("time")) {
				// User asked for "update time", usually created or updated
				wrapper.orderBy(true, isAsc, Listings::getUpdatedAt);
			} else {
				wrapper.orderByDesc(Listings::getUpdatedAt);
			}
		} else {
			wrapper.orderByDesc(Listings::getUpdatedAt);
		}

		IPage<Listings> listingPage = this.page(page, wrapper);
		List<String> listingNftObjectIds = listingPage.getRecords().stream().map(Listings::getListingObjectId)
				.collect(Collectors.toList());
		if (CollUtil.isEmpty(listingNftObjectIds)) {
			Page<ListingResp> resultPage = new Page<>(request.getCurrent(), request.getSize(), listingPage.getTotal());
			resultPage.setRecords(List.of());
			return resultPage;

		}
		List<ListingNft> queryListingNfts = oneChainService.queryMyListingNfts(listingNftObjectIds);
		List<ListingResp> collect = listingPage.getRecords().stream().map(listing -> {
			ListingResp copyProperties = BeanUtil.copyProperties(listing, ListingResp.class);
			queryListingNfts.stream().filter(lisingNft -> lisingNft.getObjectId().equals(listing.getNftObjectId()))
					.findFirst().ifPresent(nft -> {
						copyProperties.setListingNft(nft);
					});
			return copyProperties;
		}).collect(Collectors.toList());
		Page<ListingResp> resultPage = new Page<>(request.getCurrent(), request.getSize(), listingPage.getTotal());
		resultPage.setRecords(collect);
		return resultPage;
	}

	@Override
	public Listings queryListingByListingObjectId(String listingObjectId) {
		QueryWrapper<Listings> query = new QueryWrapper<>();
		query.eq("listing_object_id", listingObjectId);
		return this.getOne(query);
	}
}
