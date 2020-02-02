package com.gateway.server.dto;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "notification_type")
public class NotificationTypeDTO {
	
	private String type;
	private Integer description;
}