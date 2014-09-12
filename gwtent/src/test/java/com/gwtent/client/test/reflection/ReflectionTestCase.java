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

package com.gwtent.client.test.reflection;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import com.google.gwt.junit.client.GWTTestCase;
import com.gwtent.client.test.common.annotations.Entity;
import com.gwtent.client.test.common.annotations.Id;
import com.gwtent.client.test.common.annotations.Table;
import com.gwtent.client.test.reflection.ReflectionSaveSize.Anno;
import com.gwtent.client.test.reflection.ReflectionSaveSize.ClassRefereceByAnno;
import com.gwtent.client.test.reflection.ReflectionSaveSize.ThisShouldNotThere;
import com.gwtent.client.test.reflection.ReflectionSaveSize.ThisShouldThere;
import com.gwtent.client.test.reflection.ReflectionSuperclass.TestExtendedReflection;
import com.gwtent.client.test.reflection.TestAnnotationInAnnotation.MyParameterAnn;
import com.gwtent.client.test.reflection.TestReflectionGenerics.TestReflection1;
import com.gwtent.reflection.client.AnnotationStoreImpl;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Constructor;
import com.gwtent.reflection.client.Field;
import com.gwtent.reflection.client.Method;
import com.gwtent.reflection.client.Reflectable;
import com.gwtent.reflection.client.Reflection;
import com.gwtent.reflection.client.ReflectionRequiredException;
import com.gwtent.reflection.client.TypeOracle;

public class ReflectionTestCase extends GWTTestCase {

  @Override
  public String getModuleName() {
    return "com.gwtent.client.test.reflection.Reflection";
  }
  
  public void testCreateTypeOracle(){
	  ClassType classType = TypeOracle.Instance.getClassType(TestReflection.class);
	  assertNotNull(classType);
  }

  public void testObject(){
  	ClassType classType = TypeOracle.Instance.getClassType(Object.class);
  	assertNotNull(classType);
  	assertNotNull(classType.invoke(new Object(), "getClass", null));
  }
  
  public void testSuperClass(){
  	ClassType<TestReflection> ctTestReflection = TypeOracle.Instance.getClassType(TestReflection.class);
  	ClassType ctObject = TypeOracle.Instance.getClassType(Object.class);
  	assertTrue(ctTestReflection.getSuperclass() == ctObject);
  }
  
  public void testImplementsInterfaces(){
  	ClassType ctTestReflection = TypeOracle.Instance.getClassType(TestReflection.class);
  	ClassType ctReflection = TypeOracle.Instance.getClassType(Reflection.class);
  	ClassType[] types = ctTestReflection.getImplementedInterfaces();
  	boolean found = false;
  	for (ClassType type : types){
  		if (type == ctReflection){
  			found = true;
  			break;
  		}
  	}
  	assertTrue(found);
  	assertTrue(types.length == 1);
  }
  
  private boolean fieldExists(String fieldName, Field[] fields){
  	for (Field field : fields){
  		if (field.getName().equals(fieldName))
  			return true;
  	}
  	
  	return false;
  }
  
  public void testFields() {
    TestReflection test = new TestReflection();
    test.setString("username");
    assertTrue(test.getString().equals("username"));
    
    ClassType classType = TypeOracle.Instance.getClassType(TestReflection.class);
    Field[] fields = classType.getFields();
    assertTrue(fieldExists("t", fields));
    assertTrue(fieldExists("names", fields));
    assertTrue(fieldExists("bool", fields));
    assertTrue(fieldExists("sets", fields));
    //assertTrue(classType.findField("bool").getType().getSimpleSourceName().equals("boolean"));
    
    Field field = classType.findField("bool");
    field.setFieldValue(test, Boolean.TRUE);
    assertTrue((Boolean)field.getFieldValue(test));
  }

