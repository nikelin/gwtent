package com.gwtent.client.test.common.annotations;

import java.lang.annotation.Annotation;

public class UniqueConstraintImpl implements UniqueConstraint{

	public String[] columnNames() {
		return new String[]{"abc", "def"};
	}

	public Class<? extends Annotation> annotationType() {
		return null;
	}

}
