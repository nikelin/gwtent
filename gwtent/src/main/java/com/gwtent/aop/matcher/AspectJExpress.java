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
package com.gwtent.aop.matcher;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.aspectj.weaver.BCException;
import org.aspectj.weaver.reflect.ReflectionWorld;
import org.aspectj.weaver.tools.ContextBasedMatcher;
import org.aspectj.weaver.tools.FuzzyBoolean;
import org.aspectj.weaver.tools.MatchingContext;
import org.aspectj.weaver.tools.PointcutDesignatorHandler;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParameter;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.aspectj.weaver.tools.ShadowMatch;

import com.gwtent.aop.ClassFilter;
import com.gwtent.aop.Pointcut;
import com.gwtent.common.client.CheckedExceptionWrapper;


public class AspectJExpress implements Pointcut, ClassFilter, com.gwtent.aop.MethodMatcher {

	private static final Set<PointcutPrimitive> DEFAULT_SUPPORTED_PRIMITIVES = new HashSet<PointcutPrimitive>();

	static {
		DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
		DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
		DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
		DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
		DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
		DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
		DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
		DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
		DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
		DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
	}

	private String expression;
	
	private final Map<Method, ShadowMatch> shadowMapCache = new HashMap<Method, ShadowMatch>();
	 
	private PointcutParser pointcutParser;

	private Class<?> pointcutDeclarationScope;

	private String[] pointcutParameterNames = new String[0];

	private Class[] pointcutParameterTypes = new Class[0];

	private PointcutExpression pointcutExpression;
	
	private final Set<ClassMethodMatcher> methodMatchers = new HashSet<ClassMethodMatcher>();

	
	public AspectJExpress(){
		this.pointcutParser =
			PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(
					DEFAULT_SUPPORTED_PRIMITIVES);
		this.pointcutParser.registerPointcutDesignatorHandler(new MatchClassPointcutDesignatorHandler());
	}
	
	public AspectJExpress(String expression){
		this();
		this.setExpression(expression);
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("AspectJExpressionPointcut: ");
		if (this.pointcutParameterNames != null && this.pointcutParameterTypes != null) {
			sb.append("(");
			for (int i = 0; i < this.pointcutParameterTypes.length; i++) {
				sb.append(this.pointcutParameterTypes[i].getName());
				sb.append(" ");
				sb.append(this.pointcutParameterNames[i]);
				if ((i+1) < this.pointcutParameterTypes.length) {
					sb.append(", ");
				}
			}
			sb.append(")");
		}
		sb.append(" ");
		if (getExpression() != null) {
			sb.append(getExpression());
		}
		else {
			sb.append("<pointcut expression not set>");
		}
		return sb.toString();
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
	}


	public String getExpression() {
		return expression;
	}
	
	/**
	 * Return the underlying AspectJ pointcut expression.
	 */
	public PointcutExpression getPointcutExpression() {
		checkReadyToMatch();
		return this.pointcutExpression;
	}


	public boolean matches(Class<?> targetClass) {
		checkReadyToMatch();
		boolean result = false;
		try {
			result = this.pointcutExpression.couldMatchJoinPointsInType(targetClass);
		}
		catch (BCException ex) {
			//logger.debug("PointcutExpression matching rejected target class", ex);
			result = false;
		}
		
		if (result)
			return result;
		else{
			return matchesByClassMatcher(targetClass) != null;
		}	
	}

	public boolean isRuntime() {
		checkReadyToMatch();
		return this.pointcutExpression.mayNeedDynamicTest();
	}

	public boolean matches(Method method, Class<?> targetClass, Object... args) {
		checkReadyToMatch();
//		ShadowMatch shadowMatch = null;
		ShadowMatch originalShadowMatch = null;
		try {
			//shadowMatch = getShadowMatch(AopUtils.getMostSpecificMethod(method, targetClass), method);
			originalShadowMatch = getShadowMatch(method, method);
			if (originalShadowMatch.alwaysMatches()) 
				return true;
			else
				return matchesByMethodMatcher(method, targetClass, args);
				
		}
		catch (ReflectionWorld.ReflectionWorldException ex) {
			// Could neither introspect the target class nor the proxy class ->
			// let's simply consider this method as non-matching.
			return false;
		}
	}

	void addMethodMatcher(ClassMethodMatcher methodMatcher){
		methodMatchers.add(methodMatcher);
	}
	
	/**
	 * 
	 * @param clazz
	 * @return if matcher, return MethodMatcher, otherwise return null
	 */
	private ClassMethodMatcher matchesByClassMatcher(Class<?> clazz){
		for (ClassMethodMatcher methodMatcher : methodMatchers){
			if (methodMatcher.getClassMatcher().matches(clazz))
				return methodMatcher;
		}
		return null;
	}
	
	private boolean matchesByMethodMatcher(Method method, Class<?> clazz, Object[] args){
		ClassMethodMatcher methodMatcher = matchesByClassMatcher(clazz);
		if (methodMatcher != null){
			return methodMatcher.getMethodMatcher().matches(method);
		}
		
		return false;
	}
	
