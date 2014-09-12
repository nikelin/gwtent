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


package com.gwtent.aop.client.intercept.impl;

import com.gwtent.aop.client.intercept.MethodInvocation;
import com.gwtent.reflection.client.Method;

public abstract class MethodInvocationAdapter implements MethodInvocation {

	private final Method method;
	private Object[] args;
	private final Object enclosingObject;
		
	public MethodInvocationAdapter(Method method, Object enclosingObject){
		this.method = method;
		this.args = null;
		this.enclosingObject = enclosingObject;
	}
	
	public Method getMethod() {
		return method;
	}

	public Object[] getArguments() {
		return args;
	}

	public Object getThis() {
		return enclosingObject;
	}

	/**
	 * Reset MethodInvocation, before first invoke "proceed", 
	 * Caller must call this function first. 
	 * @param args
	 */
	public void reset(Object[] args){
		this.args = args;
	}
	
	public abstract Object proceed() throws Throwable;

}