  public void testAnnotations(){
//    Annotation annotation = null;
//    assertTrue(annotation.annotationType() == null);
    
    new AnnotationStoreImpl(Entity.class, null);
    new AnnotationStoreImpl(NotNull.class, null);
    
    TestReflection test = new TestReflection();
    test.setString("username");
    assertTrue(test.getString().equals("username"));
    
    ClassType classType = TypeOracle.Instance.getClassType(TestReflection.class);
    //Class Annotations
    assertNotNull(classType.getAnnotation(Entity.class));
    assertTrue(classType.getAnnotation(Entity.class).name().equals("TestReflection"));
    assertTrue(classType.getAnnotation(Table.class).name().equals("Table_Test"));
    
    //Method Annotations
    assertNotNull(classType.findMethod("getId", new String[]{}).getAnnotation(Id.class));
    
    //Field Annotations
    assertNotNull(classType.findField("id").getAnnotation(Id.class));
    
    classType = TypeOracle.Instance.getClassType(Id.class);
    assertNotNull(classType);
    
    classType = TypeOracle.Instance.getClassType(NotNull.class);
    assertNotNull(classType);
    
    //Test annotation default value
    classType = TypeOracle.Instance.getClassType(MyParameterAnn.class);
    Method m = classType.findMethod("p2");
    assert m != null;
    assert m.getDefaultValue().toString().equals("1");
  }
  
  public void testPrimitive(){
  	ClassType classType = TypeOracle.Instance.getClassType(TestReflection.class);
  	
  	Field field = classType.findField("bool");
  	assertTrue(field.getType().isPrimitive() != null);
  	
  	field = classType.findField("ints");
  	assertTrue(int[].class != null);
  	assertTrue(int.class != null);
  	assertTrue(field != null);
  	assertTrue(field.getType() != null);
  	assertTrue(field.getType().isArray() != null);
  	assertTrue(field.getType().isArray().getComponentType().isPrimitive() != null);
  	assertTrue(field.getType().isArray().getComponentType().isPrimitive().getSimpleSourceName().equals("int"));
  	assertTrue(field.getType().isArray().getRank() == 1);
  	
  	
  	field = classType.findField("boxedInts");
  	assertTrue(field != null);
  	assertTrue(field.getType() != null);
  	assertTrue(field.getType().isArray() != null);
  	assertTrue(field.getType().isArray().getComponentType().isClass() != null);
  	assertTrue(field.getType().isArray().getComponentType().isClass().getDeclaringClass() == Integer.class);
  	

  	Method method = classType.findMethod("setBoxedInts", Integer[].class);
  	assertTrue(method != null);
  	
  	TestReflection test = new TestReflection();
  	method.invoke(test, new Object[]{new Integer[]{0, 1, 2, 3}});
  	assert test.getBoxedInts().length == 4;
  	
  	method = classType.findMethod("setInts", int[].class);
  	assertTrue(method != null);
  }
  
  private Annotation getAnnotation(Annotation[] annos, Class<? extends Annotation>clazz){
    ClassType classType = TypeOracle.Instance.getClassType(clazz);
    for (Annotation anno : annos){
      if (anno.annotationType().getName() == classType.getName())
        return anno;
    }
    
    return null;
  }

  private boolean hasMethod(String methodName, Method[] methods){
  	for (Method method : methods){
  		if (method.getName().equals(methodName))
  			return true;
  	}
  	
  	return false;
  }
  
  public void testMethods() {
  	ClassType classType = TypeOracle.Instance.getClassType(TestReflection.class);
  	Method[] methods = classType.getMethods();
  	for (Method method : methods)
  		System.out.println(method.toString());
  	System.out.println(methods.length);
  	
  	assertTrue(hasMethod("setT", methods));
  	assertTrue(hasMethod("getT", methods));
  	assertTrue(hasMethod("getString", methods));
  	assertTrue(hasMethod("getNames", methods));
  	
  	//Test find method with autobox
  	Method method = classType.findMethod("setBool", new String[]{"boolean"});
  	assertTrue(method != null);
  	//Not finished yet
  	//method = classType.findMethod("setBool", "java.lang.Boolean");
    //assertTrue(method != null);
  }

