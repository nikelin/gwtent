package com.gwtent.reflection.client.impl;

import java.lang.annotation.Annotation;

import com.google.gwt.core.client.GWT;
import com.gwtent.reflection.client.AnnotationType;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.ReflectionRequiredException;
import com.gwtent.reflection.client.TypeOracle;

/**
 * This class designed for holder a annotation class name and values,
 * 
 * Will be used for delay create annotation
 * 
 * @author James Luo
 */
public class AnnotationValues {
	private final String annoClassName;
	private final Object[] values;

	public AnnotationValues(String annoClassName, Object[] values) {
		this.annoClassName = annoClassName;
		this.values = values;
	}

	public String getAnnoClassName() {
		return annoClassName;
	}

	public Object[] getValues() {
		return values;
	}

	public static Annotation toAnnotation(AnnotationValues ann) {
		if (ann == null)
			return null;

		String annoClassName = ann.getAnnoClassName();
		
		try {
			ClassType<? extends Annotation> type = (ClassType<? extends Annotation>) TypeOracle.Instance.getClassType(annoClassName);
			
			if (type.isAnnotation() == null){
				GWT.log(annoClassName + " not a annotation type, we will set up the annotation as null.");
				return null;
			}
			
			AnnotationType<? extends Annotation> antoType = (AnnotationType<? extends Annotation>) type;
			Annotation result = antoType.createAnnotation(ann.getValues());

			return result;
		} catch (ReflectionRequiredException e) {
			GWT.log("You are try to access annotation :" + annoClassName
					+ ", but this class don't have any reflection information yet, we will set up the annotation as 'null'.");
			return null;
		}
	}
}
