package com.formulafund.portfolio.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ticker")
public class Ticker extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @ManyToOne
    @JoinColumn(name = "issuing_company_id")
	private IssuingCompany issuingCompany;
	

	private String symbol;
	
	@Enumerated(EnumType.STRING)
	private Exchange exchange;
	
	public Exchange getExchange() {
		return exchange;
	}
	public void setExchange(Exchange exchange) {
		this.exchange = exchange;
	}

	public IssuingCompany getIssuingCompany() {
		return issuingCompany;
	}
	public void setIssuingCompany(IssuingCompany issuingCompany) {
		this.issuingCompany = issuingCompany;
	}
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	@Override
	public String toString() {
		return "Ticker [issuingCompany=" + issuingCompany + ", symbol=" + symbol + ", exchange=" + exchange + ", id="
				+ id + "]";
	}
}
