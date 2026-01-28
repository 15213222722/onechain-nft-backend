package io.xone.chain.onenft.listener;

import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.onechain.OneChain;
import io.onechain.models.events.EventFilter;
import io.onechain.models.events.OneChainEvent;
import io.onechain.models.events.PaginatedEvents;
import io.xone.chain.onenft.config.OneChainProperties;
import io.xone.chain.onenft.service.IFeeConfigUpdatesService;
import io.xone.chain.onenft.service.IListingsService;
import io.xone.chain.onenft.service.ITradesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MarketPlaceEventListener implements CommandLineRunner {

    private final OneChain oneChain;
    private final OneChainProperties properties;
    private final IListingsService listingsService;
    private final ITradesService tradesService;
    private final IFeeConfigUpdatesService feeConfigUpdatesService;

    @Override
    public void run(String... args) throws Exception {
        subscribeListingCreated();
        subscribeListingSaleFilled();
        subscribeListingSwapFilled();
        subscribeListingCancelled();
        subscribeFeeUpdated();
    }

    @Scheduled(fixedDelay = 5000)
    public void subscribeListingCreated() {
        fetchAndProcessEvents("ListingCreated", this::handleListingCreated);
    }
    
    @Scheduled(fixedDelay = 5000)
    public void subscribeListingSaleFilled() {
        fetchAndProcessEvents("ListingSaleFilled", this::handleListingSaleFilled);
    }

    @Scheduled(fixedDelay = 5000)
    public void subscribeListingSwapFilled() {
        fetchAndProcessEvents("ListingSwapFilled", this::handleListingSwapFilled);
    }
    
    @Scheduled(fixedDelay = 5000)
    public void subscribeListingCancelled() {
        fetchAndProcessEvents("ListingCancelled", this::handleListingCancelled);
    }
    
    @Scheduled(fixedDelay = 5000)
    public void subscribeFeeUpdated() {
        fetchAndProcessEvents("FeeUpdated", this::handleFeeUpdated);
    }

    private void fetchAndProcessEvents(String eventSuffix, java.util.function.Consumer<OneChainEvent> handler) {
        try {
            String packageId = properties.getContract().getPackageId();
            // Assuming module is always "market"
            String moduleName = "market"; 
            String eventType = String.format("%s::%s::%s", packageId, moduleName, eventSuffix);

            EventFilter.MoveEventTypeEventFilter filter = new EventFilter.MoveEventTypeEventFilter();
            filter.setMoveEventType(eventType);

            PaginatedEvents page = this.oneChain.queryEvents(filter, null, 10, true).get();

            if (page.getData() != null && !page.getData().isEmpty()) {
                for (OneChainEvent event : page.getData()) {
                    log.info("Processing {} event: {}", eventSuffix, event);
                    handler.accept(event);
                }
            }
        } catch (Exception e) {
            log.error("Failed to poll for {}", eventSuffix, e);
        }
    }
    
    private void handleListingCreated(OneChainEvent event) {
        if (isInvalid(event)) return;
        Map<String, Object> data = (Map<String, Object>) event.getParsedJson();
        
        String listingId = getString(data, "listing_id");
        String owner = getString(data, "owner");
        String nftId = getString(data, "nft_id");
        Integer listingType = getInt(data, "listing_type");
        Long timestamp = event.getTimestampMs().longValue();
        
        listingsService.handleListingCreated(event.getId().getTxDigest(), listingId, owner, nftId, listingType, timestamp);
    }
    
    private void handleListingSaleFilled(OneChainEvent event) {
        if (isInvalid(event)) return;
        Map<String, Object> data = (Map<String, Object>) event.getParsedJson();

        String listingId = getString(data, "listing_id");
        String taker = getString(data, "taker");
        String lister = getString(data, "lister");
        String nftId = getString(data, "nft_id");
        Long paymentAmount = getLong(data, "payment_amount");
        Long feeAmount = getLong(data, "fee_amount");
        Long sellerAmount = getLong(data, "seller_amount");
        String coinType = getString(data, "coin_type");
        Long timestamp = event.getTimestampMs().longValue();
        
        tradesService.handleListingSaleFilled(event.getId().getTxDigest(), listingId, taker, lister, nftId, paymentAmount, feeAmount, sellerAmount, coinType, timestamp);
    }
    
    private void handleListingSwapFilled(OneChainEvent event) {
        if (isInvalid(event)) return;
        Map<String, Object> data = (Map<String, Object>) event.getParsedJson();
        
        String listingId = getString(data, "listing_id");
        String taker = getString(data, "taker");
        String lister = getString(data, "lister");
        String nftIdOut = getString(data, "nft_id_out");
        String nftIdIn = getString(data, "nft_id_in");
        Long timestamp = event.getTimestampMs().longValue();
        
        tradesService.handleListingSwapFilled(event.getId().getTxDigest(), listingId, taker, lister, nftIdOut, nftIdIn, timestamp);
    }
    
    private void handleListingCancelled(OneChainEvent event) {
        if (isInvalid(event)) return;
        Map<String, Object> data = (Map<String, Object>) event.getParsedJson();

        String listingId = getString(data, "listing_id");
        Long timestamp = event.getTimestampMs().longValue();
        
        listingsService.handleListingCancelled(event.getId().getTxDigest(), listingId, timestamp);
    }

    private void handleFeeUpdated(OneChainEvent event) {
        if (isInvalid(event)) return;
        Map<String, Object> data = (Map<String, Object>) event.getParsedJson();

        String feeConfigId = getString(data, "fee_config_id");
        Integer newFeeBps = getInt(data, "new_fee_bps");
        String newRecipient = getString(data, "new_recipient");
        Long timestamp = event.getTimestampMs().longValue();
        
        feeConfigUpdatesService.handleFeeUpdated(event.getId().getTxDigest(), feeConfigId, newFeeBps, newRecipient, timestamp);
    }

    private boolean isInvalid(OneChainEvent event) {
        return event.getParsedJson() == null || !(event.getParsedJson() instanceof Map);
    }
    
    private String getString(Map<String, Object> map, String key) {
        Object val = map.get(key);
        return val != null ? val.toString() : null;
    }
    
    private Integer getInt(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val == null) return null;
        try {
            return Integer.valueOf(val.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    private Long getLong(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val == null) return null;
        try {
            return Long.valueOf(val.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}