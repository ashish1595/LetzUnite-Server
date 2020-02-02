package com.gateway.server.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gateway.server.dto.NotificationDetailsDTO;

public interface NotificationRepository extends MongoRepository<NotificationDetailsDTO, String> {

	List<NotificationDetailsDTO> findAllByOrderByNotificationTypeIdAsc();
}
