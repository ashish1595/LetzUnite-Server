package com.gateway.server.entity;

import lombok.Data;

@Data
public class LoginRequestData {

	private String emailId;
	private String password;
	private String deviceId;
	private Object additionalInfo;
}