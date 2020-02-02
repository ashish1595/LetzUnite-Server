package com.gateway.server.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.gateway.server.dto.UserProfileDTO;
import com.gateway.server.entity.UserProfile;
import com.gateway.server.exception.UtilityException;

public interface UserProfileService {

	String save(UserProfile userProfile) throws UtilityException;

	String update(UserProfile userProfile, MultipartFile picture) throws UtilityException;

	UserProfileDTO getByEmail(String emailId) throws UtilityException;
	
	Boolean validateEmailForUser(String id) throws UtilityException;

	Object getUserProfileImage(String emailId, HttpServletResponse response) throws UtilityException;
}
