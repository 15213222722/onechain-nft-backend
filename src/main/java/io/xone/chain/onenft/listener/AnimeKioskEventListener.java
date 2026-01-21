package io.xone.chain.onenft.listener;

import io.onechain.OneChain;
import io.onechain.models.OneChainApiException;
import io.onechain.models.events.EventFilter;
import io.onechain.models.events.OneChainEvent;
import io.xone.chain.onenft.config.OneChainProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class AnimeKioskEventListener implements CommandLineRunner {

    private final OneChain oneChain;
    private final OneChainProperties properties;

    public AnimeKioskEventListener(OneChain oneChain, OneChainProperties properties) {
        this.oneChain = oneChain;
        this.properties = properties;
    }

    @Override
    public void run(String... args) throws Exception {
        OneChainProperties.Contract contract = properties.getContract();
        String fullEventType = String.format("%s::%s::%s", 
                contract.getPackageId(), 
                contract.getModuleName(), 
                contract.getEventStruct());
        
        log.info("Starting listener for event: {}", fullEventType);

        EventFilter.MoveEventTypeEventFilter eventFilter = new EventFilter.MoveEventTypeEventFilter();
        eventFilter.setMoveEventType(fullEventType);

        oneChain.subscribeEvent(eventFilter, this::onEvent, this::onError);
        
        log.info("Subscribed to {}", fullEventType);
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