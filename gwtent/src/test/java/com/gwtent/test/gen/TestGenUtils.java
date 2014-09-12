package com.gwtent.test.gen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.aspectj.lang.annotation.Aspect;

import com.gwtent.gen.GenUtils;
import com.gwtent.reflection.client.Reflectable;

import junit.framework.TestCase;

public class TestGenUtils extends TestCase {

  @Reflectable
  public class A {
    
  }
  
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Reflectable
  public @interface MyReflectionable {

  }
  
  @MyReflectionable
  @Aspect
  public class B{
    
  }
  
  public void testGetClassTypeAnnotationWithMataAnnotation(){
    Reflectable result = GenUtils.getAnnotationFromAnnotation(B.class.getAnnotation(MyReflectionable.class), Reflectable.class);
    
    assertTrue(result != null);
    
    result = GenUtils.getAnnotationFromAnnotation(B.class.getAnnotation(Aspect.class), Reflectable.class);
    assertTrue(result == null);
  }
  
}
