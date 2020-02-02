package com.gateway.server.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gateway.server.dto.UserProfileDTO;
import com.gateway.server.entity.RestResponse;
import com.gateway.server.entity.UserProfile;
import com.gateway.server.exception.UtilityException;
import com.gateway.server.service.UserProfileService;
import com.gateway.server.utils.Constants;
import com.gateway.server.utils.RestUtils;

@RestController
@RequestMapping(Constants.URI.PROFILE)
public class UserProfileController {
	
	@Autowired
	private UserProfileService userProfileService;

	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<RestResponse<String>> saveUserProfile(
			@RequestBody UserProfile userProfile) throws UtilityException {
		return RestUtils.successResponse(userProfileService.save(userProfile));
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	public ResponseEntity<RestResponse<String>> updateUserProfile(
			@RequestPart(value = "picture", required = false) MultipartFile picture,
			@RequestBody UserProfile userProfile) throws UtilityException {
		return RestUtils.successResponse(userProfileService.update(userProfile, picture));
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/picture")
	public ResponseEntity<RestResponse<Object>> getUserProfileImage(
			@RequestParam(value = "emailId") String emailId,
			HttpServletResponse response) throws UtilityException {
		return RestUtils.successResponse(userProfileService.getUserProfileImage(emailId, response));
	}
	
	@RequestMapping
	public ResponseEntity<RestResponse<UserProfileDTO>> getUserProfile(
			@RequestParam("emailId") String emailId) throws UtilityException {
		return RestUtils.successResponse(userProfileService.getByEmail(emailId));
	}
	
	@RequestMapping(value=Constants.URI.VERIFY_EMAIL)
	public ResponseEntity<RestResponse<Boolean>> validateEmailForUser(
			@RequestParam("id") String id) throws UtilityException {
		return RestUtils.successResponse(userProfileService.validateEmailForUser(id));
	}
}