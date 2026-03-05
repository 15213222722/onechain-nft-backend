package io.xone.chain.onenft.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.bean.BeanUtil;
import io.xone.chain.onenft.common.entity.Result;
import io.xone.chain.onenft.entity.VerificationApplications;
import io.xone.chain.onenft.request.VerificationApplicationRequest;
import io.xone.chain.onenft.resp.VerificationApplicationResp;
import io.xone.chain.onenft.service.VerificationApplicationsService;

/**
 * <p>
 * NFT 集合验证申请记录表，保存申请者提交的材料与状态 前端控制器
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-03-05
 */
@RestController
@RequestMapping("/verificationApplications")
@Validated
public class VerificationApplicationsController {
    @Autowired
    private VerificationApplicationsService verificationApplicationsService;

    /**
     * 用户提交集合验证申请
     * @param request 申请参数
     * @return 申请结果
     */
    @PostMapping("/apply")
    public Result<VerificationApplicationResp> apply(@Valid @RequestBody VerificationApplicationRequest request) {
        VerificationApplications entity = verificationApplicationsService.applyForVerification(request);
        return Result.success(BeanUtil.copyProperties(entity, VerificationApplicationResp.class));
    }
}