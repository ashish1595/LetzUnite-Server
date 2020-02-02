package com.gateway.server.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.server.entity.NotificationData;
import com.gateway.server.entity.RestResponse;
import com.gateway.server.exception.UtilityException;
import com.gateway.server.service.FeedsService;
import com.gateway.server.utils.RestUtils;

@RestController
@RequestMapping(value = "/notification")
public class FeedsController {
	
	@Autowired
	private FeedsService feedsService;
	
	private static final Logger logger = LoggerFactory.getLogger(FeedsController.class);

	
	@RequestMapping(value = "/feeds")
	public ResponseEntity<RestResponse<List<NotificationData>>> getFeeds(
			@RequestHeader("userId") String userId,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "type", required = false) String type) throws UtilityException {
		logger.info("getFeeds starts.. for userId :: "+ userId);
		return RestUtils.successResponse(feedsService.getFeeds(userId, latitude, longitude, type));
	}
}