  public void testInvokeMethod() {
    TestReflection<Date> account = new TestReflection<Date>();
    account.setString("username");
    assertTrue(account.getString().equals("username"));
    
    ClassType classType = TypeOracle.Instance.getClassType(TestReflection.class);
    assertTrue(classType.invoke(account, "getString", null).equals("username"));
    classType.invoke(account, "setString", new String[]{"username set by reflection"});
    assertTrue(account.getString().equals("username set by reflection"));
    Object theString = classType.invoke(account, "getString");
    assertTrue(theString.equals("username set by reflection"));
    
    //Invoke generic functions 
    List<String> names = new ArrayList<String>();
    names.add("test1");
    names.add("test2");
    classType.invoke(account, "setNames", new Object[]{names});
    assertTrue(account.getNames().get(1).equals("test2"));
    
    Set<String> sets = new HashSet<String>();
    String string = "test1";
    sets.add(string);
    classType.invoke(account, "setSets", new Object[]{sets});
    assertTrue(account.getSets().contains(string));
    
    //Invoke generic functions which class declared
    assertTrue(account.getT() == null);
    Date date = new Date();
    classType.invoke(account, "setT", new Object[]{date});
    assertTrue(account.getT() == date);
  }

  public void testInheritance(){
  	
  }
  
  public void testConstructor(){
  	ClassType classType = TypeOracle.Instance.getClassType(TestReflection.class);
  	Constructor constructor = classType.findConstructor(new String[]{});
  	assertNotNull(constructor);
  	TestReflection obj = (TestReflection)constructor.newInstance();
  	obj.setId("test1");
  	
  	System.out.println(obj.getClass().getName());
  	System.out.println(TestReflection.class.getName());
  	assertTrue(obj.getClass().getName().equals(TestReflection.class.getName()));
  	assertTrue(obj.getId().equals("test1"));
  }
  
  public void testCustomTextBox(){
    ClassType classType = TypeOracle.Instance.getClassType(TextBox.class);
    TextBox t = new TextBox();
    t.setText("SetByCode");
    System.out.println(classType.getName());
    assertTrue(classType.getName().equals(TextBox.class.getName()));
    //t.getText();
    assertTrue(classType.invoke(t, "getText", null).equals("SetByCode"));
    
//    classType = TypeOracle.Instance.getClassType(SmartGWTComponents1.class);
//    assertTrue(classType != null);
//    
//    classType = TypeOracle.Instance.getClassType(SmartGWTComponents2.class);
//    assertTrue(classType != null);
    
//    classType = TypeOracle.Instance.getClassType(SmartGWTComponents3.class);
//    assertTrue(classType != null);
//    
//    
//    classType = TypeOracle.Instance.getClassType(SmartGWTComponents4.class);
//    assertTrue(classType != null);
//    
    
  }
  
  public void testGWTClass(){
  	Class c = TextBox.class;
  	ClassType type1= TypeOracle.Instance.getClassType(c);
  	assertTrue(type1.getName().equals(c.getName()));
  }
  
  public void testEnum(){
  	ClassType classType = TypeOracle.Instance.getClassType(Sex.class);
  	assertTrue(classType.isEnum() != null);
  	assertTrue(classType.isEnum().getEnumConstants().length == 2);
  	assertTrue(classType.isEnum().getEnumConstants()[0].getOrdinal() == Sex.FEMALE.ordinal());
  	assertTrue(classType.isEnum().getEnumConstants()[0].getName() == Sex.FEMALE.name());
  	assertTrue(classType.isEnum().getEnumConstants()[1].getOrdinal() == Sex.MALE.ordinal());
  	assertTrue(classType.isEnum().getEnumConstants()[1].getName() == Sex.MALE.name());

  	Object code = classType.getField("name").getFieldValue(Sex.FEMALE);
  	assertTrue(Sex.FEMALE.getName().equals(code.toString()));
  }
  
  
  public void testSaveSize(){
  	ClassType classType = TypeOracle.Instance.getClassType(ReflectionSaveSize.class);
  	assertNotNull(classType.findConstructor());
  	assertNotNull(classType.findField("string"));
  	assertNull(classType.findField("bool"));
  	assertNotNull(classType.findMethod("getId"));
  	assertNull(classType.findMethod("setId"));
  	
  	assertNotNull(classType.findField("thisShouldThere"));
  	assertNotNull(TypeOracle.Instance.getClassType(ThisShouldThere.class));
  	try {
			TypeOracle.Instance.getClassType(ThisShouldNotThere.class);
			fail("Should not exists");
		} catch (ReflectionRequiredException e) {
			
		}
		
		try {
			TypeOracle.Instance.getClassType(ThisShouldNotThere.class);
			fail("Should not exists");
		} catch (ReflectionRequiredException e) {
			
		}
		
		try {
			TypeOracle.Instance.getClassType(ReflectParent.class);
			fail("Should not exists");
		} catch (ReflectionRequiredException e) {
			
		}
  
  	
  	assertNotNull(TypeOracle.Instance.getClassType(Anno.class));  	
  	assertNotNull(TypeOracle.Instance.getClassType(ClassRefereceByAnno.class));
  }
  
