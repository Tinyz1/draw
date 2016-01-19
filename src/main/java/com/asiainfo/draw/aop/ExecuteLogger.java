package com.asiainfo.draw.aop;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ÷¥––»’÷æ
 * 
 * @author yecl
 *
 */
@Component
@Aspect
public class ExecuteLogger {

	private final Logger logger = LoggerFactory.getLogger(ExecuteLogger.class);

	@Pointcut("execution(* com.asiainfo.draw.service.*Service*.*(..))")
	public void logger() {
	}

	@Around("logger()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		logger.info("{}.{}({})", pjp.getTarget().getClass().getName(), method.getName(), Arrays.toString(pjp.getArgs()));
		Object rtn = null;
		try {
			rtn = pjp.proceed();
		} catch (Throwable e) {
			logger.error(e.toString());
			throw e;
		}
		logger.info(String.valueOf(rtn));
		return rtn;
	}

}
