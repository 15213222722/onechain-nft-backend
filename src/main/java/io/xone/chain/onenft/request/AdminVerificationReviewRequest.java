package io.xone.chain.onenft.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.xone.chain.onenft.enums.VerificationStatusEnum;
import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * 管理员审核集合验证申请请求体
 */
@ApiModel(value = "AdminVerificationReviewRequest", description = "管理员审核集合验证申请请求体")
@Data
public class AdminVerificationReviewRequest {
    @ApiModelProperty(value = "申请ID", required = true)
    @NotNull(message = "申请ID不能为空")
    private Long id;

    @ApiModelProperty(value = "审核状态（APPROVED/REJECTED）", required = true)
    @NotNull(message = "审核状态不能为空")
    private VerificationStatusEnum status;

    @ApiModelProperty(value = "是否符合display标准")
    private Boolean isDisplay;

    @ApiModelProperty(value = "分类")
    private String category;

    @ApiModelProperty(value = "若拒绝，填写原因")
    private String rejectionReason;
}
