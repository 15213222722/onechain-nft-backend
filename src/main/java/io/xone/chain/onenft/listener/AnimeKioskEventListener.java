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
import io.xone.chain.onenft.service.IKioskCreateEventService;
import io.xone.chain.onenft.service.INftPlacedEventService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AnimeKioskEventListener implements CommandLineRunner {

	private final OneChain oneChain;

	private final OneChainProperties properties;

	private final IKioskCreateEventService kioskCreateEventService;

	private final INftPlacedEventService nftPlacedEventService;

	public AnimeKioskEventListener(OneChain oneChain, OneChainProperties properties,
			IKioskCreateEventService kioskCreateEventService, INftPlacedEventService nftPlacedEventService) {
		this.oneChain = oneChain;
		this.properties = properties;
		this.kioskCreateEventService = kioskCreateEventService;
		this.nftPlacedEventService = nftPlacedEventService;
	}

	@Override
	public void run(String... args) throws Exception {
		subscribeKioskPlace();
		subscribeKioskCreated();
	}

	@Scheduled(fixedDelay = 5000)
	private void subscribeKioskPlace() {
		log.info("Subscribing to kiosk::place(kiosk, cap, nft)");
		try {
			String packageId = properties.getContract().getPackageId();
			String moduleName = properties.getContract().getModuleName();
			String eventType = String.format("%s::%s::NFTPlaced", packageId, moduleName);
			EventFilter.MoveEventTypeEventFilter filter = new EventFilter.MoveEventTypeEventFilter();
			filter.setMoveEventType(eventType);
			PaginatedEvents page = this.oneChain.queryEvents(filter, null, 10, true).get();

			if (page.getData() != null && !page.getData().isEmpty()) {
				for (OneChainEvent event : page.getData()) {
					log.info("Processing NFTPlaced event: {}", event);
					handleNFTPlacedEvent(event);
				}
			}

			log.info("Subscribed to kiosk::place");
		} catch (Exception e) {
			log.error("Failed to subscribe to kiosk::place", e);
		}
	}

	private void handleNFTPlacedEvent(OneChainEvent event) {
		String txHash = event.getId().getTxDigest();
		String walletAddress = event.getSender();
		String eventType = event.getType();
		String kioskId = null;
		String nftObjectId = null;
		Long timestampMs = event.getTimestampMs() != null ? event.getTimestampMs().longValue() : null;

		if (event.getParsedJson() != null && event.getParsedJson() instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> data = (Map<String, Object>) event.getParsedJson();
			if (data.containsKey("kiosk_id")) {
				kioskId = data.get("kiosk_id").toString();
			}
			if (data.containsKey("nft_id")) {
				nftObjectId = data.get("nft_id").toString();
			}
		}

		nftPlacedEventService.handleNFTPlacedEvent(txHash, walletAddress, eventType, kioskId, nftObjectId, timestampMs);
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

			PaginatedEvents page = this.oneChain.queryEvents(filter, null, 10, true).get();

			if (page.getData() != null && !page.getData().isEmpty()) {
				for (OneChainEvent event : page.getData()) {
					log.info("Processing KioskCreated event: {}", event);
					handleKioskCreatedEvent(event);
				}
			}
		} catch (Exception e) {
			log.error("Failed to subscribe to KioskCreated", e);
		}
	}

	private void handleKioskCreatedEvent(OneChainEvent event) {
		String txHash = event.getId().getTxDigest();
		String walletAddress = event.getSender();
		String eventType = event.getType();
		String kioskId = null;
		String capId = null;
		Long timestampMs = event.getTimestampMs() != null ? event.getTimestampMs().longValue() : null;

		if (event.getParsedJson() != null && event.getParsedJson() instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> data = (Map<String, Object>) event.getParsedJson();
			if (data.containsKey("kiosk_id")) {
				kioskId = data.get("kiosk_id").toString();
			}
			if (data.containsKey("cap_id")) {
				capId = data.get("cap_id").toString();
			}
		}

		kioskCreateEventService.handleKioskCreatedEvent(txHash, walletAddress, eventType, kioskId, capId, timestampMs);
	}

}