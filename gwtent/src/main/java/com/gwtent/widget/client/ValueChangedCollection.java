package com.gwtent.widget.client;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("serial")
public class ValueChangedCollection<T extends Object> extends ArrayList<ValueChangedListener<T>> {
	 public void fireValueChanged(Widget sender, T value){
		 for (ValueChangedListener<T> l : this){
			 l.onValueChanged(sender, value);
		 }
	 }
}
