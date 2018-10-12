package com.dunya.stakechannel.accountsconsumer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountName {
	@JsonProperty("account")
	private String accountName;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

}
