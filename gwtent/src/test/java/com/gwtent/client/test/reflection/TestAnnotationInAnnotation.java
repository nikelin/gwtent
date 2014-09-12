package com.gwtent.client.test.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;

import com.gwtent.reflection.client.annotations.Reflect_Full;

public class TestAnnotationInAnnotation {
	public static @interface AnnotationHolder{
		TestAnnotation[] annotations();
	}
	
	public static @interface TestAnnotation{
		String name();
		String value();
		float fvalue();
	}
	
	@AnnotationHolder(annotations={@TestAnnotation(name = "anno1", value = "anno1-value", fvalue=1.0F), @TestAnnotation(name = "anno2", value = "anno2-value", fvalue=2.0F)})
	@Reflect_Full
	public static class TestIt{
		
	}
	
	
	@Target(ElementType.ANNOTATION_TYPE)
	public static @interface MyParameterAnn {
	  int p1();
	  int p2() default 1;
	}
	
	
	@Target(ElementType.METHOD)
	@Inherited
	public @interface MyMethodAnn {
	  int p1();
	  MyParameterAnn pa() default @MyParameterAnn(p1 = 0);

	} 

}
