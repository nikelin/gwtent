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

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.gwtent.gen.GenExclusion;
import com.gwtent.gen.GenUtils;
import com.gwtent.gen.LogableSourceCreator;

public class AOPCreator extends LogableSourceCreator {
	
//	private Map<Method, List<String>> interceptMethods = new HashMap<Method, List<String>>();
	
	private static AspectCollector aspectCollector = null;
	
	private List<MethodCache> methodCaches = new ArrayList<MethodCache>();

	public AOPCreator(TreeLogger logger, GeneratorContext context,
			String typeName) {
		super(logger, context, typeName);
		
		if (aspectCollector == null)
			aspectCollector = new AspectCollectorImpl(typeOracle);
		
		
//		PropertyOracle propertyOracle = context.getPropertyOracle();
//		try {
//			String matcher = propertyOracle.getPropertyValue(logger, "aop.matcher.test");
//			System.out.println(matcher);
//		} catch (BadPropertyValueException e) {
//			e.printStackTrace();
//		}
	}

	protected void createSource(SourceWriter source, JClassType classType){
		
		createInterceptorMap(source, classType);
		
		source.println("private static final ClassType classType = TypeOracle.Instance.getClassType(" + classType.getSimpleSourceName() + ".class);");
		source.println();
		
		declareMethodInvocation(source);
		
		source.println();
		source.println("public " + getSimpleUnitName(classType) + "(){");
		source.indent();
		createMethodInvocation(source);
		source.outdent();
		source.println("}");
		
		overrideMethods(source, classType);
		
		source.println();
		createMethodInvocationChain(source);
	}
	
	private void createInterceptorMap(SourceWriter source, JClassType classType){
		source.println("");
		source.println("private static class InterceptorMap{");
		source.indent();
		source.println("static Map<Method, List<Method>> interceptors = new HashMap<Method, List<Method>>();");
		
		source.println("static {");
		
		source.indent();
		
		source.println("ClassType aspectClass = null;");
		source.println("Method method = null;");
		
		for (JMethod sourceMethod : classType.getMethods()){
			List<JMethod> allMatchedMethods = aspectCollector.allMatches(sourceMethod);
			if (allMatchedMethods.size() > 0){
				methodCaches.add(new MethodCache(sourceMethod, allMatchedMethods));
				
				source.println("{");
				source.println("List<Method> matchAdvices = new ArrayList<Method>();");
				for (JMethod matchedMethod : allMatchedMethods){
					source.println("aspectClass = TypeOracle.Instance.getClassType(" + matchedMethod.getEnclosingType().getQualifiedSourceName() + ".class);");
					source.println("method = aspectClass.findMethod(\"" + matchedMethod.getName() + "\", new String[]{" + GenUtils.getParamTypeNames(matchedMethod, '"') + "});");
					source.println("if (method != null)");
				  source.println("  matchAdvices.add(method);");
				}
				source.println("interceptors.put(classType.findMethod(\"" + sourceMethod.getName() + "\", new String[]{"+ GenUtils.getParamTypeNames(sourceMethod, '"') +"}), matchAdvices);");
				source.println("}");
			}
		}
		source.outdent();
		source.println("}");
			
		source.outdent();
		source.println("}");
		
		source.outdent();
	}
	
	private void declareMethodInvocation(SourceWriter source){
		for (MethodCache cache : methodCaches){
			source.println("private final MethodInvocationLinkedAdapter " + getIvnValueName(cache.getSourceMethod()) + ";");
		}
	}
	
	private void createMethodInvocation(SourceWriter source){
		source.println("Method method = null;");
		for (MethodCache cache : methodCaches){
			source.println("method = classType.findMethod(\"" + cache.getSourceMethod().getName() + "\", new String[]{"+ GenUtils.getParamTypeNames(cache.getSourceMethod(), '"') +"});");
			source.println(getIvnValueName(cache.getSourceMethod()) + " = createMethodInvocationChain(method);");
		}
	}
	
	
	private void overrideMethods(SourceWriter source, JClassType classType){
		for (MethodCache cache : methodCaches){
			String ivnValueName = getIvnValueName(cache.getSourceMethod());
			source.println(cache.getSourceMethod().toString());
		  source.println("{");
		  source.indent();
		  
		  source.println("if (" + ivnValueName + ".getCurrentInterceptor() instanceof MethodInterceptorFinalAdapter){");
		  if (GenUtils.checkIfReturnVoid(cache.getSourceMethod())){
		  	source.println("	super." + cache.getSourceMethod().getName() + "(" + GenUtils.getParamNames(cache.getSourceMethod()) + ");");
		  	source.println("  return;");
		  }
		  else
		  	source.println("	return super." + cache.getSourceMethod().getName() + "(" + GenUtils.getParamNames(cache.getSourceMethod()) + ");");
			source.println("}");
			source.println();
			source.println("Object[] args = new Object[]{" + GenUtils.getParamNames(cache.getSourceMethod()) + "};");
			source.println(ivnValueName + ".reset(args);");
			source.println("try {");
			if (GenUtils.checkIfReturnVoid(cache.getSourceMethod())){
				source.println("	" + ivnValueName + ".proceed();");
				source.println("  return;");
			}
			else
				source.println("	return (" + cache.getSourceMethod().getReturnType().getQualifiedSourceName() + ") " + ivnValueName + ".proceed();");
			source.println("} catch (Throwable e) {");
			source.println("	throw new AspectException(e);");
			source.println("}");
		  
		  source.outdent();
		  source.println("}");
		}
	}

