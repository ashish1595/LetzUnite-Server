package com.gateway.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenRequestData {

	private String emailId;
	private String mobileNumber;
	private String deviceId;
	private String tokenId;
	
}