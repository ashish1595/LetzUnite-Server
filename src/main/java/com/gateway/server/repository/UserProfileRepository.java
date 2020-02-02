package com.gateway.server.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gateway.server.dto.UserProfileDTO;

@Repository
public interface UserProfileRepository extends MongoRepository<UserProfileDTO, String> {
	
	List<UserProfileDTO> findByEmailId(String emailId);
}
