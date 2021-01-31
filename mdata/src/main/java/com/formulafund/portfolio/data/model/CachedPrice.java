package com.formulafund.portfolio.data.model;

import java.time.LocalDateTime;
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
public class CachedPrice extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tickerSymbol;
	private Float latestPrice;
	private LocalDateTime timestamp;
	@Override
	public String toString() {
		return "CachedPrice [tickerSymbol=" + tickerSymbol + ", latestPrice=" + latestPrice + ", timestamp=" + timestamp
				+ "]";
	}
	
}
