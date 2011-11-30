package com.greengar.hackathon.app.network;

public class ErrorData {
	public static final String ERROR_MESSAGE_GENERAL = "There's error when retrieving data from the server. Please try again later.";
	
	public String errorMessage;
	
	public ErrorData() {
		this.errorMessage = ERROR_MESSAGE_GENERAL;
	}
	
	public ErrorData(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
