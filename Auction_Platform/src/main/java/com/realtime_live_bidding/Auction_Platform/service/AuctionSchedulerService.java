package com.realtime_live_bidding.Auction_Platform.service;

import com.realtime_live_bidding.Auction_Platform.model.AuctionItem;
import com.realtime_live_bidding.Auction_Platform.model.AuctionStatus;
import com.realtime_live_bidding.Auction_Platform.repository.AuctionItemRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionSchedulerService {

    private final AuctionItemRepository auctionRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public AuctionSchedulerService(AuctionItemRepository auctionRepository, SimpMessagingTemplate messagingTemplate) {
        this.auctionRepository = auctionRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void closeExpiredAuctions() {
        List<AuctionItem> expiredAuctions = auctionRepository.findByStatusAndEndTimeBefore(
                AuctionStatus.ACTIVE, 
                LocalDateTime.now()
        );

        for (AuctionItem auction : expiredAuctions) {
            auction.setStatus(AuctionStatus.CLOSED);
            auctionRepository.save(auction);

            // Broadcast closed status to connected WebSocket clients
            messagingTemplate.convertAndSend("/topic/auctions", auction);
        }
    }
}