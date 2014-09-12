package com.gwtent.client.test.reflection;

import com.gwtent.reflection.client.Reflectable;

@Reflectable(relationTypes=false, superClasses=true, assignableClasses=true)
public class TextBox extends com.google.gwt.user.client.ui.TextBox{
	
	public String getSomething(){
		return this.getName();
	}
	
} 
