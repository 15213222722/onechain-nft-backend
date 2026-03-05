package io.xone.chain.onenft.dto;

import lombok.Data;

@Data
public class VerificationApplicationDTO {
    private String applicantWallet;
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
}
