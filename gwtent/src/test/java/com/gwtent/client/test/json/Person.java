package com.gwtent.client.test.json;

import com.gwtent.serialization.client.DataContract;
import com.gwtent.serialization.client.DataMember;


@DataContract
public class Person {

	@DataMember
	private String name;
	@DataMember
	private int age;
//	@DataMember
//	private Sex sex;
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getAge() {
		return age;
	}
//	public void setSex(Sex sex) {
//		this.sex = sex;
//	}
//	public Sex getSex() {
//		return sex;
//	}
//	
	public void convertDouble(String propertyName, Double value) {
		if (propertyName.equals("age"))
			setAge(value.intValue());
	}
}
