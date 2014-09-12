package com.gwtent.common.client.utils;

/**
 * 
 * @author JamesLuo.au@gmail.com
 *
 * @param <T> the sender
 */
public interface NotifyListener<T extends Object> {
	public void onNotify(T sender);
}
