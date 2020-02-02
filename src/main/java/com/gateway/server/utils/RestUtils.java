package com.gateway.server.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.gateway.server.entity.RestResponse;
import com.gateway.server.exception.ResponseCode;

public class RestUtils {
	
	public static <T> ResponseEntity<RestResponse<T>> successResponse(T data, String message, HttpStatus statusCode) {
		RestResponse<T> response = new RestResponse<T>(message, Constants.SUCCESS_STATUS, data);
		return new ResponseEntity<RestResponse<T>>(response, statusCode);
	}

	public static <T> ResponseEntity<RestResponse<T>> successResponse(T data) {
		return successResponse(data, "ok", HttpStatus.OK);
	}

	public static <T> ResponseEntity<RestResponse<T>> successResponse(T data, String message) {
		return successResponse(data, message, HttpStatus.OK);
	}

	public static <T> ResponseEntity<RestResponse<?>> errorResponseEntity(String errorMessage,
			HttpStatus statusCode) {
		return new ResponseEntity<RestResponse<?>>(new RestResponse<T>(errorMessage, Constants.FAILURE_STATUS, null), statusCode);
	}

	public static <T> ResponseEntity<RestResponse<?>> errorResponseData(ResponseCode routerResponseCode,
			HttpStatus statusCode) {
		RestResponse<T> response = new RestResponse<T>(routerResponseCode.getDescription(), routerResponseCode.getCode(), null);
		return new ResponseEntity<RestResponse<?>>(response, statusCode);
	}

	public static <T> RestResponse<?> errorResponseData(String errorMessage) {
		return new RestResponse<T>(errorMessage, Constants.FAILURE_STATUS,null);
	}

	public static <T> RestResponse<?> errorResponseEnum(ResponseCode routerResponseCode) {
		return new RestResponse<T>(routerResponseCode.getDescription(), routerResponseCode.getCode(), null);
	}
}
