package io.xone.chain.onenft.request;

import java.math.BigDecimal;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.xone.chain.onenft.common.validator.EnumValue;
import io.xone.chain.onenft.enums.ListingStatusEnum;
import io.xone.chain.onenft.enums.RarityEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("NFT Search Request")
public class NftSearchRequest extends PageRequest {

    @ApiModelProperty("Min Price")
    private BigDecimal minPrice;

    @ApiModelProperty("Max Price")
    private BigDecimal maxPrice;

    @ApiModelProperty("Rarity Type (common, rare, epic, legendary)")
    @EnumValue(enumClass = RarityEnum.class, method = "getCode")
    private List<String> rarityType;

    @ApiModelProperty("Collection/Series ID")
    private Integer seriesId;

    @ApiModelProperty("Listing Status (LISTED, NOT_LISTED)")
    @EnumValue(enumClass = ListingStatusEnum.class)
    private String status;
}