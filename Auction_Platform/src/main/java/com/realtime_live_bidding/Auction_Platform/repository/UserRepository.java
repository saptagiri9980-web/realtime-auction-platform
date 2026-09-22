package com.realtime_live_bidding.Auction_Platform.repository;

import com.realtime_live_bidding.Auction_Platform.entity.User; // FIXED: Must point to entity package
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}