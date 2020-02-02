package com.gateway.server.exception;

public enum ResponseCode {

	USER_CREDENTIALS_MISSING_OR_INVALID_IN_REQUEST(4001,"Your request could not be served by the system. Please try again!"),
	USER_DATA_NOT_FOUND_IN_REQUEST(4002,"Your request could not be served by the system. Please try again!"),
	USER_DATA_NOT_FOUND_IN_RESPONSE(4003,"No data found"),
	PROPERTIES_NOT_FOUND(4004,"Your request could not be served by the system. Please try again!"),
	INVALID_PARAMETER(4005,"Your request could not be served by the system. Please try again!"),
	PARAMETER_NOT_FOUND(4006,"Your request could not be served by the system. Please try again!"),
	MISSING_HEADER_KEY(4007,"Your request could not be served by the system. Please try again!"),
	ACCOUNT_ALREADY_EXISTS(4008,"Your account already exists!"),
	INTERNAL_SERVER_ERROR(5000,"Your request could not be served by the system. Please try again!"),
	DB_ERROR(5001,"Your request could not be served by the system. Please try again!"),
	SYSTEM_ERROR(5002,"System error occured"),
	UNAUTHORIZED(5003,"Your session has expired"),
	SERVER_ERROR(513,"Your request could not be served by the system. Please try again!"),
	GENRAL_ERROR(5010,"Your request could not be served by the system. Please try again!"),
	CACHING_PROPERTIES_EXCEPTION(5017,"Your request could not be served by the system. Please try again!"), 
	REQUEST_FLOODING(8000,"Multiple request with same data. Please try again!"),
	USER_NEEDS_SIGNUP(8001,"Currently no account associated with the Email. Please signup!"),
	INVALID_PASSWORD(8003,"Password entered is incorrect. Please try again!"),
	ERROR_DECRYPTING_DATA(8004,"Error reading the request decrypted payload"),
	TOKEN_EXPIRED(8005,"Authentication token has expired. Please login again!"),
	VERIFY_EMAIL(8006,"Please verify your email to activate the account!");

	private int code;
	private String description;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	private ResponseCode(int code, String description) {
		this.code = code;
		this.description = description;
	}

}