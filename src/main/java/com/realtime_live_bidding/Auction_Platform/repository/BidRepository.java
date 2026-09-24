package com.realtime_live_bidding.Auction_Platform.repository;

    import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.stereotype.Repository;

     import com.realtime_live_bidding.Auction_Platform.model.Bid;

     import java.util.List;

	@Repository
	public interface BidRepository extends JpaRepository<Bid, Long> {

	    // Retrieve full bidding history for a specific item ordered by newest first
	    List<Bid> findByAuctionItemIdOrderByTimestampDesc(Long auctionItemId);
	}