  public void testInvokeStaticMethod(){
  	ClassType classType = TypeOracle.Instance.getClassType(TestReflection.class);
  	assertNotNull(classType.invoke(null, "getInstance"));
  }
  
  /**
   * Private class never generate reflection information
   *
   */
  private class TestReflectionInnerClass extends TestReflection{
  	
  } 
  
  public void testAnonymousClass(){
  	try {
  		TypeOracle.Instance.getClassType(TestReflectionInnerClass.class);
			fail("Should not exists");
		} catch (ReflectionRequiredException e) {
			
		}
  	
  	TestReflection r = new TestReflection(){
  		private String abc = "Anonymous Field";

  		public String toString(){
  			return "abc: " + abc + super.toString();
  		}
  		
			public void setAbc(String abc) {
				this.abc = abc;
			}

			public String getAbc() {
				return abc;
			}
  		
  	};
  	
  	System.out.println(r.getClass().getName());
  	System.out.println(TestReflectionInnerClass.class.getName());
  	
		try {
			ClassType ct = TypeOracle.Instance.getClassType(r.getClass());
			fail("");
		} catch (ReflectionRequiredException e) {
			
		}
  	//assertNull(ct.getField("abc"));
  }
  
  public void testKnowIssues(){
  	
  }
  
  public void testNoPublicParameterIssue(){
  	ClassType type = TypeOracle.Instance.getClassType(KnowIssues.KnowIssueFunctionWithNoPublicParameters_HowFix.class);
  	assertTrue(type != null);
  	
  	assertTrue(type.findMethod("getClassA") == null);
  	assertTrue(type.findMethod("setClass") == null);
  	
  	assertTrue(type.findMethod("setName", String.class) != null);
  	assertTrue(type.findMethod("getName") != null);
  	assertTrue(type.findField("name") != null);
  }
  
  public void testGenerics(){
  	ClassType type = TypeOracle.Instance.getClassType(TestReflection1.class);
  	assertTrue(type != null);
  	
  	assertTrue(type.isParameterized() == null);
  	
  	ClassType superType = type.getSuperclass();
  	
  	assertTrue(superType.isParameterized() != null);
  	
  	assertTrue(superType.isParameterized().getActualTypeArguments().length == 1);
  	assertTrue(superType.isParameterized().getActualTypeArguments()[0].isClassOrInterface().getName().equals("java.lang.String"));
  }
  
  
  //----InfiniteLoop----------------------
  public static @interface DomainClass{
    Class<?> value(); 
  }
  
  @DomainClass(TtBina.class)
  public static interface ITtBina {}

  public static class TtBina implements ITtBina, Reflection{}
  
  public void testInfiniteLoop(){
  	ClassType type = TypeOracle.Instance.getClassType(TtBina.class);
  	assert type != null;
  }

  
  @Reflectable
  class PackageAccessClass{
  	private String str;

		public void setStr(String str) {
			this.str = str;
		}

		public String getStr() {
			return str;
		}
  }
  
  public void testPackageAccess(){
  	ClassType type = TypeOracle.Instance.getClassType(PackageAccessClass.class);
  	assertTrue(type != null);
  }
  
  //issue http://code.google.com/p/gwt-ent/issues/detail?id=31
  public void testSuperclass(){
  	assert TypeOracle.Instance.getClassType(TestExtendedReflection.class).getSuperclass() != null;
  }
}
