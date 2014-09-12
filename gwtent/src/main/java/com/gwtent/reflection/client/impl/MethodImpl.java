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


package com.gwtent.reflection.client.impl;

import com.gwtent.reflection.client.AbstractMethod;
import com.gwtent.reflection.client.AccessDef;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Constructor;
import com.gwtent.reflection.client.HasAnnotations;
import com.gwtent.reflection.client.Method;
import com.gwtent.reflection.client.MethodInvokeException;
import com.gwtent.reflection.client.ReflectionRequiredException;
import com.gwtent.reflection.client.ReflectionUtils;
import com.gwtent.reflection.client.Type;
import com.gwtent.reflection.client.TypeOracle;


public class MethodImpl extends AbstractMethodImpl implements AccessDef, HasAnnotations, AbstractMethod, Method {

	private final ClassTypeImpl enclosingType;
	private Object annotationDefaultValue = null;

	public MethodImpl(ClassTypeImpl enclosingType, String name) {
		super(name);
		this.enclosingType = enclosingType;
		enclosingType.addMethod(this);
	}

	public ClassType getEnclosingType() {
		return enclosingType;
	}
	
	/* (non-Javadoc)
	 * @see com.gwtent.client.reflection.Method#invoke(java.lang.Object, java.lang.Object[])
	 */
	public Object invoke(Object instance, Object[] args) throws MethodInvokeException {
		return enclosingType.invoke(instance, this.getName(), args);
	}

	private TypeImpl returnType;
	private String returnTypeName;

	/* (non-Javadoc)
	 * @see com.gwtent.client.reflection.Method#getReturnType()
	 */
	public Type getReturnType() throws ReflectionRequiredException{
		if (returnType == null){
			returnType = (TypeImpl)TypeOracle.Instance.getType(returnTypeName);
			
			if (returnType == null)
				ReflectionUtils.checkReflection(returnTypeName);
		}
		
		return returnType;
	}

	public boolean isAbstract() {
		return 0 != (getModifierBits() & TypeOracleImpl.MOD_ABSTRACT);
	}

	public boolean isFinal() {
		return 0 != (getModifierBits() & TypeOracleImpl.MOD_FINAL);
	}

	public Method isMethod() {
		return this;
	}

	public boolean isNative() {
		return 0 != (getModifierBits() & TypeOracleImpl.MOD_NATIVE);
	}

	public boolean isStatic() {
		return 0 != (getModifierBits() & TypeOracleImpl.MOD_STATIC);
	}

	

	public void setReturnType(TypeImpl type) {
		returnType = type;
	}




	public String getReadableDeclaration() {
		return getReadableDeclaration(getModifierBits());
	}

	public String getReadableDeclaration(boolean noAccess, boolean noNative,
			boolean noStatic, boolean noFinal, boolean noAbstract) {
		int bits = getModifierBits();
		if (noAccess) {
			bits &= ~(TypeOracleImpl.MOD_PUBLIC | TypeOracleImpl.MOD_PRIVATE | TypeOracleImpl.MOD_PROTECTED);
		}
		if (noNative) {
			bits &= ~TypeOracleImpl.MOD_NATIVE;
		}
		if (noStatic) {
			bits &= ~TypeOracleImpl.MOD_STATIC;
		}
		if (noFinal) {
			bits &= ~TypeOracleImpl.MOD_FINAL;
		}
		if (noAbstract) {
			bits &= ~TypeOracleImpl.MOD_ABSTRACT;
		}
		return getReadableDeclaration(bits);
	}

	String getReadableDeclaration(int modifierBits) {
		String[] names = TypeOracleImpl.modifierBitsToNames(modifierBits);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < names.length; i++) {
			sb.append(names[i]);
			sb.append(" ");
		}
		sb.append(this.returnTypeName);
		
		sb.append(" ");
		sb.append(getName());

		toStringParamsAndThrows(sb);

		return sb.toString();
	}

	public Constructor isConstructor() {
		return null;
	}

	public String getReturnTypeName() {
		return returnTypeName;
	}

	public void setReturnTypeName(String returnTypeName) {
		this.returnTypeName = returnTypeName;
	}

	public Class getDeclaringClass() {
		return this.getEnclosingType().getDeclaringClass();
	}

	public int getModifiers() {
		return this.getModifierBits();
	}
	
	public Object getDefaultValue(){
		return annotationDefaultValue;
	}
	
  public void setDefaultValue(Object annotationDefaultValue){
  	this.annotationDefaultValue = annotationDefaultValue;
  }

}