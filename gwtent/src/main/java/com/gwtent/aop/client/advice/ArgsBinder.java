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

import com.gwtent.aop.client.intercept.MethodInvocation;
import com.gwtent.reflection.client.Method;

public interface ArgsBinder {
	/**
	 * Create Argument array for invoke adviced method
	 * Using a smart way. If "args" in AspectJ expression, using this
	 * If the parameter's type is unique, match the parameter 
	 *   by this type, no matter what's the parameter name
	 * if the parameter's type have more then one, check the 
	 *   parameter's name, only match if parameter name and type is the same
	 *   
	 * Argument values: Object[] invocation.getArguments()
	 * Argument names and types: Parameter[] method.getParameters()
	 *   
	 * @param invocation MethodInvocation 
	 * @param method is the advice method (which will be invoked).
	 * @return Object[] args
	 */
	Object[] createArgs(MethodInvocation invocation, Method method, 
			Object returnValue, Throwable throwingValue);
}
