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


package com.gwtent.aop.client.advice;

import com.gwtent.aop.client.intercept.MethodInterceptor;
import com.gwtent.aop.client.intercept.MethodInvocation;
import com.gwtent.common.client.Virtual;
import com.gwtent.reflection.client.Method;

public abstract class AbstractAdvice implements MethodInterceptor{

	private final Method method;
	private final Object aspectInstance;
	
	public AbstractAdvice(Method method, Object aspectInstance){
		this.method = method;
		this.aspectInstance = aspectInstance;
	}
	
	
	
	@Virtual
	public abstract Object invoke(MethodInvocation invocation) throws Throwable;
	
	/**
	 * override this function and return the ArgsBinder what you want.
	 * @return
	 */
	@Virtual
	protected ArgsBinder getArgsBinder(){
		return ArgsBinderImpl.getInstance();
	}
	
	@Virtual
	protected Object getReturningValue(){
		return null;
	}
	
	protected Object invokeAdviceMethod(MethodInvocation invocation) throws Throwable{
		return getAdviceMethod().invoke(getAspectInstance(), getArgs(invocation, getReturningValue()));
	}
	
	public Method getAdviceMethod() {
		return method;
	}


	public Object getAspectInstance() {
		return aspectInstance;
	}
	
	public Object[] getArgs(MethodInvocation invocation){
		return getArgs(invocation, null, null);
	}
	
	public Object[] getArgs(MethodInvocation invocation, Object returnValue){
		return getArgsBinder().createArgs(invocation, getAdviceMethod(), returnValue, null);
	}
	
	public Object[] getArgs(MethodInvocation invocation, Object returnValue, Throwable throwingValue){
		return getArgsBinder().createArgs(invocation, getAdviceMethod(), returnValue, throwingValue);
	}

	
}
