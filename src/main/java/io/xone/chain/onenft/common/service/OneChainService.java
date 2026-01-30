package io.xone.chain.onenft.common.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

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
			List<OneChainObjectResponse> list = oneChain.multiGetObjects(nftObjectIds, new ObjectDataOptions()).get();
			
			List<ListingNft> nfts = new java.util.ArrayList<>();
			
			list.stream().map(OneChainObjectResponse::getData).forEach(new java.util.function.Consumer<OneChainObjectData>() {
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
	
	
	
	public ListingNft formatNft(OneChainObjectData data) {
		log.info("Listing NFT:",data);
		ListingNft nft = new ListingNft();
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
		return nft;		
	}
	
	
	
	
	
	

}
