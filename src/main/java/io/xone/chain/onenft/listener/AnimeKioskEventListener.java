package io.xone.chain.onenft.listener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.onechain.OneChain;
import io.onechain.models.events.EventFilter;
import io.onechain.models.events.EventId;
import io.onechain.models.events.OneChainEvent;
import io.onechain.models.events.PaginatedEvents;
import io.onechain.models.transactions.MoveFunction;
import io.onechain.models.transactions.TransactionBlockEffects;
import io.onechain.models.transactions.TransactionFilter;
import io.xone.chain.onenft.config.OneChainProperties;
import io.xone.chain.onenft.entity.KioskCreateEvent;
import io.xone.chain.onenft.service.IKioskCreateEventService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AnimeKioskEventListener implements CommandLineRunner {

    private final OneChain oneChain;
    
    private final OneChainProperties properties;
    
    private final IKioskCreateEventService kioskCreateEventService;

    public AnimeKioskEventListener(OneChain oneChain, OneChainProperties properties, IKioskCreateEventService kioskCreateEventService) {
        this.oneChain = oneChain;
        this.properties = properties;
        this.kioskCreateEventService = kioskCreateEventService;
    }

    @Override
    public void run(String... args) throws Exception {
//    	subscribeKioskPlace();
        subscribeKioskCreated();
    }


    private void subscribeKioskPlace() {
        log.info("Subscribing to kiosk::place(kiosk, cap, nft)");
        try {
            MoveFunction moveFunction = new MoveFunction();
            moveFunction.setSuiPackage("0x2");
            moveFunction.setModule("kiosk");
            moveFunction.setFunction("place");

            TransactionFilter.MoveFunctionFilter filter = new TransactionFilter.MoveFunctionFilter();
            filter.setMoveFunction(moveFunction);
            OneChain oneChain = this.oneChain;
            oneChain.subscribeTransaction(filter, 
                this::onKioskPlaceTransaction,
                (e) -> log.error("Error in kiosk::place subscription", e)
            );
            log.info("Subscribed to kiosk::place");
        } catch (Exception e) {
            log.error("Failed to subscribe to kiosk::place", e);
        }
    }

    private void onKioskPlaceTransaction(TransactionBlockEffects transaction) {
        log.info("Received kiosk::place transaction: {}", transaction.getTransactionDigest());
        // Logic to filter by subscriptions (packageIds) would go here
    }

    @Scheduled(fixedDelay = 5000)
    private void subscribeKioskCreated() {
        log.info("Subscribing to KioskCreated event");
        try {
            String packageId = properties.getContract().getPackageId();
            String moduleName = properties.getContract().getModuleName();
            String eventType = String.format("%s::%s::KioskCreated", packageId, moduleName);

            EventFilter.MoveEventTypeEventFilter filter = new EventFilter.MoveEventTypeEventFilter();
            filter.setMoveEventType(eventType);
            
            PaginatedEvents page = this.oneChain.queryEvents(
                    filter,
                    null,
                    10,
                    true
                ).get();
            
            if (page.getData() != null && !page.getData().isEmpty()) {
                for (OneChainEvent event : page.getData()) {
                    handleKioskCreatedEvent(event);
                }
            }
        } catch (Exception e) {
            log.error("Failed to subscribe to KioskCreated", e);
        }
    }

    private void handleKioskCreatedEvent(OneChainEvent event) {
        String txHash = event.getId().getTxDigest();
        
        // Check duplicate
        QueryWrapper<KioskCreateEvent> query = new QueryWrapper<>();
        query.eq("txHash", txHash);
        if (kioskCreateEventService.count(query) > 0) {
            log.info("Event already exists: {}", txHash);
            return;
        }
        
        KioskCreateEvent entity = new KioskCreateEvent();
        entity.setTxHash(txHash);
        entity.setWalletAddress(event.getSender());
        entity.setEventType(event.getType());
        
        if (event.getParsedJson() != null && event.getParsedJson() instanceof Map) {
             @SuppressWarnings("unchecked")
			 Map<String, Object> data = (Map<String, Object>) event.getParsedJson();
             if (data.containsKey("kiosk_id")) {
                 entity.setKioskId(data.get("kiosk_id").toString());
             }
        }
        
        if (event.getTimestampMs() != null) {
            entity.setCreatedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getTimestampMs().longValue()), ZoneId.systemDefault()));
        }
        
        kioskCreateEventService.save(entity);
        log.info("Saved KioskCreated event: {}", txHash);
    }

    private void onKioskCreatedEvent(OneChainEvent event) {
        log.info("Received KioskCreated event: {}", event);
    }

}