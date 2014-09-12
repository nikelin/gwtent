package com.gwtent.client.test.i18n;

import com.google.gwt.core.client.GWT;
import com.gwtent.client.test.common.GwtEntTestCase;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Method;
import com.gwtent.reflection.client.TypeOracle;

public class I18NTestCase extends GwtEntTestCase{

	@Override
  public String getModuleName() {
    return "com.gwtent.client.test.i18n.I18N";
  }

  public void testMessageReflection(){
  	ErrorMessages errorMessages = GWT.create(ErrorMessages.class);
  	
  	//ClassType type = TypeOracle.Instance.getClassType(ErrorMessages.class);
  	ClassType type = TypeOracle.Instance.getClassType(errorMessages.getClass());
  	assertTrue(type.getMethods().length == 1);
  	
  	Method permissionDenied = type.findMethod("permissionDenied", new String[]{"java.lang.String", "java.lang.String", "java.lang.String"});
  	String result = (String)permissionDenied.invoke(errorMessages, "James", "guest", "delete");
  	assertTrue(result.equals(errorMessages.permissionDenied("James", "guest", "delete")));
  }
  
}
