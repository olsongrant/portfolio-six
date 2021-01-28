package com.formulafund.portfolio.data.commands;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SaleCommand {
	
	@NotEmpty
	private String symbol;
	private String accountId;
	
	@NotNull
	@Digits(fraction=3, integer=4)
	private Float saleQuantity;
	
	private Float currentQuantity;

	public void setAccountId(Long aLongId) {
		this.accountId = aLongId.toString();
	}
	public void setAccountId(String anId) {
		this.accountId = anId;
	}
}
