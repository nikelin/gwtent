package com.gwtent.client.test.reflection;

import com.gwtent.reflection.client.annotations.Reflect_Full;

/**
 * 
 * @author James Luo
 *
 * 13/08/2010 4:01:08 PM
 */
public class TestReflectionGenerics {
	
	@Reflect_Full
	public static interface MyMap<T, V>{
	
		public T getT();
		public V getV();
		
		public void setT(T t);
		public void setV(V v);
	}
	
	
	public static class MyMapImpl<T, V> implements MyMap<T, V>{

		public T getT() {
			return null;
		}

		public V getV() {
			return null;
		}

		public void setT(T t) {
		}

		public void setV(V v) {
		}
	}
	
	public static class MyMapImpl2 implements MyMap<String, Object>{

		public String getT() {
			return null;
		}

		public Object getV() {
			return null;
		}

		public void setT(String t) {
		}

		public void setV(Object v) {
		}
	}
	
	

	@Reflect_Full
	public static class TestReflection1 extends TestReflection<String>{
		
	}
	
	
	@Reflect_Full
	public static class TestReflection2{
		public TestReflection<Integer> getTestAsInteger(TestReflection<String> stringValue){
			return new TestReflection<Integer>();
		}
		
		public TestReflection1 getTestAsInteger1(TestReflection<String> stringValue){
			return new TestReflection1();
		}
	}
	
}
