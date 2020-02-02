package com.gateway.server.entity;

import java.util.List;

import com.gateway.server.dto.NotificationDetailsDTO;

import lombok.Data;

@Data
public class NotificationData {

	private String notificationTypeId;
	List<NotificationDetailsDTO> notificationData;
}