package com.gwtent.widget.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeImages;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Tree.Resources;

/**
 * It is based gwt tree {@link com.google.gwt.user.client.ui.Tree}
 * 
 * <p>In this class, we provide more event types which you can handle.
 * {@link com.gwtent.widget.client.AdvTreeItem} implements HasAllMouseHandlers, HasClickHandlers, HasAllKeyHandlers, HasAllFocusHandlers
 * 
 * <p>And by default, this class will handle "MouseOver" and "MouseOut" event internally. If mouse over there is a CSS style called "hover" will be added to gwt-TreeItem.</p>
 * 
 * @author James Luo
 *
 * 13/07/2010 10:53:28 AM
 */
public class AdvTree extends Tree {
	
	public static interface NothingResources extends com.google.gwt.user.client.ui.Tree.Resources{

		@Source("nothing.jpg")
	  @ImageOptions(repeatStyle = RepeatStyle.Both)
		ImageResource treeClosed();

		@Source("nothing.jpg")
	  @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource treeLeaf();

		@Source("nothing.jpg")
	  @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource treeOpen();
		
	}
	
	
	public AdvTree() {
    super();
  }

  /**
   * Constructs a tree that uses the specified ClientBundle for images.
   * 
   * @param resources a bundle that provides tree specific images
   */
  public AdvTree(Resources resources) {
    super(resources);
  }
  
  /**
   * Construct a tree without showing any images
   * @return
   */
  public static AdvTree AdvTreeWithoutImages() {
  	return new AdvTree((NothingResources)GWT.create(NothingResources.class));
  }

  /**
   * Constructs a tree that uses the specified ClientBundle for images. If this
   * tree does not use leaf images, the width of the Resources's leaf image will
   * control the leaf indent.
   * 
   * @param resources a bundle that provides tree specific images
   * @param useLeafImages use leaf images from bundle
   */
  public AdvTree(Resources resources, boolean useLeafImages) {
    super(resources, useLeafImages);
  }

  /**
   * Constructs a tree that uses the specified image bundle for images.
   * 
   * @param images a bundle that provides tree specific images
   * @deprecated replaced by {@link #Tree(Resources)}
   */
  @Deprecated
  public AdvTree(TreeImages images) {
    super(images);
  }

  /**
   * Constructs a tree that uses the specified image bundle for images. If this
   * tree does not use leaf images, the width of the TreeImage's leaf image will
   * control the leaf indent.
   * 
   * @param images a bundle that provides tree specific images
   * @param useLeafImages use leaf images from bundle
   * @deprecated replaced by {@link #Tree(Resources, boolean)}
   */
  @Deprecated
  public AdvTree(TreeImages images, boolean useLeafImages) {
    super(images, useLeafImages);
  }
  
  /**
   * Sub class can override this function to create your tree item.
   * @return
   */
  protected AdvTreeItem createTreeItem(){
  	return new AdvTreeItem();
  }
  
  /**
   * Adds a simple tree item containing the specified text.
   * 
   * @param itemText the text of the item to be added
   * @return the item that was added
   */
  public TreeItem addItem(String itemText) {
  	AdvTreeItem ret = this.createTreeItem(); 
  	ret.setHTML(itemText);
    addItem(ret);

    return ret;
  }

  /**
   * Adds an item to the root level of this tree.
   * 
   * @param item the item to be added
   */
  public void addItem(TreeItem item) {
    if (item instanceof AdvTreeItem){
    	this.addItem((AdvTreeItem)item);
    }else {
    	throw new IllegalArgumentException("Must using AdvTreeItem here.");
    }
  }
  
  /**
   * Adds an item to the root level of this tree.
   * 
   * @param item the item to be added
   */
  public void addItem(AdvTreeItem item) {
    super.addItem(item);
  }

  /**
   * Adds a new tree item containing the specified widget.
   * 
   * @param widget the widget to be added
   * @return the new item
   */
  public AdvTreeItem addItem(Widget widget) {
  	AdvTreeItem item = this.createTreeItem();
  	item.setWidget(widget);
    super.addItem(item);
    return item;
  }
  
  /**
   * Give a TreeItem, return the root item of this. 
   * <p>One tree only have one Root item, so this function should always return the same node in one tree. 
   * @param item
   * @return
   */
  public TreeItem getRootTreeItem(TreeItem item){
  	TreeItem result = item.getParentItem();
  	if (result == null)
  		return item;
  	else
  		return getRootTreeItem(result);
  }
  
  /**
   * Expand item.
   * 
   * @param item
   * @param recursive
   */
  public void expand(TreeItem item, boolean recursive){
  	if (item == null){
  		if (this.getItemCount() > 0){
  			item = getRootTreeItem(this.getItem(0));
  		}
  	}
  	
  	if (item != null){
  		item.setState(true);
  		
  		if (recursive){
  			for (int i = 0; i < item.getChildCount(); i++)
  				expand(item.getChild(i), recursive);
  		}
  	}
  }

}
