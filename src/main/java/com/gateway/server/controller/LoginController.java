package com.gateway.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.server.entity.LoginRequestData;
import com.gateway.server.entity.LoginResponseData;
import com.gateway.server.entity.RestResponse;
import com.gateway.server.exception.UtilityException;
import com.gateway.server.service.LoginService;
import com.gateway.server.utils.Constants;
import com.gateway.server.utils.RestUtils;

@RestController
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	@RequestMapping(value=Constants.URI.LOGIN, method = RequestMethod.POST)
	public ResponseEntity<RestResponse<LoginResponseData>> login(@RequestBody 
			LoginRequestData loginRequestData) throws UtilityException {
		return RestUtils.successResponse(loginService.login(loginRequestData));
	}
}