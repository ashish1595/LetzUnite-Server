package com.gateway.server.serviceImpl;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.gateway.server.dto.TokenDTO;
import com.gateway.server.dto.UserProfileDTO;
import com.gateway.server.entity.LoginRequestData;
import com.gateway.server.entity.LoginResponseData;
import com.gateway.server.exception.ResponseCode;
import com.gateway.server.exception.UtilityException;
import com.gateway.server.repository.TokenRepository;
import com.gateway.server.repository.UserProfileRepository;
import com.gateway.server.service.LoginService;
import com.gateway.server.utils.CommonUtility;
import com.gateway.server.utils.Constants;
import com.gateway.server.utils.DateUtil;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UserProfileRepository userProfileRepository;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Autowired
	private EmailServiceImpl emailServiceImpl;
	
	@Autowired
	private Environment env;
	
	private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);


	@Override
	public LoginResponseData login(LoginRequestData loginRequestdata) throws UtilityException {
		validate(loginRequestdata);
		UserProfileDTO profileDTO = getUserProfileData(loginRequestdata);
		if (! profileDTO.getPassword().equals(loginRequestdata.getPassword()))
				throw new UtilityException(ResponseCode.INVALID_PASSWORD);
		tokenRepository.deleteByUserId(profileDTO.get_id());
		String tokenId = UUID.randomUUID().toString();
		saveToken(loginRequestdata.getDeviceId(), profileDTO.get_id(), tokenId);
		return getLoginResponse(profileDTO, tokenId);
	}
	
	@Override
	public Boolean validateLoginToken(String tokenId) throws UtilityException {
		logger.info("validateLoginToken :: tokenId "+ tokenId+" begin...");
		TokenDTO tokenDTO = tokenRepository.findByTokenId(tokenId);
		if (CommonUtility.isNullObject(tokenDTO) || tokenDTO.getExpiryTime().before(Calendar.getInstance().getTime())) 
			throw new UtilityException(ResponseCode.TOKEN_EXPIRED);
		logger.info("validateLoginToken :: tokenId "+ tokenId+" end...");
		return Boolean.TRUE;
	}
	
	private LoginResponseData getLoginResponse(UserProfileDTO profileDTO, String tokenId) {
		LoginResponseData responseData = new LoginResponseData();
		responseData.setTokenId(tokenId);
		responseData.setUserProfile(profileDTO);
		return responseData;
	}

	private void saveToken(String deviceId, String userId, String tokenId) {
		TokenDTO tokenDTO = new TokenDTO();
		tokenDTO.setDeviceId(deviceId);
		tokenDTO.setExpiryTime(DateUtil.getExpiryDate());
		tokenDTO.setIsDeleted(0);
		tokenDTO.setTokenId(tokenId);
		tokenDTO.setUserId(userId);
		tokenRepository.save(tokenDTO);
	}

	private UserProfileDTO getUserProfileData(LoginRequestData loginRequestdata) throws UtilityException {
		List<UserProfileDTO> userProfileList = userProfileRepository.findByEmailId(loginRequestdata.getEmailId());
		if (! CommonUtility.isValidList(userProfileList))
			throw new UtilityException(ResponseCode.USER_NEEDS_SIGNUP);
		UserProfileDTO userProfile = userProfileList.get(0);
		if (userProfile.getStatus() != Constants.ACTIVE_STATUS) {
			verifyEmailId(userProfile.get_id(), userProfile.getEmailId());
		}
		return userProfile;
	}
	
	private void verifyEmailId(String userId, String emailId) {
		String verificationApi = "http://35.200.165.223:80/LetzUnite/profile/verify/email?id="+userId;
		emailServiceImpl.processSendMailOperation("info.letzunite@gmail.com", emailId, 
				"Verify your email with LetzUnite", env.getProperty("html.verify.body").replace("{unique_link}", verificationApi));
		throw new UtilityException(ResponseCode.VERIFY_EMAIL);
	}

	private void validate(LoginRequestData loginRequestdata) throws UtilityException {
		if (! (CommonUtility.isValidString(loginRequestdata.getEmailId()) && CommonUtility.isValidString(loginRequestdata.getDeviceId())
				&& CommonUtility.isValidString(loginRequestdata.getPassword())))
			throw new UtilityException(ResponseCode.USER_DATA_NOT_FOUND_IN_REQUEST);
	}

}
