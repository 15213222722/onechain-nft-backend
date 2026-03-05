package io.xone.chain.onenft.admin.controller;

import io.xone.chain.onenft.entity.VerificationApplications;
import io.xone.chain.onenft.enums.VerificationStatusEnum;
import io.xone.chain.onenft.service.VerificationApplicationsService;
import io.xone.chain.onenft.resp.VerificationApplicationResp;
import io.xone.chain.onenft.request.AdminVerificationReviewRequest;
import io.xone.chain.onenft.common.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * 管理员审核系列验证申请接口
 */
@RestController
@RequestMapping("/admin/verificationApplications")
public class AdminVerificationApplicationsController {
    @Autowired
    private VerificationApplicationsService verificationApplicationsService;

    /**
     * 审核集合验证申请
     * @param request 审核请求
     * @return 审核后结果
     */
    @PostMapping("/review")
    public Result<VerificationApplicationResp> review(@Valid @RequestBody AdminVerificationReviewRequest request) {
        VerificationApplications entity = verificationApplicationsService.reviewVerificationApplication(request);
        VerificationApplicationResp resp = new VerificationApplicationResp();
        resp.setId(entity.getId());
        resp.setStatus(entity.getStatus());
        resp.setReviewedAt(entity.getReviewedAt());
        resp.setRejectionReason(entity.getRejectionReason());
        resp.setIsDisplay(entity.getIsDisplay());
        resp.setCategory(entity.getCategory());
        // 其他字段可补充
        return Result.success(resp);
    }
}