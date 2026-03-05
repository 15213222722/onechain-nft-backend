package io.xone.chain.onenft.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.xone.chain.onenft.entity.CollectionsVerification;
import io.xone.chain.onenft.entity.VerificationApplications;
import io.xone.chain.onenft.enums.VerificationStatusEnum;
import io.xone.chain.onenft.mapper.CollectionsVerificationMapper;
import io.xone.chain.onenft.mapper.VerificationApplicationsMapper;
import io.xone.chain.onenft.request.AdminVerificationReviewRequest;
import io.xone.chain.onenft.request.VerificationApplicationRequest;
import io.xone.chain.onenft.service.VerificationApplicationsService;

/**
 * <p>
 * NFT 集合验证申请记录表，保存申请者提交的材料与状态 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-03-05
 */
@Service
public class VerificationApplicationsServiceImpl implements VerificationApplicationsService {
    @Autowired
    private VerificationApplicationsMapper verificationApplicationsMapper;
    @Autowired
    private CollectionsVerificationMapper collectionsVerificationMapper;

    /**
     * 用户提交集合验证申请
     * @param request 申请参数
     * @return 申请结果
     */
    @Override
    public VerificationApplications applyForVerification(VerificationApplicationRequest request) {
        VerificationApplications entity = new VerificationApplications();
        // 拷贝参数
        entity.setApplicantWallet(request.getApplicantWallet());
        entity.setCollectionType(request.getCollectionType());
        entity.setCollectionName(request.getCollectionName());
        entity.setLogoUrl(request.getLogoUrl());
        entity.setDescription(request.getDescription());
        entity.setWebsiteUrl(request.getWebsiteUrl());
        entity.setSocialLinks(request.getSocialLinks());
        entity.setUniqueHoldersCount(request.getUniqueHoldersCount());
        entity.setIsOriginal(request.getIsOriginal());
        entity.setProofMaterials(request.getProofMaterials());
        entity.setContactEmail(request.getContactEmail());
        entity.setAgreedTerms(request.getAgreedTerms());
        entity.setIsDisplay(request.getIsDisplay());
        entity.setCategory(request.getCategoryEnum().name());
        // 状态用枚举
        entity.setStatus(VerificationStatusEnum.PENDING.getCode());
        entity.setCreatedAt(java.time.LocalDateTime.now());
        entity.setUpdatedAt(java.time.LocalDateTime.now());
        entity.setCategory(request.getCategoryEnum() != null ? request.getCategoryEnum().name() : null);
        verificationApplicationsMapper.insert(entity);
        return entity;
    }

    /**
     * 管理员审核集合验证申请
     * @param request 审核参数
     * @return 审核结果
     */
    @Override
    public VerificationApplications reviewVerificationApplication(AdminVerificationReviewRequest request) {
        VerificationApplications entity = verificationApplicationsMapper.selectById(request.getId());
        if (entity == null) {
            throw new IllegalArgumentException("未找到对应的验证申请");
        }
        // 只允许审核pending状态
        if (!VerificationStatusEnum.PENDING.getCode().equals(entity.getStatus())) {
            throw new IllegalStateException("该申请已审核，不能重复操作");
        }
        // 设置审核状态
        entity.setStatus(request.getStatus().getCode());
        entity.setReviewedAt(LocalDateTime.now());
        if (VerificationStatusEnum.REJECTED.equals(request.getStatus())) {
            entity.setRejectionReason(request.getRejectionReason());
        } else {
            entity.setRejectionReason(null);
        }
        verificationApplicationsMapper.updateById(entity);

        // 新增或更新集合验证状态表
        if (VerificationStatusEnum.APPROVED.equals(request.getStatus())) {
            CollectionsVerification cv = collectionsVerificationMapper.selectOne(
                new QueryWrapper<CollectionsVerification>()
                    .eq("collection_type", entity.getCollectionType())
            );
            if (cv == null) {
                cv = new CollectionsVerification();
                cv.setCollectionType(entity.getCollectionType());
            }
            cv.setIsVerified(true);
            cv.setVerifiedAt(LocalDateTime.now());
            cv.setLastApplicationId(entity.getId());
            cv.setCategory(entity.getCategory());
            cv.setUpdatedAt(LocalDateTime.now());
            cv.setRevokedAt(null);
            cv.setRevokeReason(null);
            collectionsVerificationMapper.insert(cv);
        }
        return entity;
    }
}