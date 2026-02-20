package io.xone.chain.onenft.resp;

import io.xone.chain.onenft.common.entity.ListingNft;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class UserCollectionResp extends ListingNft {
    // Add any specific fields if needed, otherwise it extends ListingNft which contains NFT details
    
    private Boolean isListed;
}
