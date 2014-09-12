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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.annotation.Aspect;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.gwtent.aop.ClassFilter;
import com.gwtent.aop.MethodMatcher;
import com.gwtent.aop.Pointcut;
import com.gwtent.aop.matcher.AspectJExpress;
import com.gwtent.common.client.CheckedExceptionWrapper;
import com.gwtent.gen.GenUtils;

/**
 * For AOPCreator
 * matchers from all the aspect classes
 * 
 * @author JamesLuo.au@gmail.com
 *
 */
public class AspectCollectorImpl implements Pointcut, ClassFilter, MethodMatcher, AspectCollector {

	private final TypeOracle typeOracle;
	
	private List<JClassType> aspectClasses = new ArrayList<JClassType>();
	
	private Map<JMethod, Pointcut> pointcuts = new HashMap<JMethod, Pointcut>();
	
	public AspectCollectorImpl(TypeOracle typeOracle){
		this.typeOracle = typeOracle;
		
		for (JClassType classType : typeOracle.getTypes()) {
			Aspect aspect = classType.getAnnotation(org.aspectj.lang.annotation.Aspect.class);
			if (aspect != null){
				aspectClasses.add(classType);
			}
		}
		
		for (JClassType classType : this.aspectClasses){
			for (JMethod method : classType.getMethods()){
				String expression = AOPUtils.getExpression(method);
				if ((expression != null) && (expression.length() > 0)){
						this.pointcuts.put(method, new AspectJExpress(expression));
				}
			}
		}
	}
	
	public ClassFilter getClassFilter() {
		return this;
	}

	public MethodMatcher getMethodMatcher() {
		return this;
	}

	public boolean matches(Class<?> clazz) {
		for (JMethod method : this.pointcuts.keySet()){
			try{
				if (pointcuts.get(method).getClassFilter().matches(clazz))
					return true;
			}catch (Throwable e){
				throw new CheckedExceptionWrapper("class: " + clazz.toString() + "  " + e.getMessage(), e);
			}
			
		}
		
		return false;
	}

	public boolean matches(Method method, Class<?> targetClass, Object... args) {
		for (JMethod amethod : this.pointcuts.keySet()){
			try{
				if (pointcuts.get(amethod).getMethodMatcher().matches(method, targetClass, args))
					return true;
			}catch (Throwable e){
				throw new CheckedExceptionWrapper("class: " + targetClass.toString() + "  Method: " + method.toString() + e.getMessage(), e);
			}
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.gwtent.gen.aop.AspectCollector#allMatches(com.google.gwt.core.ext.typeinfo.JMethod)
	 */
	public List<JMethod> allMatches(JMethod method){
		List<JMethod> methods = new ArrayList<JMethod>();
		
		Method javaMethod = GenUtils.gwtMethodToJavaMethod(method);
		Class<?> javaClass = GenUtils.gwtTypeToJavaClass(method.getEnclosingType());
		
		for (JMethod amethod : this.pointcuts.keySet()){
			try{
				if (pointcuts.get(amethod).getMethodMatcher().matches(javaMethod, javaClass))
					methods.add(amethod);
			}catch (Throwable e){
				throw new CheckedExceptionWrapper("class: " + method.getEnclosingType().toString() + "  Method: " + method.toString() + e.getMessage(), e);
			}
		}
		
		return methods;
	}
	
	/* (non-Javadoc)
	 * @see com.gwtent.gen.aop.AspectCollector#getPointcut()
	 */
	public Pointcut getPointcut(){
		return this;
	}

}
