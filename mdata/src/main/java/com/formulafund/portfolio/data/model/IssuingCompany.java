package com.formulafund.portfolio.data.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class IssuingCompany extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fullName;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "issuingCompany")
	private Set<Ticker> tickers = new HashSet<>();

	public Set<Ticker> getTickers() {
		return tickers;
	}

	public void setTickers(Set<Ticker> tickers) {
		this.tickers = tickers;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "IssuingCompany [fullName=" + fullName + ", id=" + id + "]";
	}
	
}
