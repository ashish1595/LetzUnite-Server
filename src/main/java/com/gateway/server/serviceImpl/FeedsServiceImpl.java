package com.gateway.server.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateway.server.dto.NotificationDetailsDTO;
import com.gateway.server.entity.NotificationData;
import com.gateway.server.exception.ResponseCode;
import com.gateway.server.exception.UtilityException;
import com.gateway.server.repository.NotificationRepository;
import com.gateway.server.repository.UserProfileRepository;
import com.gateway.server.service.FeedsService;
import com.gateway.server.utils.CommonUtility;
import com.gateway.server.utils.UtilityConstants;

@Service
public class FeedsServiceImpl implements FeedsService {

	@Autowired
	private UtilityConstants utilityConstants;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Override
	public List<NotificationData> getFeeds(String userId, Double latitude, Double longitude, String type) throws UtilityException {
		List<NotificationDetailsDTO> notificationDetails =  notificationRepository.findAllByOrderByNotificationTypeIdAsc();
		if (! CommonUtility.isValidList(notificationDetails))
			throw new UtilityException(ResponseCode.USER_DATA_NOT_FOUND_IN_RESPONSE);

		List<NotificationData> notificationDataList = new ArrayList<NotificationData>();
		NotificationData notificationData = null;
		String notificationTypeId = "NULL";
		for (NotificationDetailsDTO detailsDTO : notificationDetails) {
			if (notificationTypeId.equals(detailsDTO.getNotificationTypeId())) {
				List<NotificationDetailsDTO> notificationDTOList = notificationData.getNotificationData();
				notificationDTOList.add(detailsDTO);
				notificationData.setNotificationData(notificationDTOList);
			} else {
				if (! CommonUtility.isNullObject(notificationData)) notificationDataList.add(notificationData);
				notificationData = new NotificationData();
				notificationTypeId = detailsDTO.getNotificationTypeId();
				notificationData.setNotificationTypeId(notificationTypeId);
				List<NotificationDetailsDTO> notificationDTOList = new ArrayList<>();
				notificationDTOList.add(detailsDTO);
				notificationData.setNotificationData(notificationDTOList);
			}
		}
		notificationDataList.add(notificationData);
		return notificationDataList;




		//		if (CommonUtility.isValidDouble(latitude) && CommonUtility.isValidDouble(longitude)) {
		//			
		//		} else {
		//			UserProfileDTO userProfile = userProfileRepository.findById(userId).get();
		//			BSONObject additionalInfo = userProfile.getAdditionalInfo();
		//			String city = (String) additionalInfo.get("city");
		//			String state = (String) additionalInfo.get("state");
		//		}
		//		return null;
	}

	private List<String> getStateNames(Double userLat, Double userLng) {
		List<String> stateLatLngList = utilityConstants.stateLatLng;
		List<String> states = new ArrayList<String>();
		for (String stateLatLng: stateLatLngList) {

		}

		return null;
	}

	public static double formulaDistance(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 6371000;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
		* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return earthRadius * c;
	}

}
