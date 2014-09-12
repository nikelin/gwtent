package com.gwtent.client.test.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.gwtent.aop.client.intercept.MethodInvocation;

public class LogInterceptors {
	
	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface LogForAccountClick{
		public String value();
	}
	
	@Aspect
	public static class LogAccountClickInterceptor{
		@Before("@annotation(com.gwtent.client.test.aop.LogInterceptors.LogForAccountClick)")
		public void logAccountClick(MethodInvocation invocation) throws Throwable {
			System.out.println("logAccountClick here");
		}
	}
}
