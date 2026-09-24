package com.realtime_live_bidding.Auction_Platform.dto;

public class BidRequest {
    private Long auctionItemId;
    private Double amount;

    public BidRequest() {}

    public BidRequest(Long auctionItemId, Double amount) {
        this.auctionItemId = auctionItemId;
        this.amount = amount;
    }

    public Long getAuctionItemId() {
        return auctionItemId;
    }

    public void setAuctionItemId(Long auctionItemId) {
        this.auctionItemId = auctionItemId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

	public String getBidderName() {
		// TODO Auto-generated method stub
		return null;
	}
}