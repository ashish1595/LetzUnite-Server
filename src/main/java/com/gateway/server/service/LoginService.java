package com.gateway.server.service;

import com.gateway.server.entity.LoginRequestData;
import com.gateway.server.entity.LoginResponseData;
import com.gateway.server.exception.UtilityException;

public interface LoginService {

	LoginResponseData login(LoginRequestData loginRequestdata) throws UtilityException;
	Boolean validateLoginToken(String tokenId) throws UtilityException;
}
