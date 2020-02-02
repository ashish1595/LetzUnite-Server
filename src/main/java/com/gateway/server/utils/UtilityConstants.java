package com.gateway.server.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UtilityConstants {

	@Value("#{'${state.lat.lng}'.split(',')}") 
	public List<String> stateLatLng;
	
	@Value("{html.verify.body}")
	public String htmlVerifyEmailBody;
}