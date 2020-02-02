package com.gateway.server.entity;

import org.json.JSONObject;

import lombok.Data;

@Data
public class UserProfile {

	private String id;
	private String name;
	private String emailId;
	private String mobileNumber;
	private String password;
	private JSONObject additionalInfo;
}
