package io.xone.chain.onenft.service.impl;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import io.onechain.OneChain;
import io.onechain.models.objects.ObjectDataOptions;
import io.onechain.models.objects.OneChainObjectData;
import io.onechain.models.objects.OneChainObjectResponse;
import io.onechain.models.objects.OneChainParsedData;
import io.xone.chain.onenft.entity.Nfts;
import io.xone.chain.onenft.entity.Series;
import io.xone.chain.onenft.entity.Users;
import io.xone.chain.onenft.enums.ListingStatusEnum;
import io.xone.chain.onenft.mapper.NftsMapper;
import io.xone.chain.onenft.mapper.SeriesMapper;
import io.xone.chain.onenft.mapper.UsersMapper;
import io.xone.chain.onenft.request.MyKioskNftRequest;
import io.xone.chain.onenft.request.NftSearchRequest;
import io.xone.chain.onenft.resp.NftResp;
import io.xone.chain.onenft.service.INftsService;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-16
 */
@Slf4j
@Service
public class NftsServiceImpl extends ServiceImpl<NftsMapper, Nfts> implements INftsService {

	@Autowired
	private UsersMapper usersMapper;

	@Autowired
	private SeriesMapper seriesMapper;
	
	@Autowired
	private OneChain oneChain;

	@Override
	public IPage<Nfts> searchNfts(NftSearchRequest request) {
		Page<Nfts> page = new Page<>(request.getCurrent(), request.getSize());
		LambdaQueryWrapper<Nfts> wrapper = new LambdaQueryWrapper<>();

		if (request.getSeriesId() != null) {
			wrapper.eq(Nfts::getSeriesId, request.getSeriesId());
		}

		if (request.getRarityType() != null && !request.getRarityType().isEmpty()) {
			wrapper.in(Nfts::getRarityType, request.getRarityType());
		}

//		if (!StringUtils.isEmpty(request.getStatus())) {
//			try {
//				ListingStatusEnum statusEnum = ListingStatusEnum.valueOf(request.getStatus());
//				wrapper.eq(Nfts::getIsListed, statusEnum.getCode());
//			} catch (IllegalArgumentException e) {
//				// Should be handled by validation, but safe ignore
//			}
//		}

		if (request.getMinPrice() != null) {
			wrapper.ge(Nfts::getListingPrice, request.getMinPrice());
		}
		if (request.getMaxPrice() != null) {
			wrapper.le(Nfts::getListingPrice, request.getMaxPrice());
		}
		wrapper.eq(Nfts::getIsListed, ListingStatusEnum.LISTED.getCode());
		wrapper.orderByDesc(Nfts::getUpdatedAt);
		return this.page(page, wrapper);
	}

