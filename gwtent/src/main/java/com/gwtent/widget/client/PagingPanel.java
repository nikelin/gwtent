package com.gwtent.widget.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

public class PagingPanel extends Grid{
	
	public static interface PagingClickListener{
		public void onClick(final int startIndex, final int pageSize);
	}
	
	static class PagingLink extends Hyperlink{
		private int startIndex;
		private int pageSize;
		private int pageIndex;
		
		private PagingClickListener clickListener;
		
		public PagingLink(int startIndex, int pageSize, int pageIndex, ClickListener clickListener){
			super("", "");
			
			this.setStartIndex(startIndex);
			this.setPageSize(pageSize);
			this.setPageIndex(pageIndex);
			this.addClickListener(clickListener);
		}
		
		public void setStartIndex(int startIndex) {
			this.startIndex = startIndex;
		}
		public int getStartIndex() {
			return startIndex;
		}
		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}
		public int getPageSize() {
			return pageSize;
		}
		public void setPageIndex(int pageIndex) {
			this.pageIndex = pageIndex;
			
			this.setText(String.valueOf(pageIndex + 1));
		}
		
		public void setDiaplyAsDot(){
			this.setText("...");
		}
		
		public void setDiaplyAsNumber(){
			this.setText(String.valueOf(pageIndex + 1));
		}
		
		public int getPageIndex() {
			return pageIndex;
		}
		
		private void doOnClick(){
			if (clickListener != null){
				clickListener.onClick(startIndex, pageSize);
			}
		}

		public void setClickListener(PagingClickListener clickListener) {
			this.clickListener = clickListener;
		}

