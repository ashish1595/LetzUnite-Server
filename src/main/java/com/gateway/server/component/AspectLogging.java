package com.gateway.server.component;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gateway.server.exception.UtilityException;
import com.gateway.server.utils.CommonUtility;

@Aspect
@Component
public class AspectLogging {

	private static final Logger logger = LoggerFactory.getLogger(AspectLogging.class);

	@Pointcut("execution(public !void org.springframework.data.repository.Repository+.*(..))")
	public void publicNonVoidRepositoryMethod() {}

	@Around("execution( * com.gateway.server.controller..*.*(..)) || "
			+ "execution( * com.gateway.server.serviceImpl..*.*(..)) || publicNonVoidRepositoryMethod()")
	public Object around(ProceedingJoinPoint joinPoint) throws UtilityException {
		StringBuffer loggerString = new StringBuffer();
		try {
			long startTime = System.currentTimeMillis();
			CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature(); 
			String[] argNames = codeSignature.getParameterNames();
			Object[] paramData = joinPoint.getArgs();
			if (! (CommonUtility.isNullObject(argNames) || CommonUtility.isNullObject(paramData))) {
				if (argNames.length == paramData.length) {
					for (int i=0; i< argNames.length; i++) {
						loggerString.append(((i==0)? "": ", ") + argNames[i]+ " = " + (CommonUtility.isNullObject(paramData[i])? "null" : paramData[i].toString()));
					}
				} else {
					loggerString.append(Arrays.asList(paramData).toString().replace("[", "").replace("]", ""));
				}
			}
			Object returnValue = joinPoint.proceed();
			long timeTaken = System.currentTimeMillis() - startTime;
			logger.info(joinPoint.getSignature() + " with arguments("+ loggerString.toString() + ") took "+ timeTaken+ "(ms)" + " Output :: "+returnValue);
			return returnValue;
		} catch (UtilityException e) {
			throw new UtilityException(e.getResponseCode());
		} catch (Throwable e) {
			logger.error(joinPoint.getSignature() + " with arguments("+ loggerString.toString() + ") failed due to :: ", e);
			throw new UtilityException();
		}
	}
}
