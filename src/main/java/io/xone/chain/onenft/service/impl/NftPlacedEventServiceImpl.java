package io.xone.chain.onenft.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.onechain.OneChain;
import io.onechain.models.objects.ObjectDataOptions;
import io.onechain.models.objects.OneChainObjectData;
import io.onechain.models.objects.OneChainObjectResponse;
import io.onechain.models.objects.OneChainParsedData;
import io.xone.chain.onenft.entity.NftPlacedEvent;
import io.xone.chain.onenft.entity.Nfts;
import io.xone.chain.onenft.mapper.NftPlacedEventMapper;
import io.xone.chain.onenft.service.INftPlacedEventService;
import io.xone.chain.onenft.service.INftsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * nft放入市场事件 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-24
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NftPlacedEventServiceImpl extends ServiceImpl<NftPlacedEventMapper, NftPlacedEvent>
		implements INftPlacedEventService {

	private final INftsService nftsService;

	private final OneChain oneChain;

	@Override
	public void handleNFTPlacedEvent(String txHash, String walletAddress, String eventType, String kioskId,
			String nftObjectId, Long timestampMs) {
		// 更新nft加入市场
		updateNft(kioskId, nftObjectId, walletAddress);

		// Check duplicate
		QueryWrapper<NftPlacedEvent> query = new QueryWrapper<>();
		query.eq("txHash", txHash);
		if (this.count(query) > 0) {
			log.info("NFT placed Event already exists: {}", txHash);
			return;
		}

		NftPlacedEvent entity = new NftPlacedEvent();
		entity.setTxHash(txHash);
		entity.setWalletAddress(walletAddress);
		entity.setEventType(eventType);
		entity.setKioskId(kioskId);
		entity.setNftObjectId(nftObjectId);
		if (timestampMs != null) {
			entity.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMs), ZoneId.systemDefault()));
		}
		this.save(entity);
		log.info("Saved KioskCreated event: {}", txHash);
	}

	private void updateNft(String kioskId, String nftObjectId, String walletAddress) {
		if (kioskId == null || nftObjectId == null) {
			return;
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
//SuiObjectData{objectId='0xddf2c32c5ce5c1daafff19c24362798ce23381f86f66f10861030593685c3d96', version=267563106, digest='6YRahMDHsnKZTfnGkREtWa1Wr2svDG7ScQbrmEQ3kpBq', type='0xe4f0d1d7cacff10bab9a6d35920b90a4e4bc126b5f43c886e7f957641e81c966::nft_client::MyGameNFT', content=MoveObject{type='0xe4f0d1d7cacff10bab9a6d35920b90a4e4bc126b5f43c886e7f957641e81c966::nft_client::MyGameNFT, dataType='moveObject, fields={data={type=0xe4f0d1d7cacff10bab9a6d35920b90a4e4bc126b5f43c886e7f957641e81c966::nft_standard::OneNFTStandard, fields={description=Awesome NFT, game=MyGame, image_url=https://zcokpvofuddigtybzmap.supabase.co/storage/v1/object/public/images/merchandise/1768298620692-free_stock_photo.jpg, metadata_uri=null, name=My First NFT}}, id={id=0xddf2c32c5ce5c1daafff19c24362798ce23381f86f66f10861030593685c3d96}, level=5, rarity=epic}, hasPublicTransfer=true}, bcs=null, owner=ObjectOwner{objectOwner='0xda631dddfa13e6ba612b8911156a7af0ad22a060d84ee11ca6ce44b744c6a893'}, previousTransaction='null', storageRebate=null, display=null}
			log.info("Fetched NFT object data: {}", data);
		} catch (InterruptedException e) {
			log.error("Error fetching NFT object data for {}: {}", nftObjectId, e.getMessage());
			return;
		} catch (ExecutionException e) {
			log.error("Error fetching NFT object data for {}: {}", nftObjectId, e.getMessage());
			return;
		}

		if (data == null || data.getContent() == null) {
			log.error("Failed to get object data for {}", nftObjectId);
			return;
		}

		QueryWrapper<Nfts> userQuery = new QueryWrapper<>();
		userQuery.eq("objectId", nftObjectId);
		Nfts nft = nftsService.getOne(userQuery);

		if (nft == null) {
			nft = new Nfts();
			nft.setObjectId(nftObjectId);
			nft.setCreatedAt(LocalDateTime.now());
		}
		nft.setKioskId(kioskId);
		nft.setIsListed(false);
		nft.setMintTxHash(data.getDigest());
		nft.setUpdatedAt(LocalDateTime.now());
		nft.setOwnerAddress(walletAddress);
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
				// Handle potentially nested data structure based on the log example
				Map<String, Object> targetFields = (Map<String, Object>) fields;
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
				if (targetFields.containsKey("metadata_uri")) {
//					nft.setAttributes(targetFields.get("metadata_uri").toString());
				}
			}
		}
		nftsService.saveOrUpdate(nft);
	}

}