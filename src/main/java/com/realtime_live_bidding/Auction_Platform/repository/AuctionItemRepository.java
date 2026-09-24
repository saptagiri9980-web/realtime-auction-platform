package com.realtime_live_bidding.Auction_Platform.repository;

import com.realtime_live_bidding.Auction_Platform.model.AuctionItem;
import com.realtime_live_bidding.Auction_Platform.model.AuctionStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionItemRepository extends JpaRepository<AuctionItem, Long> {

    List<AuctionItem> findByStatus(AuctionStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM AuctionItem a WHERE a.id = :id")
    Optional<AuctionItem> findByIdWithLock(@Param("id") Long id);

    List<AuctionItem> findByStatusAndEndTimeBefore(AuctionStatus status, LocalDateTime now);
}