package com.gateway.server.service;

import com.gateway.server.entity.BloodRequirementData;
import com.gateway.server.entity.FactsData;

public interface RequirementService {

	String saveRequirement(BloodRequirementData bloodRequirementDTO);
	String saveFacts(FactsData factsData);
}
