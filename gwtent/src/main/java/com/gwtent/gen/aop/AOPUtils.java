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


package com.gwtent.gen.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import com.google.gwt.core.ext.typeinfo.JMethod;

public class AOPUtils {

	/**
	 * Get AspectJ expression from a method
	 * @param method
	 * @return the expression
	 */
	public static String getExpression(JMethod method){
		if (method.getAnnotation(Around.class) != null)
			return method.getAnnotation(Around.class).value();
		else if (method.getAnnotation(Before.class) != null)
			return method.getAnnotation(Before.class).value();
		else if (method.getAnnotation(After.class) != null)
			return method.getAnnotation(After.class).value();
		else if (method.getAnnotation(AfterReturning.class) != null){
			if (method.getAnnotation(AfterReturning.class).pointcut().length() > 0)
				return method.getAnnotation(AfterReturning.class).pointcut();
			else
				return method.getAnnotation(AfterReturning.class).value();
		}
		else if (method.getAnnotation(AfterThrowing.class) != null){
			if (method.getAnnotation(AfterThrowing.class).pointcut().length() > 0)
				return method.getAnnotation(AfterThrowing.class).pointcut();
			else
				return method.getAnnotation(AfterThrowing.class).value();
		}
		else if (method.getAnnotation(Pointcut.class) != null)
			return method.getAnnotation(Pointcut.class).value();
		else return null;
	}
	
}
