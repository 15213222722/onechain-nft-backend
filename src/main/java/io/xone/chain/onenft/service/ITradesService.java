package io.xone.chain.onenft.service;

import io.xone.chain.onenft.entity.Trades;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * NFT 成交记录表 服务类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-28
 */
public interface ITradesService extends IService<Trades> {

    void handleListingSaleFilled(String txDigest, String listingObjectId, String taker, String lister, String nftId, 
            Long paymentAmount, Long feeAmount, Long sellerAmount, String coinType, Long timestampMs);

    void handleListingSwapFilled(String txDigest, String listingObjectId, String taker, String lister, 
            String nftIdOut, String nftIdIn, Long timestampMs);

}