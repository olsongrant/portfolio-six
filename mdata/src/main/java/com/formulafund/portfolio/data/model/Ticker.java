package com.formulafund.portfolio.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "ticker")
@Getter
@Setter
@Slf4j
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
	
	@Override
	public String toString() {
		return "Ticker [issuingCompany=" + issuingCompany + ", symbol=" + symbol + ", exchange=" + exchange + ", id="
				+ id + "]";
	}
}
