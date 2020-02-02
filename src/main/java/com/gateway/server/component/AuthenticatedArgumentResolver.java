package com.gateway.server.component;

import java.io.InputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.gateway.server.exception.ResponseCode;
import com.gateway.server.exception.UtilityException;

public class AuthenticatedArgumentResolver implements HandlerMethodArgumentResolver {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public String resolveArgument(MethodParameter param, ModelAndViewContainer arg1, NativeWebRequest request,
			WebDataBinderFactory arg3) throws UtilityException {
		logger.info("Entering func: resolveArgument :: Starts...");
		Annotation[] paramAnns = param.getParameterAnnotations();
		String msisdn = null;
		for (Annotation annotation : paramAnns) {
			if (Annotation.class.isInstance(annotation)) {
				HttpServletRequest httprequest = (HttpServletRequest) request.getNativeRequest();
				StringBuilder stringBuilder = new StringBuilder();
				try {
					InputStream inputStream = httprequest.getInputStream();
					if (inputStream != null) {
						StringWriter stringWriter = new StringWriter();
						IOUtils.copy(inputStream, stringWriter, "UTF-8");
						stringBuilder.append(stringWriter.toString());
					} else {
						stringBuilder.append("");
					}
				}
				catch (Exception e) {
					throw new UtilityException(ResponseCode.USER_CREDENTIALS_MISSING_OR_INVALID_IN_REQUEST);
				}
				msisdn = parseBodyToFindMsisdn(stringBuilder.toString());
				MDC.put("msisdn", msisdn);
				logger.info("Entering func: resolveArgument :: Ends for msisdn :: "+msisdn);
			}
		}

		return msisdn;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		logger.info("*************  AuthenticatedArgumentResolver ***************");
		boolean val = parameter.hasParameterAnnotation(Annotation.class);
		return val;
	}

	private String parseBodyToFindMsisdn(String message) throws UtilityException {
		JSONObject requestData = null;
		String msisdn = null;
		try {
			requestData = XML.toJSONObject(message);
			msisdn = requestData.getJSONObject("message").getJSONObject("sms").getJSONObject("source").getJSONObject("address").getJSONObject("number").get("content").toString();
		} catch (JSONException e) {
			throw new UtilityException(ResponseCode.USER_DATA_NOT_FOUND_IN_REQUEST);
		}
		logger.info("checkReverificationStatus :: msisdn after parsing xml :: "+ msisdn);
		return msisdn;
	}

}
