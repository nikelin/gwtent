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

import com.gwtent.aop.client.intercept.MethodInterceptor;
import com.gwtent.aop.client.intercept.MethodInvocation;

/**
 * This class is the default implement of last call in AOP invoke chain.
 * 
 * Please don't use this for other purpose,
 * when a call arrive to AOP Generated class, method will check 
 * Interceptor's type, if the type assignable of this class,
 * Generated class will direct call super class's implement.
 * 
 * @author JLuo
 *
 */
public class MethodInterceptorFinalAdapter implements MethodInterceptor {


	public Object invoke(MethodInvocation invocation) throws Throwable {
		return invocation.getMethod().invoke(invocation.getThis(), invocation.getArguments());
	}
	
}
