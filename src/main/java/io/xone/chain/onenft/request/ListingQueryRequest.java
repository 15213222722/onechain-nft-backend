package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("Listing query Search Request")
public class ListingQueryRequest extends PageRequest {
	
	private String walletAddress;
	
	private String searchKey;
	
	private String sort; // price_asc, price_desc, time_asc, time_desc

    /** 价格最小值（单位：分/wei等） */
    private Long minPrice;
    /** 价格最大值 */
    private Long maxPrice;
    /** collectionSlug精确查询 */
    private String collectionSlug;
    /** NFT对象ID精确查询 */
    private String nftObjectId;
    /** 币种精确查询 */
    private String coinType;
}