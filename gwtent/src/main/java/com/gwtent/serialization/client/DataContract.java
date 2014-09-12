package com.gwtent.serialization.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gwtent.reflection.client.annotations.Reflect_Full;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Reflect_Full
public @interface DataContract {
	
	/**
	 * if tagart class is a container, ie, List, Set
	 * This return the contained object class
	 * TList<Person>, here is Person.class
	 * @return
	 */
	Class<?> type() default Object.class;
}
