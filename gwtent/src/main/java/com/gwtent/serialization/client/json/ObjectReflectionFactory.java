package com.gwtent.serialization.client.json;

import com.gwtent.common.client.ObjectFactory;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Constructor;
import com.gwtent.reflection.client.TypeOracle;
import com.gwtent.serialization.client.DataContract;
import com.gwtent.serialization.client.DataMember;

public class ObjectReflectionFactory implements ObjectFactory<Object> {

	/**
	 * the name of annotation method name
	 */
	private static final String ANNOTATION_TYPE_NAME = "typeName";
	private static final String ANNOTATION_TYPE = "type";
	
	private Constructor constructor;
	
	public ObjectReflectionFactory(ClassType type){
		setType(type);
	}
	
	/**
	 * The 
	 * @param annotation @DataContract and @DataMember
	 */
	public ObjectReflectionFactory(DataMember annotation){
		ClassType type = null;
		if (annotation.typeName() != null && annotation.typeName().length() > 0){
			type = TypeOracle.Instance.getClassType(annotation.typeName());
		}else if (!annotation.type().equals(Object.class)){
			type = TypeOracle.Instance.getClassType(annotation.type());
		}
		
	
		if (type != null)
			setType(type);
		else
			throw new RuntimeException("Can not found ClassType from annotation(DataContract or DataMember request \"clazz\" value when annotation a Collection(ie List)");
	}
	
	public ObjectReflectionFactory(DataContract annotation){
		ClassType type = null;
		if (!annotation.type().equals(Object.class)){
			type = TypeOracle.Instance.getClassType(annotation.type());
		}
		
		if (type != null)
			setType(type);
		else
			throw new RuntimeException("Can not found ClassType from annotation(DataContract or DataMember request \"clazz\" value when annotation a Collection(ie List)");
	}
	
	private void setType(ClassType type){
		constructor = type.findConstructor(new String[0]);
	}
	
	public Object getObject() {
		return constructor.newInstance();
	}

}
