package com.gateway.server.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateway.server.dto.RewardDTO;
import com.gateway.server.exception.ResponseCode;
import com.gateway.server.exception.UtilityException;
import com.gateway.server.repository.RewardRepository;
import com.gateway.server.service.RewardService;
import com.gateway.server.utils.CommonUtility;

@Service
public class RewardServiceImpl implements RewardService {
	
	
	@Autowired
	private RewardRepository rewardRepository;

	@Override
	public List<RewardDTO> getRewardData(String userId) throws UtilityException {
		if (! CommonUtility.isValidString(userId))
			throw new UtilityException(ResponseCode.USER_DATA_NOT_FOUND_IN_REQUEST);
		List<String> userIdList = new ArrayList<String>();
		userIdList.add(userId); userIdList.add("DEFAULT");
		return rewardRepository.findAllByUserIdIn(userIdList);
	}

	@Override
	public String saveRewardData(RewardDTO rewardDTO) throws UtilityException {
		rewardRepository.save(rewardDTO);
		return "Reward saved successfully!";
	}
}