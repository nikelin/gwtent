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

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.gwtent.aop.client.advice.AfterAdvice;
import com.gwtent.aop.client.advice.AfterReturningAdvice;
import com.gwtent.aop.client.advice.AfterThrowingAdvice;
import com.gwtent.aop.client.advice.AroundAdvice;
import com.gwtent.aop.client.advice.BeforeAdvice;
import com.gwtent.aop.client.intercept.MethodInterceptor;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Method;

/**
 * 1, Provid advice instance depend on @Aspect (singleton...)
 * 2, Adapte @After, @AfterReturning, @AfterThrowing, @Around,
 * 		@Before to MethodInterceptor
 * 
 * @author JLuo
 *
 */
public class AdviceInstanceProviderImpl implements AdviceInstanceProvider {

//	
//	public static class MethodInterceptorAdapter implements MethodInterceptor{
//
//		private final Method method;
//		private final Object aspectInstance;
//		
//		public MethodInterceptorAdapter(Method method, Object aspectInstance){
//			this.method = method;
//			this.aspectInstance = aspectInstance;
//		}
//		
//		public Object invoke(MethodInvocation invocation) throws Throwable {
//			Object[] args = ArgsGeneratorImpl.getInstance().createArgs(invocation, method, null);
//			if (method.isAnnotationPresent(Around.class)){
//				//if Around, put every thing to method, and let method choose how/when invoke next invocation
//				//return method.invoke(aspectInstance, new Object[]{invocation});
//				return method.invoke(aspectInstance, args);
//			}else if (method.isAnnotationPresent(Before.class)){
//				method.invoke(aspectInstance, args);
//				return invocation.proceed();
//			}else if (method.isAnnotationPresent(After.class)){
//				
//			}
//			
//			throw new RuntimeException("Advice type not supported.");
//		}		
//	}
//	
	
	/**
	 * Map<ClassType AspectClassType, Object AspectInstance>
	 */
	public Map<ClassType, Object> singletonAspects = new HashMap<ClassType, Object>();
	public Map<Method, MethodInterceptor> singletonInterceptors = new HashMap<Method, MethodInterceptor>();
	
	public MethodInterceptor getInstance(Method method) {
		MethodInterceptor result = singletonInterceptors.get(method);
		if (result == null){
			Object aspect = getAspectInstance(method.getEnclosingType());
			result = getMethodInterceptor(method, aspect);
			singletonInterceptors.put(method, result);
		}
		
		return result;
	}
	
	protected MethodInterceptor getMethodInterceptor(Method method, Object aspect){
		if (method.getAnnotation(Around.class) != null)
			return new AroundAdvice(method, aspect);
		else if (method.getAnnotation(Before.class) != null)
			return new BeforeAdvice(method, aspect);
		else if (method.getAnnotation(After.class) != null)
			return new AfterAdvice(method, aspect);
		else if (method.getAnnotation(AfterReturning.class) != null)
			return new AfterReturningAdvice(method, aspect);
		else if (method.getAnnotation(AfterThrowing.class) != null)
			return new AfterThrowingAdvice(method, aspect);
		else return null;
	}
	
	protected Object getAspectInstance(ClassType classType){
		Object result = singletonAspects.get(classType);
		
		if (result == null){
			Aspect annotation = classType.getAnnotation(Aspect.class);
			if (annotation != null){
				if ((annotation.value() == "") || ("singleton".equalsIgnoreCase(annotation.value().toString()))){
					result = classType.findConstructor(new String[]{}).newInstance();
					singletonAspects.put(classType, result);
				}
				else
					throw new RuntimeException("Now just support singleton aspect.");
			}
			
		}
		
		return result;
	}

}
