package com.gateway.server.service;

import java.util.List;

import com.gateway.server.dto.RewardDTO;
import com.gateway.server.exception.UtilityException;

public interface RewardService {

	List<RewardDTO> getRewardData(String userId) throws UtilityException;
	String saveRewardData(RewardDTO rewardDTO) throws UtilityException;
}
