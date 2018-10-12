package com.dunya.stakechannel.accountsconsumer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {
	@JsonProperty("error_code")
	private int errorCode=0;
	@JsonProperty("error_msg")
	private String errorMessage="";

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Error Code: ").append(this.errorCode).append(", Error message: ").append(this.errorMessage);
		return sb.toString();
	}
}