package com.gateway.server.dto;

import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Document(collection = "userProfile")
public class UserProfileDTO {
	
	@Id
    private String _id;
	private String name;
	private String emailId;
	private String mobileNumber;
	@JsonIgnore
	private String password;
	private Integer status;
	private JSONObject additionalInfo;
}