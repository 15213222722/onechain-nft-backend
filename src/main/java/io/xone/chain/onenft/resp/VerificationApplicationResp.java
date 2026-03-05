package io.xone.chain.onenft.resp;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VerificationApplicationResp {
    private Long id;
    private String status;
    private LocalDateTime createdAt;
    private String collectionType;
    private String collectionName;
    private String logoUrl;
    private String description;
    private String websiteUrl;
    private String socialLinks;
    private Integer uniqueHoldersCount;
    private Boolean isOriginal;
    private String proofMaterials;
    private String contactEmail;
    private Boolean agreedTerms;
    private Boolean isDisplay;
    private String category;
    private LocalDateTime reviewedAt;
    private String rejectionReason;
}