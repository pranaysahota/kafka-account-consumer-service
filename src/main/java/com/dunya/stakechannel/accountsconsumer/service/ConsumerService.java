package com.dunya.stakechannel.accountsconsumer.service;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

import com.dunya.stakechannel.accountsconsumer.exception.AccountCreationMonitorSvcException;
import com.dunya.stakechannel.accountsconsumer.model.Account;
import com.dunya.stakechannel.accountsconsumer.model.AccountName;
import com.dunya.stakechannel.accountsconsumer.model.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ConsumerService {
	private Logger log = LogManager.getLogger(this.getClass().getName());
	@Autowired
	private AccountOperations accountOperations;

	@Autowired
	private ObjectMapper objectMapper;

	private CountDownLatch cdl1 = new CountDownLatch(2);
	private CountDownLatch cdl2 = new CountDownLatch(2);

	@KafkaListener(topicPartitions = { @TopicPartition(topic = "accounts", partitions = { "0" }) })
	public void consumeMessageFromPartition0(String accountJson) {
		log.debug("Received record from Partition 0: " + accountJson);
		Account account;
		try {
			account = objectMapper.readValue(accountJson, Account.class);
			consumeAccountAndCallServices(account);
		} catch (IOException e) {
			log.error("Exception caught while serializating consumer record, Trace: ", e.getMessage());
		}
		cdl1.countDown();
	}

	@KafkaListener(topicPartitions = { @TopicPartition(topic = "accounts", partitions = { "1" }) })
	public void consumeMessageFromPartition1(String accountJson) {
		log.debug("Received record from Partition 1: " + accountJson);
		Account account;
		try {
			account = objectMapper.readValue(accountJson, Account.class);
			consumeAccountAndCallServices(account);
		} catch (IOException e) {
			log.error("Exception caught while serializating consumer record, Trace: ", e.getMessage());
		}
		cdl2.countDown();
	}

	private void consumeAccountAndCallServices(Account account) {
		ResponseEntity<ErrorResponse> serviceResponse;
		ErrorResponse errorResponse = new ErrorResponse();
		try {
			serviceResponse = accountOperations.saveAccount(account);
			if (!serviceResponse.getStatusCode().equals(HttpStatus.OK)) {
				errorResponse = serviceResponse.getBody();
				log.error("Error in getting a response from account-service, error response {}",
						errorResponse.toString());
				throw new AccountCreationMonitorSvcException(errorResponse.getErrorMessage());
			}
			log.info("Successfully called account-service for account: " + account.getAccountName());
			AccountName accountName = new AccountName();
			accountName.setAccountName(account.getAccountName());
			serviceResponse = accountOperations.delegateAccount(accountName);
			if (!serviceResponse.getStatusCode().equals(HttpStatus.OK)) {
				errorResponse = serviceResponse.getBody();
				log.error("Error in getting a response from delegation-service, error response {}",
						errorResponse.toString());
				throw new AccountCreationMonitorSvcException("Exception caught while calling delegate-service");
			}
			log.info("Successfully called delegation-service for account: " + account.getAccountName());
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer();
			sb.append("Exception in account-creation-monitor-service with message: ").append(errorResponse.toString())
					.append("Exception trace: {}");
			log.error(sb.toString(), e);
		}
	}
}
