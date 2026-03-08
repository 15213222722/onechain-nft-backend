package io.xone.chain.onenft.request;

import io.xone.chain.onenft.enums.CategoryEnum;
import lombok.Data;

@Data
public class CollectionPageQueryRequest extends PageRequest{
    /** 集合名称模糊查询 */
    private String collectionName;
    /** 集合slug精确查询 */
    private String collectionSlug;
    /** 持有者钱包地址 */
    private String walletAddress;
    /** 分类枚举 */
    private CategoryEnum categoryEnum;
    /** NFT对象ID */
    private String nftObjectId;
    /** 是否官方验证 */
    private String isVerified;
    /** 是否原创 */
    private String isOriginal;
}