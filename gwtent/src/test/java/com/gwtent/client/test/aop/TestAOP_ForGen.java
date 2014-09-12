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


package com.gwtent.client.test.aop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gwtent.aop.client.AdviceInstanceProvider;
import com.gwtent.aop.client.AspectException;
import com.gwtent.aop.client.intercept.MethodInterceptor;
import com.gwtent.aop.client.intercept.impl.MethodInterceptorFinalAdapter;
import com.gwtent.aop.client.intercept.impl.MethodInvocationLinkedAdapter;
import com.gwtent.client.test.aop.Interceptors.PhoneLoggerInterceptor;
import com.gwtent.client.test.aop.Interceptors.PhoneRedirectInterceptor;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Method;
import com.gwtent.reflection.client.TypeOracle;

public class TestAOP_ForGen extends Phone {
	private static class InterceptorMap{
		static Map<Method, List<Method>> interceptors = new HashMap<Method, List<Method>>();
		//static List<String> matcherClassNames = null;

		static {
			ClassType aspectClass = null;
			Method method = null;
			
		  //loop... for every aspected method
			List<Method> matchAdvices = new ArrayList<Method>();
			
			aspectClass = TypeOracle.Instance.getClassType(PhoneLoggerInterceptor.class);
			method = aspectClass.findMethod("invoke", new String[]{"com.gwtent.client.aop.intercept.MethodInvocation"});
			if (method != null)  //Maybe we can found it. Private method with a Pointcut, used for share pointcut
			  matchAdvices.add(method);
			
			aspectClass = TypeOracle.Instance.getClassType(PhoneRedirectInterceptor.class);
			method = aspectClass.findMethod("invoke", new String[]{"com.gwtent.client.aop.intercept.MethodInvocation"});
			if (method != null)  //Maybe we can found it. Private method with a Pointcut, used for share pointcut
				matchAdvices.add(method);

			interceptors.put(classType.findMethod("call", new String[]{"java.lang.Number"}), matchAdvices);
			//end loop...
		}
	}
	
	private static final ClassType classType = TypeOracle.Instance.getClassType(Phone.class);
	
	private final MethodInvocationLinkedAdapter Ivn_call;
	
	public TestAOP_ForGen(){
		
		Method method = null;
		//Do loop...
		method = classType.findMethod("call", new String[]{"java.lang.Number"});
		Ivn_call = createMethodInvocationChain(method);
	}


	//TODO How To handle error?
	public Receiver call(Number number) {
		if (Ivn_call.getCurrentInterceptor() instanceof MethodInterceptorFinalAdapter){
			return super.call(number);
		}
		
		Object[] args = new Object[]{number};
		Ivn_call.reset(args);
		try {
			return (Receiver) Ivn_call.proceed();
		} catch (Throwable e) {
			throw new AspectException(e);
		}
	}
	
	private MethodInvocationLinkedAdapter createMethodInvocationChain(Method method) {
		List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
		for (Method adviceMethod : InterceptorMap.interceptors.get(method)){
			interceptors.add(AdviceInstanceProvider.INSTANCE.getInstance(adviceMethod));
		}
		interceptors.add(new MethodInterceptorFinalAdapter());
		return new MethodInvocationLinkedAdapter(method, this, interceptors);
	}
	
}
