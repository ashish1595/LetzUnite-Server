package com.gateway.server.dto;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "token")
public class TokenDTO {

	@Id
	private String _id;
	private String userId;
	private String deviceId;
	private String tokenId;
	private Date expiryTime;
	private Object additionalInfo;
	private Integer isDeleted;
}