package com.gateway.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gateway.server.dto.TokenDTO;

@Repository
public interface TokenRepository extends MongoRepository<TokenDTO, Long> {
	
	public void deleteByUserId(String userId);
	public TokenDTO findByTokenId(String tokenId);

}