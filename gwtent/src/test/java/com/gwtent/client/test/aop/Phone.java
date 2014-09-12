/*******************************************************************************
 *  Copyright 2001, 2007 JamesLuo(JamesLuo.au@gmail.com)
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *  
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 * 
 *  Contributors:
 *******************************************************************************/


package com.gwtent.client.test.aop;

import java.util.HashMap;
import java.util.Map;

import com.gwtent.aop.client.Aspectable;
import com.gwtent.client.test.aop.Interceptors.AOPTestAnnotation;


public class Phone implements Aspectable {
	private static final Map<Number, Receiver> RECEIVERS = new HashMap<Number, Receiver>();

	static {
		RECEIVERS.put(123456789, new Receiver("Aunt Jane"));
		RECEIVERS.put(111111111, new Receiver("Santa"));
	}
	
	/**
	 * the phone number, like your home number
	 */
	private Number number;
	
	private final Number myNumber;
	
	public Phone(){
		myNumber = 50000;
	}

	@AOPTestAnnotation(value="abc")
	public Receiver call(Number number) {
		System.out.println("The call here...");
		System.out.println("My Number is : " + myNumber);
		Receiver result = RECEIVERS.get(number);
		if (result != null)
			return result;
		else
			throw new NumberNotFoundException("Can't  found receiver, number: " + number);
	}
	
	@AOPTestAnnotation(value="abc")
	public Receiver call(String number){
		System.out.println("The call here...");
		return RECEIVERS.get(111111111);
	}
	
	public String toString(){
		return super.toString();
	}
	

	public void setNumber(Number number) {
		this.number = number;
	}

	public Number getNumber() {
		return number;
	}
	
	public static class NumberNotFoundException extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public NumberNotFoundException(String msg){
			super(msg);
		}
		
	}


	public static class Receiver {
		private final String name;

		public Receiver(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return getClass().getName() + "[name=" + name + "]";
		}
	}
}


