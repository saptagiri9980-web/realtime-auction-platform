package com.realtime_live_bidding.Auction_Platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AuctionPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuctionPlatformApplication.class, args);
    }
}