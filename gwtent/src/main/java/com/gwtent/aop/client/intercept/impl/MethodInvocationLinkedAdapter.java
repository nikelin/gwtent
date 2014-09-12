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

import java.util.ArrayList;
import java.util.List;

import com.gwtent.aop.client.intercept.MethodInterceptor;
import com.gwtent.reflection.client.Method;

/**
 * Create a command chain that put all interceptors in.
 * 
 * @author JamesLuo.au@gmail.com
 *
 */
public class MethodInvocationLinkedAdapter extends MethodInvocationAdapter {

	private int invokeCount = 0;
	
	private List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
	
	public MethodInvocationLinkedAdapter(Method method,
			Object enclosingObject, List<MethodInterceptor> interceptors) {
		super(method, enclosingObject);
		this.interceptors.addAll(interceptors);
	}

	public Object proceed() throws Throwable {
		invokeCount++;
		
		return interceptors.get(invokeCount - 1).invoke(this);
	}
	
	public void reset(Object[] args){
		super.reset(args);
		invokeCount = 0;
	}
	
	public MethodInterceptor getCurrentInterceptor(){
		if (invokeCount <= 0)
			return interceptors.get(invokeCount);
		else
			return interceptors.get(invokeCount - 1);
	}

}
