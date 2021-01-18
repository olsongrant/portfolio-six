package com.formulafund.portfolio.data.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

//@Entity
//@Getter
//@Setter
//@Slf4j
public class League extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String leagueName;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "league")
	private Set<Account> tickers = new HashSet<>();

	@Override
	public String toString() {
		return "League [leagueName=" + leagueName + "]";
	}


}
