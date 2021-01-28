package com.formulafund.portfolio.data.commands;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class DeleteAccountCommand {
	private String accountId;
	private String accountName;
	private boolean acknowledged;
	private String userId;
}
