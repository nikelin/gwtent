package com.gwtent.widget.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author James Luo
 *
 * 13/07/2010 11:37:44 AM
 */
public class AdvTreeItem extends TreeItem implements HasAllMouseHandlers, HasClickHandlers, HasAllKeyHandlers,
	HasAllFocusHandlers {
	
	protected final FocusPanel focuseWrapper = new FocusPanel();
	
	private Element contentElem;
	
	/**
   * Creates an empty tree item.
   */
  public AdvTreeItem() {
    super();
    
    contentElem = DOM.getFirstChild(this.getElement());
    
    this.addMouseOverHandler(new MouseOverHandler(){

			public void onMouseOver(MouseOverEvent event) {
				AdvTreeItem.this.setStyleName1("hover", true);
			}});
    
    this.addMouseOutHandler(new MouseOutHandler(){

			public void onMouseOut(MouseOutEvent event) {
				AdvTreeItem.this.setStyleName1("hover", false);
			}});
  }

  /**
   * Constructs a tree item with the given HTML.
   * 
   * @param html the item's HTML
   */
  public AdvTreeItem(String html) {
    this();
    
    setHTML(html);
  }
  
  /**
   * Constructs a tree item with the given <code>Widget</code>.
   * 
   * @param widget the item's widget
   */
  public AdvTreeItem(Widget widget) {
    this();
    setWidget(widget);
  }
  
  /**
   * Sub class can override this function to create your tree item.
   * @return
   */
  protected AdvTreeItem createTreeItem(){
  	return new AdvTreeItem();
  }
  
  /**
   * Adds a child tree item containing the specified text.
   * 
   * @param itemText the text to be added
   * @return the item that was added
   */
  public TreeItem addItem(String itemText) {
    AdvTreeItem ret = this.createTreeItem();
    ret.setText(itemText);
    addItem(ret);
    return ret;
  }

  /**
   * Adds another item as a child to this one.
   * 
   * @param item the item to be added
   */
  public void addItem(TreeItem item) {
  	if (item instanceof AdvTreeItem){
    	super.addItem((AdvTreeItem)item);
    }else {
    	throw new IllegalArgumentException("Must using AdvTreeItem here.");
    }
  }

  /**
   * Adds a child tree item containing the specified widget.
   * 
   * @param widget the widget to be added
   * @return the item that was added
   */
  public TreeItem addItem(Widget widget) {
    AdvTreeItem ret = this.createTreeItem();
    ret.setWidget(widget);
    addItem(ret);
    return ret;
  }
  
  
  public void setHTML(String html) {
    setWidget(new HTML(html));
  }
  
  public void setText(String text) {
    setWidget(new Label(text));
  }
  
  public void setWidget(Widget newWidget) {
  	super.setWidget(getWrapper(newWidget));
  }
  
  public Widget getWidget() {
    return this.focuseWrapper.getWidget();
  }
  
  public String getHTML() {
  	Widget w = focuseWrapper.getWidget();
  	if (w == null)
  		return "";
  	else
  		return DOM.getInnerHTML(focuseWrapper.getWidget().getElement());
  }
  
  public String getText() {
  	Widget w = focuseWrapper.getWidget();
  	if (w == null)
  		return "";
  	else
  		return DOM.getInnerText(focuseWrapper.getWidget().getElement());
  }
  
  protected Widget getWrapper(Widget widget){
  	focuseWrapper.setWidget(widget);
  	return focuseWrapper;
  }
  
  /**
   * Returns a suggested {@link Focusable} instance to use when this tree item
   * is selected. The tree maintains focus if this method returns null. By
   * default, if the tree item contains a focusable widget, that widget is
   * returned.
   * 
   * Note, the {@link Tree} will ignore this value if the user clicked on an
   * input element such as a button or text area when selecting this item.
   * 
   * @return the focusable item
   */
  protected Focusable getFocusable() {
    return this.focuseWrapper;
  }

  /**
   * Returns the widget, if any, that should be focused on if this TreeItem is
   * selected.
   * 
   * @return widget to be focused.
   * @deprecated use {@link #getFocusable()} instead
   */
  @Deprecated
  protected HasFocus getFocusableWidget() {
    return this.focuseWrapper;
  }

	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return focuseWrapper.addMouseDownHandler(handler);
	}

	public void fireEvent(GwtEvent<?> event) {
		focuseWrapper.fireEvent(event);
	}

	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return focuseWrapper.addMouseUpHandler(handler);
	}

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return focuseWrapper.addMouseOutHandler(handler);
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return focuseWrapper.addMouseOverHandler(handler);
	}

	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return focuseWrapper.addMouseMoveHandler(handler);
	}

	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return focuseWrapper.addMouseWheelHandler(handler);
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return focuseWrapper.addClickHandler(handler);
	}

	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return focuseWrapper.addKeyUpHandler(handler);
	}

	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return focuseWrapper.addKeyDownHandler(handler);
	}

	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return focuseWrapper.addKeyPressHandler(handler);
	}

	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return focuseWrapper.addFocusHandler(handler);
	}

	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return focuseWrapper.addBlurHandler(handler);
	}
	
	private void setStyleName1(String style, boolean add){
		setStyleName(contentElem, style, add);
	}
}
