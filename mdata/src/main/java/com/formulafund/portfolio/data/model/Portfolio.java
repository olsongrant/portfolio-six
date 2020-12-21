package com.formulafund.portfolio.data.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Portfolio extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @ManyToOne
    @JoinColumn(name = "account_id")
	private Account account;
	private String name;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "portfolio")
	private Set<Transaction> transactions = new HashSet<>();
	
	
	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

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

	@Transient
	private transient Set<StockHolding> holdings = new HashSet<>();

	@Override
	public String toString() {
		return "Portfolio [account=" + account + ", holdings=" /* + holdings */ + ", id=" + id + "]";
	}
	
	

}
