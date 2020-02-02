package com.gateway.server.serviceImpl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.server.dto.NotificationDetailsDTO;
import com.gateway.server.entity.BloodRequirementData;
import com.gateway.server.entity.FactsData;
import com.gateway.server.repository.NotificationRepository;
import com.gateway.server.service.RequirementService;

@Service
public class RequirementServiceImpl implements RequirementService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Override
	public String saveRequirement(BloodRequirementData bloodRequirementDTO) {
		NotificationDetailsDTO notificationDetails = new NotificationDetailsDTO();
        ObjectMapper oMapper = new ObjectMapper();

		notificationDetails.setData(oMapper.convertValue(bloodRequirementDTO, Map.class));
		notificationDetails.setNotificationTypeId("1");
		notificationDetails.setUserId("1"); //universal userId
		notificationDetails.setCreatedDate(new Date());
		notificationRepository.save(notificationDetails);
		return "Your requirements has been successfully posted!";
	}

	@Override
	public String saveFacts(FactsData factsData) {
		NotificationDetailsDTO notificationDetails = new NotificationDetailsDTO();
        ObjectMapper oMapper = new ObjectMapper();

		notificationDetails.setData(oMapper.convertValue(factsData, Map.class));
		notificationDetails.setNotificationTypeId("3");
		notificationDetails.setUserId("1"); //universal userId
		notificationDetails.setCreatedDate(new Date());
		notificationRepository.save(notificationDetails);
		return "Your facts has been successfully posted!";
	}
}