package com.formulafund.portfolio.data.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Setter
@Slf4j
public class IssuingCompany extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fullName;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "issuingCompany")
	private Set<Ticker> tickers = new HashSet<>();

	@Override
	public String toString() {
		return "IssuingCompany [fullName=" + fullName + ", id=" + id + "]";
	}
	
}
