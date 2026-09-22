package com.realtime_live_bidding.Auction_Platform.controller;

import com.realtime_live_bidding.Auction_Platform.dto.AuctionRequest;
import com.realtime_live_bidding.Auction_Platform.dto.BidRequest;
import com.realtime_live_bidding.Auction_Platform.model.AuctionItem;
import com.realtime_live_bidding.Auction_Platform.model.Bid;
import com.realtime_live_bidding.Auction_Platform.service.AuctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auctions")
@CrossOrigin(origins = "*") // Allows local Vanilla JS frontend connections
public class AuctionController {

    private final AuctionService auctionService;

    // Explicit constructor injection
    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping
    public ResponseEntity<List<AuctionItem>> getAllActiveAuctions() {
        return ResponseEntity.ok(auctionService.getAllActiveAuctions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionItem> getAuctionById(@PathVariable Long id) {
        return ResponseEntity.ok(auctionService.getAuctionById(id));
    }

    @GetMapping("/{id}/bids")
    public ResponseEntity<List<Bid>> getBidHistory(@PathVariable Long id) {
        return ResponseEntity.ok(auctionService.getBidHistory(id));
    }

    @PostMapping
    public ResponseEntity<AuctionItem> createAuction(@RequestBody AuctionRequest request) {
        return ResponseEntity.ok(auctionService.createAuction(request));
    }

    @PostMapping("/{id}/bids")
    public ResponseEntity<AuctionItem> placeBid(
            @PathVariable Long id, 
            @RequestBody BidRequest request,
            Authentication authentication) {
        
        // Extract authenticated username from JWT, or fallback to request body/Anonymous
        String bidderName = (authentication != null && authentication.isAuthenticated()) 
                ? authentication.getName() 
                : (request.getBidderName() != null ? request.getBidderName() : "Anonymous");

        AuctionItem updatedAuction = auctionService.placeBid(id, request, bidderName);
        return ResponseEntity.ok(updatedAuction);
    }
}