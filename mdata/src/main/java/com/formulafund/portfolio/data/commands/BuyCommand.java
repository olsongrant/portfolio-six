package com.formulafund.portfolio.data.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BuyCommand {
	private String symbol;
	private String accountId;
	private Float shareQuantity;

	public void setAccountId(Long aLongId) {
		this.accountId = aLongId.toString();
	}
	public void setAccountId(String anId) {
		this.accountId = anId;
	}
}
