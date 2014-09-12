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


package com.gwtent.test.spring;

import java.lang.reflect.Method;

import junit.framework.TestCase;

//import org.springframework.aop.ClassFilter;
//import org.springframework.aop.MethodMatcher;
//import org.springframework.aop.Pointcut;
//import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import com.gwtent.client.test.aop.Phone;

public class AspectJSpringTestCase extends TestCase {
//	public void testMatchExplicit() throws SecurityException, NoSuchMethodException {
//		String expression = "execution(* com.gwtent.client.test.aop.Phone.call(java.lang.Number))";
//
//		Pointcut pointcut = getPointcut(expression);
//		ClassFilter classFilter = pointcut.getClassFilter();
//		MethodMatcher methodMatcher = pointcut.getMethodMatcher();
//
//		assertTrue(classFilter.matches(Phone.class));
//		System.out.println(classFilter.matches(this.getClass()));
//		//assertFalse(classFilter.matches(this.getClass()));
//
//		assertFalse("Should not be a runtime match", methodMatcher.isRuntime());
//		Method method = Phone.class.getMethod("call", java.lang.Number.class);
//		assertTrue(methodMatcher.matches(method, Phone.class));
//		Method methodToString = Phone.class.getMethod("toString");
//		assertFalse("Expression should match toString() method", methodMatcher.matches(methodToString, Phone.class));
//	}
//	
//	
//	private Pointcut getPointcut(String expression) {
//		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//		pointcut.setExpression(expression);
//		return pointcut;
//	}
}
