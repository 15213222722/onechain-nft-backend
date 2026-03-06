package io.xone.chain.onenft.admin.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.xone.chain.onenft.common.entity.Result;
import io.xone.chain.onenft.entity.VerificationApplications;
import io.xone.chain.onenft.request.AdminVerificationReviewRequest;
import io.xone.chain.onenft.resp.VerificationApplicationResp;
import io.xone.chain.onenft.service.VerificationApplicationsService;

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