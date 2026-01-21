package io.xone.chain.onenft.listener;

import io.onechain.OneChain;
import io.onechain.models.OneChainApiException;
import io.onechain.models.events.EventFilter;
import io.onechain.models.events.OneChainEvent;
import io.xone.chain.onenft.config.OneChainProperties;
import io.xone.chain.onenft.entity.ContractWhitelist;
import io.xone.chain.onenft.service.IContractWhitelistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import io.reactivex.rxjava3.disposables.Disposable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AnimeKioskEventListener implements CommandLineRunner {

    private final OneChain oneChain;
    private final OneChainProperties properties;
    private final IContractWhitelistService whitelistService;
    private final Map<String, Disposable> subscriptions = new ConcurrentHashMap<>();

    public AnimeKioskEventListener(OneChain oneChain, OneChainProperties properties, IContractWhitelistService whitelistService) {
        this.oneChain = oneChain;
        this.properties = properties;
        this.whitelistService = whitelistService;
    }

    @Override
    public void run(String... args) throws Exception {
        refreshSubscriptions();
    }

    public void refreshSubscriptions() {
        log.info("Refreshing contract subscriptions...");
        LambdaQueryWrapper<ContractWhitelist> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContractWhitelist::getIsActive, true);
        List<ContractWhitelist> whitelists = whitelistService.list(wrapper);

        // Identify currently active package IDs
        List<String> activePackageIds = whitelists.stream()
                .map(ContractWhitelist::getPackageId)
                .collect(Collectors.toList());

        // Unsubscribe removed contracts
        subscriptions.entrySet().removeIf(entry -> {
            boolean keep = activePackageIds.contains(entry.getKey());
            if (!keep) {
                log.info("Unsubscribing from package: {}", entry.getKey());
                entry.getValue().dispose();
            }
            return !keep;
        });

        // Subscribe new contracts
        OneChainProperties.Contract contractConfig = properties.getContract();
        for (ContractWhitelist cw : whitelists) {
            String packageId = cw.getPackageId();
            if (!subscriptions.containsKey(packageId)) {
                String fullEventType = String.format("%s::%s::%s", 
                        packageId, 
                        contractConfig.getModuleName(), 
                        contractConfig.getEventStruct());
                
                log.info("Subscribing to event: {}", fullEventType);
                
                EventFilter.MoveEventTypeEventFilter eventFilter = new EventFilter.MoveEventTypeEventFilter();
                eventFilter.setMoveEventType(fullEventType);

                Disposable disposable = oneChain.subscribeEvent(eventFilter, 
                    this::onEvent, 
                    (e) -> log.error("Error in subscription for {}: {}", fullEventType, e.getMessage())
                );
                
                subscriptions.put(packageId, disposable);
            }
        }
    }

    private void onEvent(OneChainEvent event) {
        log.info("Received event: {}", event);
        if (event.getParsedJson() != null) {
            Map<String, ?> data = event.getParsedJson();
            log.info("Event data: {}", data);
            
            Object nftId = data.get("nft_id");
            Object price = data.get("price");
            Object feePaid = data.get("fee_paid");

            log.info("NFT Sold - ID: {}, Price: {}, Fee Paid: {}", nftId, price, feePaid);
        }
    }

    private void onError(OneChainApiException throwable) {
        log.error("Error in AnimeKiosk event subscription", throwable);
        // Might implement logic to resubscribe logic here
    }
}