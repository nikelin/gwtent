package com.gwtent.reflection.client.impl;

import java.util.ArrayList;
import java.util.List;

import com.gwtent.reflection.client.EnumConstant;
import com.gwtent.reflection.client.EnumType;
import com.gwtent.reflection.client.Field;

public class EnumTypeImpl<T> extends ClassTypeImpl<T> implements EnumType<T> {

	public EnumTypeImpl(final Class<T> declaringClass) {
		super(declaringClass);
	}

	public EnumType<?> isEnum() {
		return this;
	}

	public EnumConstant[] getEnumConstants() {
		if (lazyEnumConstants == null) {
      final List<EnumConstant> enumConstants = new ArrayList<EnumConstant>();
      for (final Field field : getFields()) {
        if (field.isEnumConstant() != null) {
          enumConstants.add(field.isEnumConstant());
        }
      }

      lazyEnumConstants = enumConstants.toArray(new EnumConstant[0]);
    }

    return lazyEnumConstants;
	}
	
	private EnumConstant[] lazyEnumConstants;

}
