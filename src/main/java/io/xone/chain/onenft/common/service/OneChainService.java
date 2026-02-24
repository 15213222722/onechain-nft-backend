package io.xone.chain.onenft.common.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import cn.hutool.json.JSONUtil;
import io.onechain.OneChain;
import io.onechain.models.objects.ObjectDataOptions;
import io.onechain.models.objects.OneChainObjectData;
import io.onechain.models.objects.OneChainObjectResponse;
import io.onechain.models.objects.OneChainParsedData;
import io.xone.chain.onenft.common.entity.ListingNft;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OneChainService {

	private final OneChain oneChain;

	public List<ListingNft> queryListingNfts(List<String> nftObjectIds) {
		try {
			log.info("Fetching NFT objects from OneChain for IDs:{}", nftObjectIds);
			List<OneChainObjectResponse> list = oneChain.multiGetObjects(nftObjectIds, new ObjectDataOptions()).get();
			log.info("Fetched NFT objects from OneChain:{}", list);
			List<ListingNft> nfts = new java.util.ArrayList<>();

			list.stream().map(OneChainObjectResponse::getData)
					.forEach(new java.util.function.Consumer<OneChainObjectData>() {
						@Override
						public void accept(OneChainObjectData data) {
							ListingNft formatNft = formatNft(data);
							nfts.add(formatNft);
						}
					});
			return nfts;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;

	}

	public List<ListingNft> queryMyListingNfts(List<String> listingObjectIds) {
		try {
			log.info("Fetching listing NFT objects from OneChain for IDs:{}", listingObjectIds);
			List<OneChainObjectResponse> list = oneChain.multiGetObjects(listingObjectIds, new ObjectDataOptions())
					.get();

			log.info("Fetched NFT objects from OneChain:{}", JSONUtil.toJsonStr(list));

			List<ListingNft> nfts = new java.util.ArrayList<>();

			if (list != null) {
				for (OneChainObjectResponse response : list) {
					if (response.getData() == null)
						continue;
					OneChainObjectData data = response.getData();
					if (data.getContent() instanceof OneChainParsedData.MoveObject) {
						OneChainParsedData.MoveObject moveObject = (OneChainParsedData.MoveObject) data.getContent();
						Map<String, ?> fields = moveObject.getFields();
						if (fields != null) {
							@SuppressWarnings("unchecked")
							Map<String, Object> fieldsMap = (Map<String, Object>) fields;
							if (fieldsMap.containsKey("nft") && fieldsMap.get("nft") instanceof Map) {
								@SuppressWarnings("unchecked")
								Map<String, Object> nftMap = (Map<String, Object>) fieldsMap.get("nft");
								if (nftMap.containsKey("fields") && nftMap.get("fields") instanceof Map) {
									@SuppressWarnings("unchecked")
									Map<String, Object> nftFields = (Map<String, Object>) nftMap.get("fields");
									ListingNft nft = new ListingNft();
									if (nftMap.containsKey("type")) {
										nft.setNftType(nftMap.get("type").toString());
									}
									if (fieldsMap.containsKey("owner")) {
										nft.setOwnerAddress(fieldsMap.get("owner").toString());
									}
									if (fieldsMap.containsKey("nft_id")) {
										System.out.println("xxxxx:" + fieldsMap.get("nft_id").toString());
										nft.setObjectId(fieldsMap.get("nft_id").toString());
									}
									fillNftMetadata(nft, nftFields);
									nfts.add(nft);
								}
							}
						}
					}
				}
			}
			return nfts;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;

	}

	public ListingNft formatNft(OneChainObjectData data) {
		log.info("Listing NFT:{}", data);
		ListingNft nft = new ListingNft();
		nft.setObjectId(data.getObjectId());
		nft.setNftType(data.getType());
		nft.setOwnerAddress(JSONUtil.parseObj(data.getOwner()).getStr("AddressOwner"));
		nft.setMintTxHash(data.getDigest());
		if (data.getContent() instanceof OneChainParsedData.MoveObject) {
			OneChainParsedData.MoveObject moveObject = (OneChainParsedData.MoveObject) data.getContent();
			Map<String, ?> fields = moveObject.getFields();

			if (fields != null) {
				@SuppressWarnings("unchecked")
				Map<String, Object> targetFields = (Map<String, Object>) fields;
				fillNftMetadata(nft, targetFields);
			}
		}
		return nft;
	}

	private void fillNftMetadata(ListingNft nft, Map<String, Object> fields) {
		Map<String, Object> targetFields = fields;
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

	public List<ListingNft> getListedNfts(List<String> nftObjectIds) {
		try {
			log.info("Fetching NFT objects from OneChain for IDs:{}", nftObjectIds);
			List<OneChainObjectResponse> list = oneChain.multiGetObjects(nftObjectIds, new ObjectDataOptions()).get();
			log.info("Fetched NFT objects from OneChain:{}", list);
			List<ListingNft> nfts = new java.util.ArrayList<>();

			list.stream().map(OneChainObjectResponse::getData)
					.forEach(new java.util.function.Consumer<OneChainObjectData>() {
						@Override
						public void accept(OneChainObjectData data) {
							if (data == null) {
								return;
							}
							ListingNft formatNft = formatNft(data);
							nfts.add(formatNft);
						}
					});
			return nfts;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;

	}

	public ListingNft queryNftDetail(String nftObjectId) {
		try {
			log.info("Fetching NFT object from OneChain for ID:{}", nftObjectId);
			OneChainObjectResponse response = oneChain.getObject(nftObjectId, new ObjectDataOptions()).get();
			log.info(nftObjectId + " fetched from OneChain:{}", JSONUtil.toJsonStr(response));
			if (response != null && response.getData() != null) {
				return formatNft(response.getData());
			}
		} catch (InterruptedException | ExecutionException e) {
			log.error("Error fetching NFT detail", e);
		}
		return null;
	}
}