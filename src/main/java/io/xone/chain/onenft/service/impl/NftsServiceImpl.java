package io.xone.chain.onenft.service.impl;

import java.util.Locale;

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

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-16
 */
@Service
public class NftsServiceImpl extends ServiceImpl<NftsMapper, Nfts> implements INftsService {

	@Autowired
	private UsersMapper usersMapper;

	@Autowired
	private SeriesMapper seriesMapper;

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

		if (!StringUtils.isEmpty(request.getStatus())) {
			try {
				ListingStatusEnum statusEnum = ListingStatusEnum.valueOf(request.getStatus());
				wrapper.eq(Nfts::getIsListed, statusEnum.getCode());
			} catch (IllegalArgumentException e) {
				// Should be handled by validation, but safe ignore
			}
		}

		if (request.getMinPrice() != null) {
			wrapper.ge(Nfts::getListingPrice, request.getMinPrice());
		}
		if (request.getMaxPrice() != null) {
			wrapper.le(Nfts::getListingPrice, request.getMaxPrice());
		}
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
			if (StrUtil.isNotBlank(user.getKioskId())) {
				wrapper.eq(Nfts::getKioskId, user.getKioskId());
				hasCondition = true;
			}
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
}
