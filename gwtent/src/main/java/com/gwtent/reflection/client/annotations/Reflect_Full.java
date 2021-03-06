package com.gwtent.reflection.client.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gwtent.reflection.client.Reflectable;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Reflectable(classAnnotations = true, fieldAnnotations = true, relationTypes=true, superClasses=true, assignableClasses=true)
public @interface Reflect_Full {

}
