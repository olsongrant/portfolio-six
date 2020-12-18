package com.formulafund.portfolio.data.model;

import java.util.HashSet;
import java.util.Set;


public class Portfolio extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Account account;
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Set<StockHolding> getHoldings() {
		return holdings;
	}

	public void setHoldings(Set<StockHolding> holdings) {
		this.holdings = holdings;
	}

	private Set<StockHolding> holdings = new HashSet<>();

	@Override
	public String toString() {
		return "Portfolio [account=" + account + ", holdings=" + holdings + ", id=" + id + "]";
	}
	
	

}
