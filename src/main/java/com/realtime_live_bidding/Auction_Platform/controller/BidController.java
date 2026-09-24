package com.realtime_live_bidding.Auction_Platform.controller;
    import com.realtime_live_bidding.Auction_Platform.dto.BidRequest;
	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;
	import org.springframework.security.core.Authentication;
	import org.springframework.web.bind.annotation.*;

	@RestController
	@RequestMapping("/api/bids")
	@CrossOrigin(origins = "*")
	public class BidController {

	    @PostMapping("/place")
	    public ResponseEntity<?> placeBid(@RequestBody BidRequest request, Authentication authentication) {
	        try {
	            if (authentication == null || !authentication.isAuthenticated()) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
	            }

	            String currentUsername = authentication.getName();
	            
	            // Log received data to console
	            System.out.println("Received bid from: " + currentUsername + 
	                               " | Item ID: " + request.getAuctionItemId() + 
	                               " | Amount: " + request.getAmount());

	            // Add your repository save / validation logic here

	            return ResponseEntity.ok("Bid placed successfully by " + currentUsername);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Error placing bid: " + e.getMessage());
	        }
	    }
	}


