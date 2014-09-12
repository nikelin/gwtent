package com.gwtent.widget.client;

import com.google.gwt.user.client.ui.Widget;

public interface ValueChangedListener<T extends Object> {
	public void onValueChanged(Widget sender, T value);
}
