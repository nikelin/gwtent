package com.gwtent.widget.client;


public class RadioBoolSelector extends RadioSelector<Boolean> {
	public RadioBoolSelector(){
		this.addStyleName("gwtent-RadioBoolSelector");
		
		this.addValue(Boolean.TRUE, "Yes");
		this.addValue(Boolean.FALSE, "No");
	}
}
