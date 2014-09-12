package com.gwtent.client.test.reflection;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * 
 * @author James Luo
 *
 * 08/03/2011 10:22:23 AM
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
public @interface NotNull {
	String message() default "{javax.validation.constraints.NotNull.message}";

	Class<?>[] groups() default { };

	
	/**
	 * Defines several <code>@NotNull</code> annotations on the same element
	 * @see javax.validation.constraints.NotNull
	 *
	 * @author Emmanuel Bernard
	 */
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		NotNull[] value();
	}
}
