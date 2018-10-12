package com.dunya.stakechannel.accountsconsumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dunya.stakechannel.accountsconsumer.config.AccountsConsumerConfiguration;
import com.dunya.stakechannel.accountsconsumer.model.Account;
import com.dunya.stakechannel.accountsconsumer.model.AccountName;
import com.dunya.stakechannel.accountsconsumer.model.ErrorResponse;

@Component
public class AccountOperations {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	AccountsConsumerConfiguration accountsConfig;

	public ResponseEntity<ErrorResponse> saveAccount(Account account) {
		return restTemplate.postForEntity(accountsConfig.getAccountsServiceUrl(), account, ErrorResponse.class);
	}

	public ResponseEntity<ErrorResponse> delegateAccount(AccountName resource) {

		return restTemplate.postForEntity(accountsConfig.getDelegateServiceUrl(), resource, ErrorResponse.class);
	}
}
