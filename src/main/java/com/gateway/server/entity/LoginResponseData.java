package com.gateway.server.entity;

import com.gateway.server.dto.UserProfileDTO;

import lombok.Data;

@Data
public class LoginResponseData {

	private String tokenId;
	private UserProfileDTO userProfile;
}