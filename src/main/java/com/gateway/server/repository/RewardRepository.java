package com.gateway.server.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gateway.server.dto.RewardDTO;

public interface RewardRepository extends MongoRepository<RewardDTO, Long>{

	List<RewardDTO> findAllByUserIdIn(List<String> userId);
}
