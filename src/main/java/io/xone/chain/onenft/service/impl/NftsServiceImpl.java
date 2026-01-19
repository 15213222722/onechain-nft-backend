package io.xone.chain.onenft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.xone.chain.onenft.entity.Nfts;
import io.xone.chain.onenft.enums.ListingStatusEnum;
import io.xone.chain.onenft.mapper.NftsMapper;
import io.xone.chain.onenft.request.NftSearchRequest;
import io.xone.chain.onenft.service.INftsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
	
	
	
	
	
	
	
	
}