	/**
	 * SourceWriter instantiation. Return null if the resource already exist.
	 * 
	 * @return sourceWriter
	 */
	public SourceWriter doGetSourceWriter(JClassType classType) throws Exception {
		String packageName = classType.getPackage().getName();
		String simpleName = getSimpleUnitName(classType);
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(
				packageName, simpleName);
		composer.setSuperclass(classType.getQualifiedSourceName());
		// composer.addImplementedInterface(
		composer.addImport(classType.getQualifiedSourceName());
		
		composer.addImport("com.gwtent.reflection.client.*");
		composer.addImport("java.util.*");
		composer.addImport("com.gwtent.aop.client.*");
		composer.addImport("com.gwtent.aop.client.intercept.*");
		composer.addImport("com.gwtent.aop.client.intercept.impl.*");
		composer.addImport(classType.getPackage().getName() + ".*");

		PrintWriter printWriter = context.tryCreate(logger, packageName,
				simpleName);
		if (printWriter == null) {
			return null;
		} else {
			SourceWriter sw = composer.createSourceWriter(context, printWriter);
			return sw;
		}
		
	}

	@Override
	protected String getSUFFIX() {
		return GenUtils.getAOP_SUFFIX();
	}
	
	protected GenExclusion getGenExclusion(){
		return GenExclusionCompositeAOP.getInstance(aspectCollector);
	}

	private String getIvnValueName(JMethod method){
		return "Ivn_" + MethodNameProvider.getName(method);
	}
	
	private String getParamAsSourceCode(Method method){
		String result = "new String[]{";
		
		boolean needComma = false;
		for (String type : getParamAsString(method)){
			type = "\"" + type + "\"";
			if (needComma)
				result = result + ", " + type;
			else
				result = result + type;
		}
		
		result = result + "}";
		return result;
	}
	
	private String[] getParamAsString(Method method){
		List<String> result = new ArrayList<String>();
		for (Class<?> clasz : method.getParameterTypes()){
			result.add(clasz.getCanonicalName());
		}
		return result.toArray(new String[]{});
	}
	
	private void createMethodInvocationChain(SourceWriter source){
		source.println("private MethodInvocationLinkedAdapter createMethodInvocationChain(Method method) {");
		source.indent();
		source.println("List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();");
		source.println("for (Method adviceMethod : InterceptorMap.interceptors.get(method)){");
		source.println("	interceptors.add(AdviceInstanceProvider.INSTANCE.getInstance(adviceMethod));");
		source.println("}");
		source.println("interceptors.add(new MethodInterceptorFinalAdapter());");
		source.println("return new MethodInvocationLinkedAdapter(method, this, interceptors);");
		source.outdent();
		source.println("}");
	}

	
	private static class MethodNameProvider {
		private static Map<JMethod, String> names = new HashMap<JMethod, String>();
		
		private static String findNextName(JMethod method){
			String result = method.getName();
			Integer i = 0;
			
			while (names.containsValue(result)) {
				result = method.getName() + i.toString();
			}
			return result;
		}
		
		static String getName(JMethod method){
			String result = names.get(method);
			if (result == null){
				result = findNextName(method);
				names.put(method, result);
			}
			
			return result;
		}
	}
	
	
	private class MethodCache {
		private final JMethod sourceMethod;
		private final List<JMethod> adviceMethods;
		
		public MethodCache(JMethod sourceMethod, List<JMethod> adviceMethods){
			this.sourceMethod = sourceMethod;
			this.adviceMethods = adviceMethods;
		}
		
		public JMethod getSourceMethod() {
			return sourceMethod;
		}

		public List<JMethod> getAdviceMethods() {
			return adviceMethods;
		}
	} 

}
