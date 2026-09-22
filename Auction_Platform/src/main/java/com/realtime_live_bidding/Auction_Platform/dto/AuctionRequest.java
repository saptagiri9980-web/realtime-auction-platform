package com.realtime_live_bidding.Auction_Platform.dto;

import java.math.BigDecimal;

public class AuctionRequest {
    private String title;
    private String description;
    private BigDecimal startingPrice;
    private Integer durationInMinutes;

    public AuctionRequest() {
    }

    public AuctionRequest(String title, String description, BigDecimal startingPrice, Integer durationInMinutes) {
        this.title = title;
        this.description = description;
        this.startingPrice = startingPrice;
        this.durationInMinutes = durationInMinutes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }
}