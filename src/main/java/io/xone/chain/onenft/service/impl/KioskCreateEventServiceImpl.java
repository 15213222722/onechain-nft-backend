package io.xone.chain.onenft.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.xone.chain.onenft.entity.KioskCreateEvent;
import io.xone.chain.onenft.entity.Users;
import io.xone.chain.onenft.mapper.KioskCreateEventMapper;
import io.xone.chain.onenft.service.IKioskCreateEventService;
import io.xone.chain.onenft.service.IUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * kiosk创建 服务实现类
 * </p>
 *
 * @author GitHub Copilot
 * @since 2026-01-23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KioskCreateEventServiceImpl extends ServiceImpl<KioskCreateEventMapper, KioskCreateEvent> implements IKioskCreateEventService {

    private final IUsersService usersService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleKioskCreatedEvent(String txHash, String walletAddress, String eventType, String kioskId, String capId, Long timestampMs) {
    	 // update user kioskId
        updateUserKioskId(walletAddress, kioskId, capId);
    	// Check duplicate
        QueryWrapper<KioskCreateEvent> query = new QueryWrapper<>();
        query.eq("txHash", txHash);
        if (this.count(query) > 0) {
            log.info("Event already exists: {}", txHash);
            return;
        }

        KioskCreateEvent entity = new KioskCreateEvent();
        entity.setTxHash(txHash);
        entity.setWalletAddress(walletAddress);
        entity.setEventType(eventType);
        entity.setKioskId(kioskId);
        entity.setCapId(capId);
        if (timestampMs != null) {
            entity.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMs), ZoneId.systemDefault()));
        }

        this.save(entity);
        log.info("Saved KioskCreated event: {}", txHash);

       
    }

    private void updateUserKioskId(String walletAddress, String kioskId, String capId) {
    	log.info("Updating user kioskId: walletAddress={}, kioskId={}", walletAddress, kioskId);
        if (walletAddress == null || kioskId == null) {
            return;
        }

        QueryWrapper<Users> userQuery = new QueryWrapper<>();
        userQuery.eq("walletAddress", walletAddress);
        Users user = usersService.getOne(userQuery);

        if (user != null) {
            if (user.getKioskId() == null || user.getKioskId().trim().isEmpty()) {
                user.setKioskId(kioskId);
                user.setCapId(capId);
                boolean updated = usersService.updateById(user);
                if (updated) {
                    log.info("Updated kioskId for user: {}", walletAddress);
                } else {
                    log.error("Failed to update kioskId for user: {}", walletAddress);
                }
            }
        }
    }
}