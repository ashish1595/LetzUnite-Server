package com.gateway.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.server.dto.RewardDTO;
import com.gateway.server.entity.RestResponse;
import com.gateway.server.exception.UtilityException;
import com.gateway.server.service.RewardService;
import com.gateway.server.utils.Constants;
import com.gateway.server.utils.RestUtils;

@RestController
@RequestMapping(value = Constants.URI.REWARD)
public class RewardController {
	
	@Autowired
	private RewardService rewardService;

	@RequestMapping
	public ResponseEntity<RestResponse<List<RewardDTO>>> getRewardData(@RequestParam("userId") String userId) throws UtilityException {
		return RestUtils.successResponse(rewardService.getRewardData(userId));
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<RestResponse<String>> saveRewardData(@RequestBody RewardDTO rewardDTO) throws UtilityException {
		return RestUtils.successResponse(rewardService.saveRewardData(rewardDTO));
	}
}