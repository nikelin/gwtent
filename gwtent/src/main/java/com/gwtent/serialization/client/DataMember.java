package com.gwtent.serialization.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface DataMember {
	String name() default "";
	
	String typeName() default "";
	Class<?> type() default Object.class;
	
}
