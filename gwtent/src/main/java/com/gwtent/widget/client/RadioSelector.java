package com.gwtent.widget.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RadioButton;
import com.gwtent.common.client.utils.WebUtils;

public class RadioSelector<T extends Object> extends FlexTable {

	public RadioSelector(){
		this.setStylePrimaryName("gwtent-RadioSelector");
		
		this.setBorderWidth(0);
	}
	
	private void doValueChanged() {
		valueChangedListeners.fireValueChanged(this, getValue());
	}
	
	public T getValue(){
		for (Data data : values){
			if (data.getRadio().isChecked())
				return data.value;
		}
		
		return null;
	}
	
	public void setValue(T value){
		setValue(value, false);
	}
	
	public void setValue(T value, boolean fireEvents) {
		for (Data data : values){
			data.getRadio().setValue(false);
		}
		
		for (Data data : values){
			if (data.getValue().equals(value))
				data.getRadio().setValue(true, fireEvents);
		}
	}
	
	
	/**
	 * Set the display label of value, by default, it's value.toString()
	 * @param value
	 * @param label
	 */
	public void setLabel(T value, String label){
		for (Data data : values){
			if (data.getValue().equals(value))
				data.setLabel(label);
		}
	}
	
	
	public void addValue(T value, String label){
		Data data = new Data(groupID, value, label);
		values.add(data);
		this.setWidget(0, values.size() - 1, data.getRadio());
	}
	
	public void addValueChangedListener(ValueChangedListener<T> listener){
		valueChangedListeners.add(listener);
	}
	
	public void removeValueChangedListener(ValueChangedListener<T> listener){
		valueChangedListeners.remove(listener);
	}
	
	public void updateByEnum(Class<T> clazzOfEnum){
		T[] constants = clazzOfEnum.getEnumConstants();
		if (constants == null) {
      throw new IllegalArgumentException(
      		clazzOfEnum.getName() + " is not an enum.");
    }
		
    for (T constant : constants) {
        this.addValue(constant, constant.toString());
    }
	}
	
	public void setEnabled(boolean enabled) {
		for (Data data : values){
			if (data.getRadio() != null)
				data.getRadio().setEnabled(enabled);
		}
	}
	
	private class Data {
		public Data(String groupID, T value, String label){
			radio = new RadioButton(groupID);
			radio.addValueChangeHandler(new ValueChangeHandler<Boolean>(){
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					doValueChanged();					
				}});
			this.setValue(value);
			this.setLabel(label);
		}
		private final RadioButton radio;
		private T value;
		private String label;
		
		public RadioButton getRadio() {
			return radio;
		}
		public void setValue(T value) {
			this.value = value;
		}
		public T getValue() {
			return value;
		}
		public void setLabel(String label) {
			this.label = label;
			radio.setText(label);
			radio.setTitle(label);
		}
		public String getLabel() {
			return label;
		}
	}
	
	private String groupID = WebUtils.getRandomElementID();
	private List<Data> values = new ArrayList<Data>();
	private ValueChangedCollection<T> valueChangedListeners = new ValueChangedCollection<T>(); 
}