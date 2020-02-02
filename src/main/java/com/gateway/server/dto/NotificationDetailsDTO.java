package com.gateway.server.dto;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Document(collection = "notification")
public class NotificationDetailsDTO {

	@JsonIgnore
	@Id
	private String _id;
	private String userId;
	private String notificationTypeId;
	private Map<String, Object> data;
	private Date createdDate;
}