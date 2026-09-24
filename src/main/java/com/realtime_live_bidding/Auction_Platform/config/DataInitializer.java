package com.realtime_live_bidding.Auction_Platform.config;

import com.realtime_live_bidding.Auction_Platform.dto.AuctionRequest;
import com.realtime_live_bidding.Auction_Platform.service.AuctionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final AuctionService auctionService;

    // Explicit constructor injection (Doesn't require Lombok)
    public DataInitializer(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @Override
    public void run(String... args) {
        AuctionRequest item1 = new AuctionRequest(
                "Vintage Mechanical Watch (1972)",
                "Rare Swiss vintage chronograph watch in fully working condition.",
                new BigDecimal("250.00"),
                120
        );

        AuctionRequest item2 = new AuctionRequest(
                "Cyberpunk Mechanical Keyboard",
                "Custom RGB hot-swappable mechanical keyboard with lubricated switches.",
                new BigDecimal("95.00"),
                60
        );

        AuctionRequest item3 = new AuctionRequest(
                "Signed First Edition Sci-Fi Novel",
                "Collector's hardcover edition signed directly by the author.",
                new BigDecimal("150.00"),
                180
        );

        auctionService.createAuction(item1);
        auctionService.createAuction(item2);
        auctionService.createAuction(item3);
    }
}