	@Override
	public Nfts getByObjectId(String objectId) {
		LambdaQueryWrapper<Nfts> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Nfts::getObjectId, objectId);
		return this.getOne(wrapper);
	}

	@Override
	public NftResp getNftDetail(String objectId) {
		Nfts nfts = this.getByObjectId(objectId);
		if (nfts == null) {
			return null;
		}
		NftResp resp = BeanUtil.copyProperties(nfts, NftResp.class);

		// Creator
		if (StrUtil.isNotBlank(nfts.getCreatorAddress())) {
			QueryWrapper<Users> userQuery = new QueryWrapper<>();
			userQuery.eq("walletAddress", nfts.getCreatorAddress());
			Users user = usersMapper.selectOne(userQuery);
			if (user != null) {
				resp.setCreatorName(StringUtils.isEmpty(user.getName()) ? user.getWalletAddress() : user.getName());
			}
		}

		// Owner
		if (nfts.getOwnerAddress() != null) {
			QueryWrapper<Users> userQuery = new QueryWrapper<>();
			userQuery.eq("walletAddress", nfts.getOwnerAddress());
			Users user = usersMapper.selectOne(userQuery);
			if (user != null) {
				resp.setOwnerName(StringUtils.isEmpty(user.getName()) ? user.getWalletAddress() : user.getName());
			}
		}

		// Series
		if (nfts.getSeriesId() != null) {
			Series series = seriesMapper.selectById(nfts.getSeriesId());
			if (series != null) {
				Locale locale = LocaleContextHolder.getLocale();
				boolean isChinese = Locale.CHINESE.getLanguage().equals(locale.getLanguage())
						|| Locale.SIMPLIFIED_CHINESE.getLanguage().equals(locale.getLanguage());
				// Assuming simplified chinese is the primary target for Chinese
				if (isChinese) {
					resp.setSeriesName(series.getZhDesc());
				} else {
					resp.setSeriesName(series.getEnDesc());
				}
			}
		}

		return resp;
	}

	@Override
	public IPage<Nfts> myKioskNfts(MyKioskNftRequest request) {
		int userId = StpUtil.getLoginIdAsInt();
		Users user;
		if (StrUtil.isNotBlank(request.getWalletAddress())) {
			QueryWrapper<Users> userQuery = new QueryWrapper<>();
			userQuery.eq("walletAddress", request.getWalletAddress());
			user = usersMapper.selectOne(userQuery);
		} else {
			user = usersMapper.selectById(userId);
		}
		Page<Nfts> page = new Page<>(request.getCurrent(), request.getSize());
		if (user != null) {
			LambdaQueryWrapper<Nfts> wrapper = new LambdaQueryWrapper<>();
			boolean hasCondition = false;
		
			if (StrUtil.isNotBlank(user.getWalletAddress())) {
				if (hasCondition) {
					wrapper.or();
				}
				wrapper.eq(Nfts::getOwnerAddress, user.getWalletAddress());
				hasCondition = true;
			}
			if (hasCondition) {
				return this.page(page, wrapper);
			}
		}
		return page;
	}

	@Override
	public Nfts syncNftFromChain(String nftObjectId, Consumer<Nfts> customizer) {
		if (nftObjectId == null) {
			return null;
		}
		
		ObjectDataOptions options = new ObjectDataOptions();
		options.setShowType(true);
		options.setShowDisplay(true);
		options.setShowContent(true);
		options.setShowOwner(true);
		options.setShowBcs(false);
		options.setShowPreviousTransaction(false);
		options.setShowStorageRebate(false);
		options.setShowDisplay(false);
		
		OneChainObjectData data = null;
		try {
			OneChainObjectResponse oneChainObjectResponse = oneChain.getObject(nftObjectId, options).get();
			data = oneChainObjectResponse.getData();
			log.info("Fetched NFT object data: {}", data);
		} catch (InterruptedException | ExecutionException e) {
			log.error("Error fetching NFT object data for {}: {}", nftObjectId, e.getMessage());
			return null;
		}

		if (data == null || data.getContent() == null) {
			log.error("Failed to get object data for {}", nftObjectId);
			return null;
		}

		QueryWrapper<Nfts> query = new QueryWrapper<>();
		query.eq("objectId", nftObjectId);
		Nfts nft = this.getOne(query);

		if (nft == null) {
			nft = new Nfts();
			nft.setObjectId(nftObjectId);
			nft.setCreatedAt(LocalDateTime.now());
		}

		nft.setMintTxHash(data.getDigest());
		nft.setUpdatedAt(LocalDateTime.now());
		nft.setNftType(data.getType());
		
		// Parse type to get contract address
		if (data.getType() != null) {
			String type = data.getType();
			String[] parts = type.split("::");
			if (parts.length > 0) {
				nft.setContractAddress(parts[0]);
			}
		}

		// Parse fields
		if (data.getContent() instanceof OneChainParsedData.MoveObject) {
			OneChainParsedData.MoveObject moveObject = (OneChainParsedData.MoveObject) data.getContent();
			Map<String, ?> fields = moveObject.getFields();

			if (fields != null) {
				@SuppressWarnings("unchecked")
				Map<String, Object> targetFields = (Map<String, Object>) fields;
				// Handle potentially nested data structure
				if (targetFields.containsKey("data") && targetFields.get("data") instanceof Map) {
					@SuppressWarnings("unchecked")
					Map<String, Object> innerData = (Map<String, Object>) targetFields.get("data");
					if (innerData.containsKey("fields") && innerData.get("fields") instanceof Map) {
						@SuppressWarnings("unchecked")
						Map<String, Object> innerFields = (Map<String, Object>) innerData.get("fields");
						targetFields = innerFields;
					}
				}

				if (targetFields.containsKey("name")) {
					nft.setName(targetFields.get("name").toString());
				}
				if (targetFields.containsKey("description")) {
					nft.setDescription(targetFields.get("description").toString());
				}
				if (targetFields.containsKey("image_url")) {
					nft.setImageUrl(targetFields.get("image_url").toString());
				}
				if (targetFields.containsKey("url")) {
					nft.setImageUrl(targetFields.get("url").toString());
				}
			}
		}
		
		// Apply custom updates
		if (customizer != null) {
			customizer.accept(nft);
		}

		this.saveOrUpdate(nft);
		return nft;
	}
}