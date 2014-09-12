package com.gwtent.widget.client;

import com.google.gwt.user.client.ui.ListBox;

public class EnumListBox<T extends Enum<T>> extends ListBox {
	
	private Class<T> clazzOfEnum;
	
	public void updateComboSelectList(Class<T> clazzOfEnum){
	  this.clazzOfEnum = clazzOfEnum;
	  
		T[] constants = clazzOfEnum.getEnumConstants();
		if (constants == null) {
      throw new IllegalArgumentException(
      		clazzOfEnum.getName() + " is not an enum.");
    }
		
    for (T constant : constants) {
        this.addItem(constant.toString(), constant.name());
    }
	}
	
	public T getSelectedValue(){
		if (clazzOfEnum == null)
			throw new RuntimeException("Please invoke updateComboSelectList first.");
		
		if (getSelectedIndex() >= 0){
			String name = getValue(getSelectedIndex());	
			
			T[] constants = clazzOfEnum.getEnumConstants();
			for (T constant : constants) {
	      if (constant.name().equals(name))
	      	return constant;
			}  
		}
		
		return null;
	}
	
}
