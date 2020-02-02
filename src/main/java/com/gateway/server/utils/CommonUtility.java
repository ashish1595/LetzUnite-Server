package com.gateway.server.utils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gateway.server.exception.UtilityException;

public class CommonUtility {
	private final static Logger logger = LoggerFactory.getLogger(CommonUtility.class.getName());

	public static boolean isNullObject(Object obj) {
		return (null == obj ? true : false);
	}

	public static String validMobileNumber(String mobile) {
		if (isValidString(mobile) && mobile.charAt(0) == '+') {
			mobile = mobile.substring(3);
		}
		return mobile;
	}

	public static Boolean hasMoreElements(Long totalElements, Integer page, Integer size) {
		return (totalElements - (page * size)) > 0;
	}

	public static boolean isValidString(String obj) {
		return ((null == obj || obj.trim().isEmpty()) ? false : true);
	}

	public static boolean isValidMap(Map<?, ?> map) {
		return ((null == map || map.size() == 0) ? false : true);
	}

	public static boolean isValidCollection(Collection<?> obj) {
		return (obj != null && obj.size() > 0 ? true : false);
	}

	public static double getScaledAmount(double amount) {
		BigDecimal scaledAmount = new BigDecimal(amount);
		scaledAmount = scaledAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		return scaledAmount.doubleValue();
	}

	public static boolean isValidInteger(Integer value) {
		return ((null == value || value.intValue() == 0) ? false : true);
	}

	public static boolean isValidDouble(Double value) {
		return ((null == value) ? false : true);
	}

	public static boolean isValidFloat(Float value) {
		return ((null == value) ? false : true);
	}

	public static boolean isValidLong(Long value) {
		return ((null == value || value.intValue() == 0) ? false : true);
	}

	public static boolean isValidList(List<?> list) {
		return ((list == null || list.size() == 0) ? false : true);
	}

	public static boolean isNotEmpty(String attrKey) {
		if (!attrKey.isEmpty())
			return true;
		return false;
	}
	
	public static HashMap<String, String> getConfiguration(int clientAppVersionId,
			HashMap<Integer, HashMap<String, String>> configMap) {
		logger.info("utility  service :: loadConfiguration");
		return configMap.get(clientAppVersionId);
	}

	public static void validateBloodType(String bloodType) {
		if(! "".contains(bloodType))
			throw new UtilityException(); // invalid blood type
	}
}