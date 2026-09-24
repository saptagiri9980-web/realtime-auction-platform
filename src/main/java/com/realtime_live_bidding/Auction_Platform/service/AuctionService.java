package com.realtime_live_bidding.Auction_Platform.service;

import com.realtime_live_bidding.Auction_Platform.dto.AuctionRequest;
import com.realtime_live_bidding.Auction_Platform.dto.BidRequest;
import com.realtime_live_bidding.Auction_Platform.model.AuctionItem;
import com.realtime_live_bidding.Auction_Platform.model.AuctionStatus;
import com.realtime_live_bidding.Auction_Platform.model.Bid;
import com.realtime_live_bidding.Auction_Platform.repository.AuctionItemRepository;
import com.realtime_live_bidding.Auction_Platform.repository.BidRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionService {

    private final AuctionItemRepository auctionRepository;
    private final BidRepository bidRepository;

    public AuctionService(AuctionItemRepository auctionRepository, BidRepository bidRepository) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
    }

    public List<AuctionItem> getAllActiveAuctions() {
        return auctionRepository.findByStatus(AuctionStatus.ACTIVE);
    }

    public AuctionItem getAuctionById(Long id) {
        return auctionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found with ID: " + id));
    }

    public List<Bid> getBidHistory(Long auctionId) {
        return bidRepository.findByAuctionItemIdOrderByTimestampDesc(auctionId);
    }

    public AuctionItem createAuction(AuctionRequest request) {
        AuctionItem item = new AuctionItem();
        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setStartingPrice(request.getStartingPrice());
        item.setCurrentHighestBid(request.getStartingPrice());
        item.setHighestBidder("No Bids Yet");
        item.setEndTime(LocalDateTime.now().plusMinutes(
                request.getDurationInMinutes() != null ? request.getDurationInMinutes() : 60
        ));
        item.setStatus(AuctionStatus.ACTIVE);

        return auctionRepository.save(item);
    }

    @Transactional
    public AuctionItem placeBid(Long auctionId, BidRequest bidRequest, String bidderName) {
        AuctionItem auction = auctionRepository.findByIdWithLock(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found with ID: " + auctionId));

        // 1. Verify auction is ACTIVE
        if (auction.getStatus() != AuctionStatus.ACTIVE) {
            throw new IllegalStateException("Cannot place bid on a closed auction.");
        }

        // 2. Verify auction has not expired
        if (LocalDateTime.now().isAfter(auction.getEndTime())) {
            auction.setStatus(AuctionStatus.CLOSED);
            auctionRepository.save(auction);
            throw new IllegalStateException("Auction time has expired.");
        }

        // 3. Retrieve and convert bid amount
        Double rawBidAmount = bidRequest.getAmount();
        if (rawBidAmount == null) {
            throw new IllegalArgumentException("Bid amount must not be empty.");
        }

        BigDecimal newBidAmount = BigDecimal.valueOf(rawBidAmount);

        // 4. Verify bid amount is strictly higher than current highest bid
        if (newBidAmount.compareTo(auction.getCurrentHighestBid()) <= 0) {
            throw new IllegalArgumentException("Bid amount must be strictly higher than current highest bid of $" 
                    + auction.getCurrentHighestBid());
        }

        // 5. Update Auction Item state
        auction.setCurrentHighestBid(newBidAmount);
        auction.setHighestBidder(bidderName);
        AuctionItem updatedAuction = auctionRepository.save(auction);

        // 6. Record the Bid log
        Bid bid = new Bid();
        bid.setAuctionItemId(auctionId);
        bid.setBidderName(bidderName);
        bid.setBidAmount(newBidAmount);
        bid.setTimestamp(LocalDateTime.now());
        
        bidRepository.save(bid);

        return updatedAuction;
    }
}