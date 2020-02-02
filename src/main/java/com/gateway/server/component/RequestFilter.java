package com.gateway.server.component;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.server.entity.RestResponse;
import com.gateway.server.exception.ResponseCode;
import com.gateway.server.exception.UtilityException;
import com.gateway.server.service.LoginService;
import com.gateway.server.utils.CommonUtility;
import com.gateway.server.utils.Constants;
import com.gateway.server.utils.RestUtils;

@WebFilter(urlPatterns="/*")
public class RequestFilter extends OncePerRequestFilter {

	@Autowired
	private LoginService loginService;
	
	private static final Logger logger = LoggerFactory.getLogger(RequestFilter.class);


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if (CommonUtility.isNullObject(loginService)) {
			logger.info("loginService was null and is being loaded...");
			loginService = WebApplicationContextUtils
					.getRequiredWebApplicationContext(this.getFilterConfig().getServletContext())
					.getBean(LoginService.class);
		}
		
		String serverPassKey = request.getHeader(Constants.Headers.SERVER_KEY);
		if (CommonUtility.isValidString(serverPassKey) && Constants.VALUE.SERVER_KEY.equals(serverPassKey)) {
			filterChain.doFilter(request, response);
			return;
		}
		try {
			tokenValidation(request);
			logger.info("<<<<< request.getMethod()  :: " +request.getMethod());
			if (! request.getMethod().equals(Constants.HTTP_METHOD.GET)) {
				ServletRequestWrapper myrequest = new ServletRequestWrapper(request);
				filterChain.doFilter(myrequest, response);
			} else {
				logger.info("Going to hit get request.. "+ request.getRequestURI());
				filterChain.doFilter(request, response);
			}
		} catch (UtilityException e) {
			handleException(response, RestUtils.errorResponseEnum(e.getResponseCode()));
		} catch (Exception e) {
			handleException(response, RestUtils.errorResponseData(e.getMessage()));
		}
	}

	private void tokenValidation(HttpServletRequest request) throws UtilityException {
		if (Constants.LOGIN.contains(request.getRequestURI())  || Constants.SIGNUP.contains(request.getRequestURI())
				|| Constants.VERIFY_EMAIL.contains(request.getRequestURI())) return;
		String token = request.getHeader(Constants.Headers.TOKEN_ID);
		logger.info("tokenValidation :: "+ request.getRequestURI() + " token :: "+token);
		if (CommonUtility.isValidString(token)) {
			loginService.validateLoginToken(token);
			logger.info("tokenValidation :: "+ request.getRequestURI() + " done....");
		} else {
			throw new UtilityException(ResponseCode.MISSING_HEADER_KEY);
		}
	}

	private void handleException(HttpServletResponse response, RestResponse<?> err)
			throws IOException, JsonProcessingException {
		response.setStatus(HttpStatus.OK.value());
		response.setHeader(Constants.Headers.ContentType, Constants.Headers.AppJson);
		response.getWriter().write(convertObjectToJson(err));
	}

	public String convertObjectToJson(Object object) throws JsonProcessingException {
		if (CommonUtility.isNullObject(object)) return null;
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}
}
