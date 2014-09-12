package com.gwtent.widget.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.gwtent.common.client.utils.WebUtils;


public class ElementMask{
	private final Element el;
	private Element maskElement;
	public ElementMask(Element el){
		this.el = el;
	}
	
	public void mask(){
		makePositionable();
		
		el.appendChild(maskElement);
	}
	
	public void unmask(){
		
	}
	
	private void makePositionable() {
    String position = DOM.getStyleAttribute(el, "position");
    if (position.equals("") || position.equals("static")) {
      DOM.setStyleAttribute(el, "position", "relative");
    }
  }

	public Element getMaskElement() {
		if (maskElement == null){
			maskElement = WebUtils.createElement("<div class='gwtent-mask'></div>");
		}
		
		return maskElement;
	}
}
