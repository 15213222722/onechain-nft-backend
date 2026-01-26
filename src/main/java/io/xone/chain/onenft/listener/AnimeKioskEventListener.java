package io.xone.chain.onenft.listener;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.onechain.OneChain;
import io.onechain.models.events.EventFilter;
import io.onechain.models.events.OneChainEvent;
import io.onechain.models.events.PaginatedEvents;
import io.xone.chain.onenft.common.utils.ChainUtils;
import io.xone.chain.onenft.config.OneChainProperties;
import io.xone.chain.onenft.service.IKioskCreateEventService;
import io.xone.chain.onenft.service.INftListingEventService;
import io.xone.chain.onenft.service.INftPlacedEventService;
import io.xone.chain.onenft.service.INftTakenEventService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AnimeKioskEventListener implements CommandLineRunner {

	private final OneChain oneChain;

	private final OneChainProperties properties;

	private final IKioskCreateEventService kioskCreateEventService;

	private final INftPlacedEventService nftPlacedEventService;

	private final INftTakenEventService nftTakenEventService;

	private final INftListingEventService nftListingEventService;

	public AnimeKioskEventListener(OneChain oneChain, OneChainProperties properties,
			IKioskCreateEventService kioskCreateEventService, INftPlacedEventService nftPlacedEventService,
			INftTakenEventService nftTakenEventService, INftListingEventService nftListingEventService) {
		this.oneChain = oneChain;
		this.properties = properties;
		this.kioskCreateEventService = kioskCreateEventService;
		this.nftPlacedEventService = nftPlacedEventService;
		this.nftTakenEventService = nftTakenEventService;
		this.nftListingEventService = nftListingEventService;
	}

	@Override
	public void run(String... args) throws Exception {
		subscribeKioskCreated();
		subscribeNFTTaken();
		subscribeKioskPlace();
		subscribeKioskList();
	}

	@Scheduled(fixedDelay = 5000)
	private void subscribeKioskPlace() {
		fetchAndProcessEvents("NFTPlaced", this::handleNFTPlacedEvent);
	}

	@Scheduled(fixedDelay = 5000)
	private void subscribeKioskCreated() {
		fetchAndProcessEvents("KioskCreated", this::handleKioskCreatedEvent);
	}

	@Scheduled(fixedDelay = 5000)
	private void subscribeNFTTaken() {
		fetchAndProcessEvents("NFTTaken", this::handleNFTTakenEvent);
	}

	@Scheduled(fixedDelay = 5000)
	private void subscribeKioskList() {
		fetchAndProcessEvents("NFTListed", this::handleNFTListingEvent);
	}

	private void fetchAndProcessEvents(String eventSuffix, java.util.function.Consumer<OneChainEvent> handler) {
		log.info("Polling for {} event", eventSuffix);
		try {
			String packageId = properties.getContract().getPackageId();
			String moduleName = properties.getContract().getModuleName();
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
		log.info("Handling KioskCreated event: txHash={}, walletAddress={}, kioskId={}, capId={}", txHash, walletAddress, kioskId, capId);
		kioskCreateEventService.handleKioskCreatedEvent(txHash, walletAddress, eventType, kioskId, capId, timestampMs);
	}

	private void handleNFTTakenEvent(OneChainEvent event) {
		String txHash = event.getId().getTxDigest();
		String walletAddress = event.getSender();
		String eventType = event.getType();
		String kioskId = null;
		String nftId = null;
		Long timestampMs = event.getTimestampMs() != null ? event.getTimestampMs().longValue() : null;

		if (event.getParsedJson() != null && event.getParsedJson() instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> data = (Map<String, Object>) event.getParsedJson();
			if (data.containsKey("kiosk_id")) {
				kioskId = data.get("kiosk_id").toString();
			}
			if (data.containsKey("nft_id")) {
				nftId = data.get("nft_id").toString();
			}
		}

		nftTakenEventService.handleNFTTakenEvent(txHash, walletAddress, eventType, kioskId, nftId, timestampMs);
	}

	private void handleNFTListingEvent(OneChainEvent event) {
		String txHash = event.getId().getTxDigest();
		String walletAddress = event.getSender();
		String eventType = event.getType();
		String kioskId = null;
		String nftId = null;
		BigDecimal price = null;
		Long timestampMs = event.getTimestampMs() != null ? event.getTimestampMs().longValue() : null;

		if (event.getParsedJson() != null && event.getParsedJson() instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> data = (Map<String, Object>) event.getParsedJson();
			if (data.containsKey("kiosk_id")) {
				kioskId = data.get("kiosk_id").toString();
			}
			if (data.containsKey("nft_id")) {
				nftId = data.get("nft_id").toString();
			}
			if (data.containsKey("price")) {
				price = ChainUtils.parsePrice(data.get("price"));
			}
		}

		nftListingEventService.handleNFTListingEvent(txHash, walletAddress, eventType, kioskId, nftId, price, timestampMs);
	}

}