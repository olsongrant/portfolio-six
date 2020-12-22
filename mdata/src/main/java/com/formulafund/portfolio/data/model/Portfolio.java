package com.formulafund.portfolio.data.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Setter
@Slf4j
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

	@Transient
	private transient Set<StockHolding> holdings = new HashSet<>();

	@Override
	public String toString() {
		return "Portfolio [account=" + account + ", holdings=" /* + holdings */ + ", id=" + id + "]";
	}
	
	

}
