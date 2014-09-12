package com.gwtent.reflection.client.impl;

import com.gwtent.reflection.client.AnnotationType;

/**
 * 
 * @author James Luo
 */
public abstract class AnnotationTypeImpl<T> extends ClassTypeImpl<T> implements
		AnnotationType<T> {

	public AnnotationTypeImpl(Class<T> declaringClass) {
		super(declaringClass);
	}

	public AnnotationType<T> isAnnotation() {
		return this;
	}

	

}
