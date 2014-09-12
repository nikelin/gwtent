package com.gwtent.test.study;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * @author James Luo
 * 
 *         20/08/2010 3:37:47 PM
 */
public class VistorStudy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Visitor visitor = new ConcreteVisitor();

		StringElement stringE = new StringElement("I am a String");
		visitor.visitString(stringE);

		Collection list = new ArrayList();
		list.add(new StringElement("I am a String1"));
		list.add(new StringElement("I am a String2"));
		list.add(new FloatElement(new Float(12)));
		list.add(new StringElement("I am a String3"));
		visitor.visitCollection(list);
	}

	public static interface Visitable {
		public void accept(Visitor visitor);
	}

	public static class StringElement implements Visitable {
		private String value;

		public StringElement(String string) {
			value = string;
		}

		public String getValue() {
			return value;
		}

		// 定义accept的具体内容 这里是很简单的一句调用
		public void accept(Visitor visitor) {
			visitor.visitString(this);
		}
	}

	public static class FloatElement implements Visitable {
		private Float value;

		public FloatElement(Float value) {
			this.value = value;
		}

		public Float getValue() {
			return value;
		}

		// 定义accept的具体内容 这里是很简单的一句调用
		public void accept(Visitor visitor) {
			visitor.visitFloat(this);
		}
	}

	public static interface Visitor {

		public void visitString(StringElement stringE);

		public void visitFloat(FloatElement floatE);

		public void visitCollection(Collection collection);

	}

	public static class ConcreteVisitor implements Visitor {
		// 在本方法中,我们实现了对Collection的元素的成功访问
		public void visitCollection(Collection collection) {
			Iterator iterator = collection.iterator();
			while (iterator.hasNext()) {
				Object o = iterator.next();
				if (o instanceof Visitable)
					((Visitable) o).accept(this);
			}
		}

		public void visitString(StringElement stringE) {
			System.out.println("'" + stringE.getValue() + "'");
		}

		public void visitFloat(FloatElement floatE) {
			System.out.println(floatE.getValue().toString() + "f");
		}

	}

}
