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
public class BuyCommand {
	
	@NotEmpty
	private String symbol;
	private String accountId;
	
	@NotNull
	@Digits(fraction=3, integer=4)
	private Float shareQuantity;

	public void setAccountId(Long aLongId) {
		this.accountId = aLongId.toString();
	}
	public void setAccountId(String anId) {
		this.accountId = anId;
	}
	
	public static BuyCommand with(Long anAccountId, String aSymbol, Float aQuantity) {
		BuyCommand cmd = new BuyCommand();
		cmd.setAccountId(anAccountId);
		cmd.setSymbol(aSymbol);
		cmd.setShareQuantity(aQuantity);
		return cmd;
	}
}
