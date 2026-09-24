package com.realtime_live_bidding.Auction_Platform.service;

import com.realtime_live_bidding.Auction_Platform.model.AuctionItem;
import com.realtime_live_bidding.Auction_Platform.model.AuctionStatus;
import com.realtime_live_bidding.Auction_Platform.repository.AuctionItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableScheduling
public class AuctionScheduler {

    private static final Logger log = LoggerFactory.getLogger(AuctionScheduler.class);

    private final AuctionItemRepository auctionRepository;

    // Explicit constructor injection (Fixes Lombok uninitialized field error)
    public AuctionScheduler(AuctionItemRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    // Runs every 5000ms (5 seconds)
    @Scheduled(fixedRate = 5000)
    public void closeExpiredAuctions() {
        LocalDateTime now = LocalDateTime.now();
        List<AuctionItem> expiredAuctions = auctionRepository
                .findByStatusAndEndTimeBefore(AuctionStatus.ACTIVE, now);

        for (AuctionItem auction : expiredAuctions) {
            auction.setStatus(AuctionStatus.CLOSED);
            auctionRepository.save(auction);
            log.info("Auction ID {} ('{}') has been automatically CLOSED.", auction.getId(), auction.getTitle());
        }
    }
}