		public PagingClickListener getClickListener() {
			return clickListener;
		}
	}
	
	private int pageSize = 20;
	private int totalRecords;
	private int displayPages = 10;
	
	private int currentPageIndex;
	
	private List<PagingLink> links = new ArrayList<PagingLink>();
	
	private List<PagingClickListener> listeners = new ArrayList<PagingClickListener>();
	
	public PagingPanel(){
		this.setStylePrimaryName("gwtent-pagination");
		
		this.resizeRows(1);
	}
	
	/**
	 * How many records per page.
	 * 
	 * <p><strong>If changed at runtime, better to using "sizeChanged" function</p></Strong>
	 * 
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		
		sizeChanged();
	}

	/**
	 * How many records per page.
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * How many records database have
	 * 
	 * <p><strong>If changed at runtime, better to using "sizeChanged" function</p></Strong>
	 * 
	 * @param totalRecords
	 */
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
		
		sizeChanged();
	}

	/**
	 * How many records database have
	 * @return
	 */
	public int getTotalRecords() {
		return totalRecords;
	}

	/**
	 * How many page links you want display to user
	 * @param displayPages
	 */
	public void setDisplayPages(int displayPages) {
		if (displayPages < 5)
			throw new RuntimeException("Display pages at least 5 pages.");
		
		this.displayPages = displayPages;
	}

	/**
	 * How many page links you want display to user
	 * @return
	 */
	public int getDisplayPages() {
		return displayPages;
	}
	
	/**
	 * How many pages it have
	 * TotalRecords / pageSize
	 * @return
	 */
	public int getPages(){
		if (this.totalRecords % this.pageSize != 0)
			return this.totalRecords / this.pageSize + 1;
		else
			return this.totalRecords / this.pageSize;
	}
	
	private void clearElements(){
		this.resizeRows(0);
	}
	
	private enum CurrentIndexPosition {
		FirstSeg, SecSeg, LastSeg;
		
		//FirstSeg     XXXXXX...XXX
		//SecSeg       XXX...XXX...XXX
		//LastSeg      XXX...XXXXXX
	} 
	
	private void createPageLinks(){
		links.clear();
		
		for (int i = 0; i < getPages(); i++){
			links.add(createPageLink(i));
		}
	}
	
	private int getFirstPart(){
		return displayPages / 3;
	}
	
	private int getSecPart(){
		int mod = 0;
		if (displayPages % 3 > 0)
			mod = mod + 1 + 1; //first part and last part
		
		return links.size() - getFirstPart() - getLastPart() + mod;
	}
	
	private int getLastPart(){
		return links.size() - getFirstPart();
	}
	
	private void addToGrid(int row, int col, int pageLinkIndex){
		PagingLink link = links.get(pageLinkIndex);
		if (pageLinkIndex == this.currentPageIndex)
			link.addStyleName("current");
		else
			link.removeStyleName("current");
		
		this.setWidget(row, col, link);
	}
	
	private void displayTwoPart(int broken){
		for (int i = 0; i < links.size(); i++){
			links.get(i).setDiaplyAsNumber();
			
			if (i < broken){
				addToGrid(0, i, i);
			}else if (i == broken){
				links.get(i).setDiaplyAsDot();
				addToGrid(0, i, i);
			}else{
				if (i > links.size() - (displayPages - broken))
					addToGrid(0, displayPages - (links.size() - i), i);
			}
		}
	}
	
	private void displayThreePart(int currentIndex){
		//first one and last one always there
		addToGrid(0, 0, 0);
		addToGrid(0, this.displayPages - 1, links.size() - 1);
		
		int start = currentIndex - (displayPages - 1 - 1) / 2;
		if (start < 2)
			start = 2;
		
		int end = start + displayPages - 1 - 1;  //- firstone - lastone
		if (end > links.size() - 1)
			end = links.size() - 1;
		
		int displayIndex = 1;
		for (int i = start; i < end; i++){
			if (displayIndex == 1 || displayIndex == displayPages - 1 - 1)
				links.get(i).setDiaplyAsDot();
			else
				links.get(i).setDiaplyAsNumber();
			
			if (displayIndex >= 1 && displayIndex <= displayPages - 1 - 1)
				addToGrid(0, displayIndex, i);
			
			displayIndex++;
		}
	}
	
	private void updatePageLinks(int currentIndex){
		if (pageSize <= 0)
			return;
		
		if (totalRecords <= 0)
			return;
		
		if (links.size() <= this.displayPages){
			//display all in a straight way
			for (int i = 0; i < links.size(); i++){
				addToGrid(0, i, i);
			}
		}else{
			//need more calculate
			
			if (currentIndex <= getFirstPart()){
				int broken = displayPages * 2 / 3;
				displayTwoPart(broken);
			} else if (currentIndex >= getLastPart()){
				int broken = displayPages / 3;
				displayTwoPart(broken);
			}else if (currentIndex > getFirstPart() && currentIndex < getLastPart()){
				displayThreePart(currentIndex);
			}
		}
	}

	/**
	 * Better to using this function if size relation property changed.
	 * @param pageSize
	 * @param totalRecords
	 */
	public void sizeChanged(int pageSize, int totalRecords){
		this.pageSize = pageSize;
		this.totalRecords = totalRecords;
		
		this.sizeChanged();
	}
	
	/**
	 * 
	 * @param index which index we are creating
	 */
	private PagingLink createPageLink(int index) {
		PagingLink result = new PagingLink(pageSize * index, pageSize, index, new ClickListener(){

			public void onClick(Widget sender) {
				PagingLink link = (PagingLink)sender;
				setCurrentPageIndex(link.pageIndex);
				
			}});
		
		return result;
	}
	
	private void sizeChanged(){
		clearElements();
		
		if (pageSize <= 0)
			return;
		
		if (totalRecords <= 0)
			return;
		
		if (this.totalRecords > 0){
			this.resize(1, this.getDisplayPages());
			createPageLinks();
			setCurrentPageIndex(currentPageIndex, false);
		}
	}

	
	private void doPagingClickListener(int startIndex, int pageSize){
		for (PagingClickListener listener : this.listeners){
			listener.onClick(startIndex, pageSize);
		}
	}

	public void setCurrentPageIndex(int currentPageIndex) {
		setCurrentPageIndex(currentPageIndex, true);
	}
	
	public void setCurrentPageIndex(int currentPageIndex, boolean fireEvent) {
		this.currentPageIndex = currentPageIndex;
		updatePageLinks(currentPageIndex);
		if (fireEvent){
			PagingLink link = links.get(currentPageIndex);
			doPagingClickListener(link.startIndex, link.pageSize);
		}
	}

	public int getCurrentPageIndex() {
		return currentPageIndex;
	}
	
	public void addPagingListener(PagingClickListener listener){
		listeners.add(listener);
	}
	
	public void removePagingListener(PagingClickListener listener){
		listeners.remove(listener);
	}
	
}
