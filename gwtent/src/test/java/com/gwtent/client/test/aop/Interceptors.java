/*******************************************************************************
 *  Copyright 2001, 2007 JamesLuo(JamesLuo.au@gmail.com)
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *  
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 * 
 *  Contributors:
 *******************************************************************************/


package com.gwtent.client.test.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.gwtent.aop.client.intercept.MethodInvocation;
import com.gwtent.client.test.aop.Phone.Receiver;

public class Interceptors {

	@Aspect
	public static class PhoneCallErrorLog{
		
		@AfterThrowing(pointcut="execution(* com.gwtent.client.test.aop.Phone.call(java.lang.Number))",
				throwing="e")
		public void phoneCallErrorLog(MethodInvocation invocation, Throwable e){
			System.out.println("PhoneCallErrorLog: " + e.getMessage());
		}
	}

	@Aspect
	public static class PhoneLoggerInterceptor {
		
		//execution(modifiers-pattern? ret-type-pattern declaring-type-pattern? name-pattern(param-pattern) throws-pattern?)
		@Before("execution(* com.gwtent.client.test.aop.Phone.call(java.lang.Number))")
		public void beforeCall(MethodInvocation invocation) {
			for (Object arg : invocation.getArguments()){
				System.out.println("Do something in PhoneLoggerInterceptor, Before...");
				
				if (arg instanceof Number)
					System.out.println("Call to: " + arg);
			}
		}
		
		@AfterReturning(pointcut = "execution(* com.gwtent.client.test.aop.Phone.call(java.lang.Number))",
			returning = "returnValue")
		public void afterReturningCall(MethodInvocation invocation, Object returnValue) {
			System.out.println("Do something in PhoneLoggerInterceptor, AfterReturning...");
			
			if ((returnValue != null)){
				if (returnValue instanceof Number)
					System.out.println("Returning Number: " + returnValue.toString());
				else
					System.out.println("Returning Object: " + returnValue.toString());
			}else{
				System.out.println("Returning Object is NULL?");
			}
		}
		
		@After("execution(* com.gwtent.client.test.aop.Phone.call(java.lang.Number))")
		public void afterCall(MethodInvocation invocation) {				
			for (Object arg : invocation.getArguments()){
				System.out.println("Do something in PhoneLoggerInterceptor, After...");
				
				if (arg instanceof Number)
					System.out.println("After Call: " + arg);
			}
		}
	}
	
	
	@Aspect
	public static class PhoneBillingSystemInterceptor{
		private Map<Number, Long> callTime = new HashMap<Number, Long>();
		private Map<Number, Date> startCallTime = new HashMap<Number, Date>();
		
		@Before("execution(* com.gwtent.client.test.aop.Phone.call(java.lang.Number))")
		public void beforeCall(MethodInvocation invocation) {
			startCallTime.put(getPhoneNumber(invocation), new Date());
		}
		
		//We don't care what happened, we always earn money from client
		@After("execution(* com.gwtent.client.test.aop.Phone.call(java.lang.Number))")
		public void afterCall(MethodInvocation invocation, Object returnValue) {
			Number number = getPhoneNumber(invocation);
			Date startDate = startCallTime.get(number);
			if (startDate != null){
				Long allTime = callTime.get(number);
				if (allTime == null)
					allTime = 0L;
				allTime = allTime + ((new Date().getTime()) - startDate.getTime());
				callTime.put(number, allTime);
			}
				
			System.out.println("call time: " + callTime);
		}
		
		private Number getPhoneNumber(MethodInvocation invocation){
			return ((Phone)(invocation.getThis())).getNumber();
		}
	}

	@Aspect
	public static class PhoneNumberValidateInterceptor{
		//@Pointcut("args(java.lang.Number,..)")
		private void numberArgOperation(Number number){};
		
		//args not full support yet
		@Before("args(java.lang.Number,..)")
		//@Before("execution(* com.gwtent.client.test.aop.Phone.call(java.lang.Number))")
		public void validateNumberNotSupportYet(Number number) throws Throwable {
			System.out.println("Validate, you cann't dail to 0, current Number(most time it's null): " + number);
			if ((number != null) && (number.intValue() == 0)){
				throw new RuntimeException("You cann't dail to 0.");
			}
		}
		
		@Before("args(java.lang.Number,..)")
		//@Before("execution(* com.gwtent.client.test.aop.Phone.call(java.lang.Number))")
		public void validateNumber(MethodInvocation invocation) throws Throwable {
			Number number = null;
			for (Object obj : invocation.getArguments()){
				if (obj instanceof Number)
					number = (Number)obj;
			}
			if (number != null){
				System.out.println("Validate, you cann't dail to 0, current Number: " + number);
				if ((number != null) && (number.intValue() == 0)){
					throw new RuntimeException("You cann't dail to 0.");
				}
			}
		}
	}

	@Aspect
	public static class PhoneRedirectInterceptor {
		
		@Around("execution(* com.gwtent.client.test.aop.Phone.call(java.lang.Number))")
		public Object invoke(MethodInvocation invocation) throws Throwable {
			invocation.proceed();
			System.out.println("Do something in PhoneRedirectInterceptor...");
			return new Receiver("Alberto's Pizza Place");
		}
	}
	
	
	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface AOPTestAnnotation{
		public String value();
	}
	
	@Aspect
	public static class PhoneTestAnnotation{
		@Before("@annotation(com.gwtent.client.test.aop.Interceptors.AOPTestAnnotation)")
		public void testPhoneAnnotation(MethodInvocation invocation) throws Throwable {
			Number number = null;
			for (Object obj : invocation.getArguments()){
				if (obj instanceof Number)
					number = (Number)obj;
			}
			
			System.out.println("annotation express");
		}
	}
	
	@Aspect
	public static class PhoneTestMatchClass{
		@Before("matchclass(com.gwtent.test.aspectj.TestMatcher)")
		public void testPhoneAnnotation(MethodInvocation invocation) throws Throwable {
			Number number = null;
			for (Object obj : invocation.getArguments()){
				if (obj instanceof Number)
					number = (Number)obj;
			}
			
			System.out.println("Match class express");
		}
	}
	
}


