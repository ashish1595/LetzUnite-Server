package com.gateway.server.serviceImpl;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.gateway.server.entity.NotificationData;
import com.gateway.server.entity.RestResponse;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("SERVER_KEY", "123456789");
		httpHeaders.set("userId", "ghvgh");
		RestTemplate restTemplate = new RestTemplate();
		ParameterizedTypeReference<RestResponse<List<NotificationData>>> parameterizedTypeReference = 
				new ParameterizedTypeReference<RestResponse<List<NotificationData>>>() {};
		for (int i =1; i<10000;i++) {		
				
		ResponseEntity<RestResponse<List<NotificationData>>> a = restTemplate.exchange("http://35.200.165.223:80/LetzUnite/notification/feeds", HttpMethod.GET, new HttpEntity<String>("parameters", httpHeaders),
				parameterizedTypeReference);
		
		System.out.println(a.toString());
		}
	}

}
