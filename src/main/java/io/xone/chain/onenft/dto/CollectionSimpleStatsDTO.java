package io.xone.chain.onenft.dto;

import lombok.Data;

@Data
public class CollectionSimpleStatsDTO {
    private String collectionSlug;
    private long nftCount;
    private long tradeCount;
    private long holderCount;
}
