package com.tiankong44.aspect;

import com.tiankong44.model.RequestLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogAspect {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Pointcut("execution(* com.tiankong44*.*(..))")
	public void log() {

	}

	@Before("log()")
	public void doBefore(JoinPoint joinPoint) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String url = request.getRequestURL().toString();
		String ip = request.getRemoteAddr();
		String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
		logger.info("Request : {}", requestLog);
	}

	@After("log()")
	public void doAfter() {
		logger.info("---doAfter---");
	}

	@AfterReturning(returning = "result", pointcut = "log()")
	public void doAfterReturn(Object result) {
		logger.info("Result : {}", result);
	}
}
