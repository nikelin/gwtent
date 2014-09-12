package com.gwtent.client.test.reflection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.gwtent.client.test.reflection.TestAnnotationInAnnotation.AnnotationHolder;
import com.gwtent.client.test.reflection.TestAnnotationInAnnotation.MyMethodAnn;
import com.gwtent.client.test.reflection.TestAnnotationInAnnotation.TestAnnotation;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Method;
import com.gwtent.reflection.client.ReflectionTarget;

/**
 * 
 * @author James Luo
 *
 * 24/08/2010 9:56:13 AM
 */
public class ClassTypeGenTestCase extends GWTTestCase {

  @Override
  public String getModuleName() {
    return "com.gwtent.client.test.reflection.Reflection";
  }
  
  @AnnotationHolder(annotations={@TestAnnotation(name = "anno1", value = "anno1-value", fvalue=1.0F), @TestAnnotation(name = "anno2", value = "anno2-value", fvalue=2.0F)})
  public static class ClassA{
  	private String str;

  	@MyMethodAnn(p1=100)
		public void setStr(String str) {
			this.str = str;
		}

		public String getStr() {
			return str;
		}
  }
  
  @TestAnnotation(name = "anno1", value = "anno1-value", fvalue=1.0F)
  public static class ClassB{
  	
  }
  
  @ReflectionTarget(value="com.gwtent.client.test.reflection.ClassTypeGenTestCase.ClassA")
  public static interface ClassAByAnnotation extends ClassType<ClassA>{}
  
  /**
   * At least package visible
   *
   */
  interface ClassTypeOfA extends ClassType<ClassA>{
  	
  }
  
  interface ClassTypeOfB extends ClassType<ClassB>{};
  
  public void testGen(){
  	ClassType<ClassA> typeAByAnnotation = GWT.create(ClassAByAnnotation.class);
  	checkClassA(typeAByAnnotation);
  	
  	ClassType<ClassB> typeB = GWT.create(ClassTypeOfB.class);
  	assert typeB != null;
  	TestAnnotation a = typeB.getAnnotation(TestAnnotation.class);
  	assert a != null;
  	
  	ClassType<ClassA> type = GWT.create(ClassTypeOfA.class);
  	checkClassA(type);
  }

	private void checkClassA(ClassType<ClassA> type) {
		assert type != null;
  	
  	assert type.getMethods().length == 2;
  	
  	AnnotationHolder holder = type.getAnnotation(AnnotationHolder.class);
  	assert holder != null;
  	assert holder.annotations().length == 2;
  	assert holder.annotations()[0].fvalue() == 1.0F;
  	assert holder.annotations()[1].fvalue() == 2.0F;
  	
  	Method m = type.findMethod("setStr", String.class);
  	assert m != null;
  	MyMethodAnn ann = m.getAnnotation(MyMethodAnn.class);
  	assert ann != null;
  	assert ann.p1() == 100;
	}

}
