package com.realtime_live_bidding.Auction_Platform.controller;

import com.realtime_live_bidding.Auction_Platform.dto.BidRequest;
import com.realtime_live_bidding.Auction_Platform.model.AuctionItem;
import com.realtime_live_bidding.Auction_Platform.service.AuctionService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller
public class AuctionWebSocketController {

    private final AuctionService auctionService;
    private final SimpMessagingTemplate messagingTemplate;

    public AuctionWebSocketController(AuctionService auctionService, SimpMessagingTemplate messagingTemplate) {
        this.auctionService = auctionService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/bid/{auctionId}")
    public void handleLiveBid(@DestinationVariable Long auctionId, BidRequest bidRequest, Principal principal) {
        try {
            // Extract bidder name from authenticated WebSocket Principal or fallback to request body / Anonymous
            String bidderName = (principal != null) 
                    ? principal.getName() 
                    : (bidRequest.getBidderName() != null ? bidRequest.getBidderName() : "Anonymous");

            // 1. Process the bid thread-safely in the database
            AuctionItem updatedAuction = auctionService.placeBid(auctionId, bidRequest, bidderName);

            // 2. Broadcast updated item state to all active subscribers
            messagingTemplate.convertAndSend("/topic/auctions", updatedAuction);

        } catch (Exception e) {
            // Send error message back to the user via WebSocket topic
            messagingTemplate.convertAndSend("/topic/errors", Map.of(
                "auctionId", auctionId,
                "message", e.getMessage()
            ));
        }
    }
}