package com.gateway.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.server.entity.BloodRequirementData;
import com.gateway.server.entity.FactsData;
import com.gateway.server.entity.RestResponse;
import com.gateway.server.exception.UtilityException;
import com.gateway.server.service.RequirementService;
import com.gateway.server.utils.Constants;
import com.gateway.server.utils.RestUtils;

@RestController
public class RequirementController {
	
	@Autowired
	private RequirementService requirementService;
	
	@RequestMapping(value = Constants.URI.BLOOD_REQUIREMENT, method = RequestMethod.POST)
	public ResponseEntity<RestResponse<String>> saveRequirement(@RequestBody 
			BloodRequirementData bloodRequirementDTO) throws UtilityException {
		return RestUtils.successResponse(requirementService.saveRequirement(bloodRequirementDTO));
	}
	
	@RequestMapping(value = "/facts", method = RequestMethod.POST)
	public ResponseEntity<RestResponse<String>> saveFacts(@RequestBody 
			FactsData factsData) throws UtilityException {
		return RestUtils.successResponse(requirementService.saveFacts(factsData));
	}
}