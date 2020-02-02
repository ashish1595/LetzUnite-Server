package com.gateway.server.service;

import java.util.List;

import com.gateway.server.entity.NotificationData;
import com.gateway.server.exception.UtilityException;

public interface FeedsService {

	List<NotificationData> getFeeds(String userId, Double latitude, Double longitude, String type) throws UtilityException;

}
