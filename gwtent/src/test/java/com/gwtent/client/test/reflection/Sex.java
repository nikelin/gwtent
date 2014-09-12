package com.gwtent.client.test.reflection;

import com.gwtent.reflection.client.Reflectable;

@Reflectable
public enum Sex {
	MALE("Male"),
  FEMALE("Female");
	
  private final String name;
  
  private Sex(String name){
  	this.name = name;
  }
  
  public String toString(){
  	return name;
  }
  
  public String getName(){
  	return name;
  }
}
