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


package com.gwtent.aop.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gwtent.aop.client.intercept.MethodInterceptor;
import com.gwtent.aop.matcher.ClassMethodMatcher;

public class AOPRegistor {

	/**
	 * 
	 * @param methodMatcherClassName The class name which implement MethodMatcher
	 * @param interceptors
	 * 
	 * @see ClassMethodMatcher
	 */
	public void bindInterceptor(String methodMatcherClassName,
			MethodInterceptor... interceptors) {
		methodAspects.put(methodMatcherClassName, new MethodAspect(methodMatcherClassName, interceptors));
	}
	
	public List<MethodInterceptor> getInterceptors(String matcherClassName){
		return methodAspects.get(matcherClassName).getInterceptors();
	}
	
	public Collection<String> getMatcherClassNames(){
		return methodAspects.keySet();
	}
	
	public static AOPRegistor getInstance(){
		return register;
	}
	
	private static final AOPRegistor register = new AOPRegistor();
	
	private AOPRegistor(){
		
	}
	
//	protected List<MethodAspect> getMethodAspects(){
//		return methodAspects;
//	}

	private Map<String, MethodAspect> methodAspects = new HashMap<String, MethodAspect>();
	
	private static class MethodAspect {
		final String methodMatcherClassName;
		
		public String getMethodMatcherClassName() {
			return methodMatcherClassName;
		}

		public List<MethodInterceptor> getInterceptors() {
			return interceptors;
		}

		final List<MethodInterceptor> interceptors;
		
		public MethodAspect(String methodMatcherClassName, MethodInterceptor... interceptors){
			this.methodMatcherClassName = methodMatcherClassName;
			this.interceptors = Arrays.asList(interceptors);
		}
	} 
}
