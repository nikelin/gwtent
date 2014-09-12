package com.gwtent.client.test.common.annotations;

import com.gwtent.client.test.common.GwtEntTestCase;

public class AnnotationTestCase extends GwtEntTestCase{
	
	@Override
  public String getModuleName() {
    return "com.gwtent.client.test.common.Common";
  }
	
	public void testAnnotation(){
		UniqueConstraint u = new UniqueConstraintImpl();
		assertTrue(u.columnNames().length == 2);
	}
}
