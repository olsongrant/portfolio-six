package com.formulafund.portfolio.data.view;

public class HoldingView implements Comparable<HoldingView> {
	private String accountName;
	private String userName;
	private String tickerSymbol;
	private String companyName;
	private Float shareQuantity;
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTickerSymbol() {
		return tickerSymbol;
	}
	public void setTickerSymbol(String tickerSymbol) {
		this.tickerSymbol = tickerSymbol;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Float getShareQuantity() {
		return shareQuantity;
	}
	public void setShareQuantity(Float shareQuantity) {
		this.shareQuantity = shareQuantity;
	}
	
	public boolean isFilledIn() {
		if (this.accountName == null) return false;
		if (this.userName == null) return false;
		if (this.tickerSymbol == null) return false;
		if (this.companyName == null) return false;
		if (this.shareQuantity == null) return false;
		return true;
	}
	public HoldingView deepCopy() {
		HoldingView another = new HoldingView();
		another.setAccountName(this.accountName);
		another.setCompanyName(this.getCompanyName());
		another.setShareQuantity(this.getShareQuantity());
		another.setTickerSymbol(this.getTickerSymbol());
		another.setUserName(this.getUserName());
		return another;
	}
	
	@Override
	public String toString() {
		return "HoldingView [accountName=" + accountName + ", userName=" + userName + 
				", tickerSymbol=" + tickerSymbol + ", companyName=" + companyName + ", shareQuantity=" + shareQuantity
				+ "]";
	}
	@Override
	public int compareTo(HoldingView o) {
		if (o == null) return 1;
		if (o.getUserName() == null) return 1;
		if (!this.isFilledIn()) return -1;
		int comparison = this.getUserName().compareTo(o.getUserName());
		if (comparison != 0) return comparison;
		comparison = this.getAccountName().compareTo(o.getAccountName());
		if (comparison != 0) return comparison;
		return this.getTickerSymbol().compareTo(o.getTickerSymbol());
	}
	

}
