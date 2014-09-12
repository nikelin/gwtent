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


package com.gwtent.test.aspectj;

import java.lang.reflect.Method;

import org.aspectj.lang.annotation.AfterThrowing;

import junit.framework.TestCase;

import com.gwtent.aop.matcher.AspectJExpress;
import com.gwtent.aop.matcher.Matchers;
import com.gwtent.client.test.aop.Phone;

public class AspectJExpressionTestCase extends TestCase {
	
	private Method methodCall = null;
	private Method methodCallFromString = null;
	private Method methodToString = null;
	
	public void testMatchExplicit() throws SecurityException, NoSuchMethodException {
		String expression = "execution(* com.gwtent.client.test.aop.Phone.call(java.lang.Number))";
	
		AspectJExpress pointcut = getPointcut(expression);
		assertTrue(pointcut.matches(Phone.class));
		assertTrue(pointcut.matches(Phone.Receiver.class));
		assertTrue(pointcut.matches(this.getClass()));
		assertTrue(pointcut.matches(methodCall, Phone.class));
		assertFalse(pointcut.matches(methodToString, Phone.class));
		
		assertFalse(pointcut.matches(methodCallFromString, Phone.class));
	}
	
	public void testMatchClass(){
		String expression = "matchclass(com.gwtent.test.aspectj.TestMatcher)";
		
		AspectJExpress pointcut = getPointcut(expression);
		assertTrue(pointcut.matches(Phone.class));
		assertFalse(pointcut.matches(this.getClass()));
		
		assertTrue(pointcut.matches(methodCall, Phone.class));
		assertFalse(pointcut.matches(methodToString, Phone.class));
	}
	
	public void testMatcherAndAspectJ(){
		String expression = "execution(* com.gwtent.client.test.aop.Phone.call(java.lang.Number))";
		
		assertTrue(Matchers.aspectjClass(expression).matches(Phone.class));
		assertTrue(Matchers.aspectjClass(expression).matches(Object.class));
		
		assertTrue(Matchers.aspectjMethod(expression).matches(this.methodCall));
		assertFalse(Matchers.aspectjMethod(expression).matches(this.methodCallFromString));
	}
	
	public void testArgsExpression(){
		String expression = "args(java.lang.Number,..)";
		
		assertTrue(Matchers.aspectjClass(expression).matches(Phone.class));
		assertTrue(Matchers.aspectjClass(expression).matches(Object.class));
		
		assertTrue(Matchers.aspectjMethod(expression).matches(this.methodCall));
		assertFalse(Matchers.aspectjMethod(expression).matches(this.methodCallFromString));
	}
	
	public void testSharedExpression(){
		
	}
	

	private AspectJExpress getPointcut(String expression) {
		AspectJExpress pointcut = new AspectJExpress();
		pointcut.setExpression(expression);
		return pointcut;
	}
	
	protected void setUp() throws Exception {
		methodCall = Phone.class.getMethod("call", java.lang.Number.class);
		methodToString = Phone.class.getMethod("toString");
		methodCallFromString = Phone.class.getMethod("call", java.lang.String.class);
	}
	
	protected void tearDown() throws Exception {
	}
}
