package com.gateway.server.serviceImpl;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gateway.server.dto.UserProfileDTO;
import com.gateway.server.entity.UserProfile;
import com.gateway.server.exception.ResponseCode;
import com.gateway.server.exception.UtilityException;
import com.gateway.server.repository.UserProfileRepository;
import com.gateway.server.service.UserProfileService;
import com.gateway.server.utils.CommonUtility;
import com.gateway.server.utils.Constants;

@Service
public class UserProfileServiceImpl implements UserProfileService {

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private EmailServiceImpl emailServiceImpl;

	@Autowired
	private Environment env;

	private static final Logger logger = LoggerFactory.getLogger(UserProfileServiceImpl.class);


	@Override
	public String save(UserProfile userProfile) throws UtilityException {
		logger.info("save profile :: " + userProfile);
		UserProfileDTO userProfileDTO = userProfileRepository.save(getUserProfileDTO(userProfile));
		String verificationApi = "http://35.200.165.223:80/LetzUnite/profile/verify/email?id="+userProfileDTO.get_id();
		emailServiceImpl.processSendMailOperation("info.letzunite@gmail.com", userProfile.getEmailId(), 
				"Verify your email with LetzUnite", env.getProperty("html.verify.body").replace("{unique_link}", verificationApi));
		return "Account has been created. Please verify your EmailId.";
	}

	@Override
	public String update(UserProfile userProfile, MultipartFile picture) throws UtilityException {
		if (! CommonUtility.isNullObject(picture)) {
			try {
				Files.copy(picture.getInputStream(), Paths.get(Constants.PATH, picture.getOriginalFilename()));
			} catch (Exception e) {
				logger.error("update user profile failed while copying image", e);
			}
		}
		UserProfileDTO userProfileDTO = userProfileRepository.findById(userProfile.getId()).get();
		userProfileDTO.setEmailId(CommonUtility.isValidString(userProfile.getEmailId())? userProfile.getEmailId() : userProfileDTO.getEmailId());
		userProfileDTO.setMobileNumber(CommonUtility.isValidString(userProfile.getMobileNumber())? userProfile.getMobileNumber() : userProfileDTO.getMobileNumber());
		userProfileDTO.setName(CommonUtility.isValidString(userProfile.getEmailId())? userProfile.getEmailId() : userProfileDTO.getEmailId());
		userProfileDTO.setPassword(CommonUtility.isValidString(userProfile.getPassword())? userProfile.getPassword() : userProfileDTO.getPassword());
		userProfileRepository.save(userProfileDTO);
		return "Profile updated successfully!";
	}

	@Override
	public UserProfileDTO getByEmail(String emailId) throws UtilityException {
		List<UserProfileDTO> userProfileList = userProfileRepository.findByEmailId(emailId);
		if (! CommonUtility.isValidList(userProfileList))
			throw new UtilityException(ResponseCode.USER_DATA_NOT_FOUND_IN_RESPONSE);
		UserProfileDTO userProfile = userProfileList.get(0);
		if (userProfile.getStatus() != Constants.ACTIVE_STATUS) {
			verifyEmailId(userProfile.get_id(), userProfile.getEmailId());
		}
		return userProfile;
	}

	@Override
	public Boolean validateEmailForUser(String id) throws UtilityException {
		UserProfileDTO userProfileDTO = userProfileRepository.findById(id).get();
		userProfileDTO.setStatus(Constants.ACTIVE_STATUS);
		userProfileRepository.save(userProfileDTO);
		return Boolean.TRUE;
	}

	private UserProfileDTO getUserProfileDTO(UserProfile userProfile) throws UtilityException {
		if (! (CommonUtility.isValidString(userProfile.getName()) && CommonUtility.isValidString(userProfile.getEmailId())
				&& CommonUtility.isValidString(userProfile.getPassword())))
			throw new UtilityException(ResponseCode.USER_DATA_NOT_FOUND_IN_REQUEST);
		List<UserProfileDTO> userDetails = userProfileRepository.findByEmailId(userProfile.getEmailId());
		if (CommonUtility.isValidList(userDetails)) {
			if (userDetails.get(0).getStatus() == Constants.ACTIVE_STATUS) {
				throw new UtilityException(ResponseCode.ACCOUNT_ALREADY_EXISTS);
			} else {
				verifyEmailId(userDetails.get(0).get_id(), userProfile.getEmailId());
			}
		}
		UserProfileDTO profileDTO = new UserProfileDTO();
		profileDTO.setName(userProfile.getName());
		profileDTO.setMobileNumber(userProfile.getMobileNumber());
		profileDTO.setPassword(userProfile.getPassword());
		profileDTO.setEmailId(userProfile.getEmailId());
		profileDTO.setStatus(Constants.INACTIVE_STATUS);
		if (! CommonUtility.isNullObject(userProfile.getAdditionalInfo())) {
			profileDTO.setAdditionalInfo(userProfile.getAdditionalInfo());
		}
		return profileDTO;
	}

	private void verifyEmailId(String userId, String emailId) {
		String verificationApi = "http://35.200.165.223:80/LetzUnite/profile/verify/email?id="+userId;
		emailServiceImpl.processSendMailOperation("info.letzunite@gmail.com", emailId, 
				"Verify your email with LetzUnite", env.getProperty("html.verify.body").replace("{unique_link}", verificationApi));
		throw new UtilityException(ResponseCode.VERIFY_EMAIL);
	}

	@Override
	public Object getUserProfileImage(String emailId, HttpServletResponse response) throws UtilityException {
		Path file = Paths.get(Constants.PATH, "files.zip");
		OutputStream a = null;
		if (Files.exists(file)) {
			try {
				Files.copy(file, response.getOutputStream());
				response.flushBuffer();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return a;
	}
}