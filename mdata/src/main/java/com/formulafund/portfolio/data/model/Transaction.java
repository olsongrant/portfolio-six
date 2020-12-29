package com.formulafund.portfolio.data.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Setter
@Slf4j
public class Transaction extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	@OneToOne
	private Ticker ticker;
	private Float shareQuantity;
	private LocalDateTime transactionDateTime;
	
	@ManyToOne
    @JoinColumn(name = "account_id")
	private Account account;
    
    @Enumerated(EnumType.STRING)
	private TransactionType transactionType;
	
	public static Transaction purchaseOf(Ticker aTicker, LocalDateTime aPurchaseDate, Account anAccount, Float quantity) {
		Transaction p = untypedTransactionWith(aTicker, aPurchaseDate, anAccount, quantity);
		p.setTransactionType(TransactionType.PURCHASE);
		return p;
	}
	
	public static Transaction saleOf(Ticker aTicker, LocalDateTime aSaleDate, Account anAccount, Float quantity) {
		Transaction s = Transaction.untypedTransactionWith(aTicker, aSaleDate, anAccount, quantity);
		s.setTransactionType(TransactionType.SALE);
		return s;
	}
	@Override
	public String toString() {
		return "Transaction [ticker=" + ticker + ", shareQuantity=" + shareQuantity + ", transactionDateTime="
				+ transactionDateTime + ", account=" + account + ", transactionType=" + transactionType + ", id="
				+ id + "]";
	}
	public static Transaction untypedTransactionWith(Ticker aTicker, LocalDateTime aPurchaseDate, Account anAccount,
			Float quantity) {
		Transaction p = new Transaction();
		p.setTicker(aTicker);
		p.setTransactionDateTime(aPurchaseDate);
		p.setAccount(anAccount);
		p.setShareQuantity(quantity);
		return p;
	}

}
