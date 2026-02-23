package io.xone.chain.onenft.resp;

import io.xone.chain.onenft.common.entity.ListingNft;
import lombok.Data;

@Data
public class TradeResp {
    
    private Long id;
    
    private ListingNft nftInfo;
    
    private UserResp buyer;
    
    private UserResp seller;
    
    private Long price;
    
    private String coinType;
    
    private Long timestamp;
    
    private Byte tradeType;
}
