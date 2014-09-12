package com.gwtent.client.test.json;

import com.google.gwt.junit.client.GWTTestCase;
import com.gwtent.client.test.common.GwtEntTestCase;
import com.gwtent.serialization.client.json.JsonSerializer;

public class SerializationTestCase extends GWTTestCase{
	public void testJson(){
		//Person to JSON string
		Person p = new Person();
		p.setName("A");
		p.setAge(50);
		//p.setSex(Sex.FEMALE);
		JsonSerializer serializer = new JsonSerializer();
		String jsonPerson = serializer.serializeObject(p);
		System.out.println(jsonPerson);

		//JSON string to Person
		serializer = new JsonSerializer();
		Person p1 = serializer.deserializeObject(jsonPerson, Person.class);
		//Test it
		assertTrue(p1.getName().equals(p.getName()));
		assertTrue(p1.getAge() == p.getAge());
		
		
		//ArrayList to JSON string
		People people = new People();
		people.add(p);
		serializer = new JsonSerializer();
		String json = serializer.serializeObject(people);
		System.out.println(json);
		
		//JSON to arraylist object
		serializer = new JsonSerializer();
		People p2 = serializer.deserializeObject(json, People.class);
		
		//test it
		assertTrue(p2.size() == 1);
		assertTrue(p2.get(0).getName().equals(p.getName()));
		assertTrue(p2.get(0).getAge() == p.getAge());
	}

	@Override
	public String getModuleName() {
		return "com.gwtent.client.test.json.Serialization";
	}
}