	private ShadowMatch getShadowMatch(Method targetMethod, Method originalMethod) {
		synchronized (this.shadowMapCache) {
			ShadowMatch shadowMatch = (ShadowMatch) this.shadowMapCache.get(targetMethod);
			if (shadowMatch == null) {
				try {
					shadowMatch = this.pointcutExpression.matchesMethodExecution(targetMethod);
				}
				catch (ReflectionWorld.ReflectionWorldException ex) {
					// Failed to introspect target method, probably because it has been loaded
					// in a special ClassLoader. Let's try the original method instead...
					if (targetMethod == originalMethod) {
						throw ex;
					}
					shadowMatch = this.pointcutExpression.matchesMethodExecution(originalMethod);
				}
				this.shadowMapCache.put(targetMethod, shadowMatch);
			}
			return shadowMatch;
		}
	}

	
	/**
	 * Check whether this pointcut is ready to match,
	 * lazily building the underlying AspectJ pointcut expression.
	 */
	private void checkReadyToMatch() {
		if (getExpression() == null) {
			throw new IllegalStateException("Must set property 'expression' before attempting to match");
		}
		if (this.pointcutExpression == null) {
			this.pointcutExpression = buildPointcutExpression();
		}
	}
	
	/**
	 * Build the underlying AspectJ pointcut expression.
	 */
	private PointcutExpression buildPointcutExpression() {
		PointcutParameter[] pointcutParameters = new PointcutParameter[this.pointcutParameterNames.length];
		for (int i = 0; i < pointcutParameters.length; i++) {
			pointcutParameters[i] = this.pointcutParser.createPointcutParameter(
					this.pointcutParameterNames[i], this.pointcutParameterTypes[i]);
		}
		return this.pointcutParser.parsePointcutExpression(
				getExpression(), this.pointcutDeclarationScope, pointcutParameters);
	}


	private class MatchClassPointcutDesignatorHandler implements PointcutDesignatorHandler {

		private static final String MATCHCLASS_DESIGNATOR_NAME = "matchclass";

		public String getDesignatorName() {
			return MATCHCLASS_DESIGNATOR_NAME;
		}

		public ContextBasedMatcher parse(String expression) {
			return new MatchClassContextMatcher(expression);
		}
	}
	
	private class MatchClassContextMatcher implements ContextBasedMatcher {

		private final String matchClassName;
		private final ClassMethodMatcher matcher;
		private final Matcher<? super Class<?>> classMatcher;
		//private final Matcher<? super Method> methodMatcher;

		public MatchClassContextMatcher(String matchClassName) {
			this.matchClassName = matchClassName;
			this.matcher = getMatcherClass();
			classMatcher = matcher.getClassMatcher();
			//methodMatcher = matcher.getMethodMatcher();
			AspectJExpress.this.addMethodMatcher(matcher);
		}

		@SuppressWarnings("unchecked")
		public boolean couldMatchJoinPointsInType(Class someClass) {
			Class<?> clazz = someClass;
			return classMatcher.matches(clazz);
			//return true;
		}

		@SuppressWarnings("unchecked")
		public boolean couldMatchJoinPointsInType(Class someClass, MatchingContext context) {
			Class<?> clazz = someClass;
			return classMatcher.matches(clazz);
		}

		public boolean matchesDynamically(MatchingContext context) {
			return true;
		}

		public FuzzyBoolean matchesStatically(MatchingContext context) {
			return contextMatch();
		}

		public boolean mayNeedDynamicTest() {
			return false;
		}

		private FuzzyBoolean contextMatch() {
//			String advisedBeanName = getCurrentProxiedBeanName();
//			if (advisedBeanName == null) { // no proxy creation in progress
//				// abstain; can't return YES, since that will make pointcut with negation fail
//				return FuzzyBoolean.MAYBE; 
//			}
//			if (BeanFactoryUtils.isGeneratedBeanName(advisedBeanName)) {
//				return FuzzyBoolean.NO;
//			}
//			if (this.expressionPattern.matches(advisedBeanName)) {
//				return FuzzyBoolean.YES;
//			}
//			if (beanFactory != null) {
//				String[] aliases = beanFactory.getAliases(advisedBeanName);
//				for (int i = 0; i < aliases.length; i++) {
//					if (this.expressionPattern.matches(aliases[i])) {
//						return FuzzyBoolean.YES;
//					}
//				}
//			}
			return FuzzyBoolean.NO;
		}
		
		private ClassMethodMatcher getMatcherClass(){
			try {
				Class<ClassMethodMatcher> classz = (Class<ClassMethodMatcher>) Class.forName(matchClassName);
				try {
					ClassMethodMatcher matcher = classz.getConstructor(null).newInstance(null);
					return matcher;
				} catch (Exception e) {
					throw new CheckedExceptionWrapper(e);
				}
			} catch (ClassNotFoundException e) {
				throw new CheckedExceptionWrapper(e);
			}
		}
	}


	public ClassFilter getClassFilter() {
		return this;
	}


	public com.gwtent.aop.MethodMatcher getMethodMatcher() {
		return this;
	}


}
