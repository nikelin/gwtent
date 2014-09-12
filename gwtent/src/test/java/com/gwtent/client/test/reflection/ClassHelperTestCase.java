package com.gwtent.client.test.reflection;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * 
 * @author James Luo
 *
 * 13/08/2010 2:27:45 PM
 */
public class ClassHelperTestCase extends GWTTestCase {

  @Override
  public String getModuleName() {
    return "com.gwtent.client.test.reflection.Reflection";
  }
  
  public void testClass(){
  	Class<?> clazz = int.class;
  	
  }

}
