package io.xone.chain.onenft.dto;

import lombok.Data;

@Data
public class CollectionPageDTO {
    private String collection;
    private String collectionSlug;
    private long nftCount;
    private long holderCount;
    private String firstNftImage;
}
