package io.xone.chain.onenft.service;

import io.xone.chain.onenft.dto.VerificationApplicationDTO;
import io.xone.chain.onenft.entity.VerificationApplications;
import io.xone.chain.onenft.request.AdminVerificationReviewRequest;
import io.xone.chain.onenft.request.VerificationApplicationRequest;

public interface VerificationApplicationsService {
    /**
     * 用户提交集合验证申请
     * @param request 申请参数
     * @return VerificationApplications 数据库实体
     */
    VerificationApplications applyForVerification(VerificationApplicationRequest request);

    /**
     * 管理员审核集合验证申请
     * @param request 审核请求
     * @return 审核后实体
     */
    VerificationApplications reviewVerificationApplication(AdminVerificationReviewRequest request);
}