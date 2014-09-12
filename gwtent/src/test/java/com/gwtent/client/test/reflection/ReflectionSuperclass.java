package com.gwtent.client.test.reflection;

import com.gwtent.reflection.client.Reflectable;

/**
 * for issue http://code.google.com/p/gwt-ent/issues/detail?id=31
 * 
 * @author James Luo
 *
 */
public class ReflectionSuperclass {
	@Reflectable
	public static class TestReflection {}

	@Reflectable 
	public static class TestExtendedReflection extends TestReflection {}
	
}
