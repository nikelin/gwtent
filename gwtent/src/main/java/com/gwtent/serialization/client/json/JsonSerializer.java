package com.gwtent.serialization.client.json;

import java.util.Collection;
import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.gwtent.common.client.ObjectFactory;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.Constructor;
import com.gwtent.reflection.client.Field;
import com.gwtent.reflection.client.ReflectionUtils;
import com.gwtent.reflection.client.TypeOracle;
import com.gwtent.serialization.client.AbstractDataContractSerializer;
import com.gwtent.serialization.client.DataContract;
import com.gwtent.serialization.client.DataMember;
import com.gwtent.serialization.client.DoubleConvert;

public class JsonSerializer extends AbstractDataContractSerializer{
	protected Object deserializeObject(String json, ClassType type){
		JSONValue value = JSONParser.parse(json);
		
		Constructor constructor = type.findConstructor(new String[0]);
		Object result = constructor.newInstance();
		
		deserialize(value, result, type);
		
		return result;
	}

	/**
	 * Convert value to obj 
	 * 
	 * @param value
	 * @param obj
	 * @param type the classtype of obj
	 */
	private void deserialize(JSONValue value, Object obj, ClassType type) {
		if (value instanceof JSONArray){
			
			if (obj instanceof Collection){
				//Expect @DataContract(clazz=XXXX.class), XXXX is the object class we gona create
				ObjectFactory<Object> objectFactory = new ObjectReflectionFactory(type.getAnnotation(DataContract.class));
				deserializeArray((JSONArray)value, (Collection)obj, objectFactory);
			}else{
				throw new RuntimeException("JSONArray request a Collection object to contain it.");
			}
		}else if (value instanceof JSONObject){
			deserializePureObject((JSONObject)value, obj);
		}
	}
	
	public String serializeObject(Object object, ClassType type){
		StringBuilder sb = new StringBuilder();
		
  	sb.append(serialize(object, type).toString());
		
		return sb.toString();
	}
	
	private JSONValue serialize(Object object, ClassType type){
		if (object instanceof Map){
			return null;
		}else	if (object instanceof Iterable){
			return serializeIterable((Iterable)object);
		} else {
			return serializePureObject(object, type);
		}
	}
	
	private void deserializeArray(JSONArray array, Collection object, ObjectFactory<Object> objectFactory){
		for (int i = 0; i < array.size(); i++){
			Object objectItem = objectFactory.getObject();
			deserializePureObject((JSONObject)(array.get(i)), objectItem);
			object.add(objectItem);
		}
	}
	
	private void deserializePureObject(JSONObject value, Object obj){
		ClassType type = TypeOracle.Instance.getClassType(obj.getClass());
		for (Field field : type.getFields()){
			
			if (value.containsKey(field.getName())){
				JSONValue fieldValue = value.get(field.getName());
					
				if (fieldValue instanceof JSONObject){
					
				}else{
					Object fieldObject = null;
					if (fieldValue instanceof JSONNull)
						fieldObject = null;
					else if (fieldValue instanceof JSONBoolean)
						fieldObject = ((JSONBoolean)fieldValue).booleanValue();
					else if (fieldValue instanceof JSONNumber)
						fieldObject = ((JSONNumber)fieldValue).doubleValue();
					else if (fieldValue instanceof JSONString) 
						fieldObject = ((JSONString)fieldValue).stringValue();
					
					if (fieldValue instanceof JSONNumber){
						handleDouble(obj, field, fieldValue);
					}
					else
						field.setFieldValue(obj, fieldObject);
				}
			}			
		}
	}

	private void handleDouble(Object instance, Field field, JSONValue jsonValue) {
		Double doubleValue = ((JSONNumber)jsonValue).doubleValue();
		if ((instance instanceof DoubleConvert))
			((DoubleConvert)instance).convertDouble(field.getName(), doubleValue);
		else{
			//Convert double automatically
			if (field.getTypeName().equals(java.lang.Integer.class.getName()) || field.getTypeName().equals("int")){
				field.setFieldValue(instance, doubleValue.intValue());
			}else if (field.getTypeName().equals(java.lang.Float.class.getName()) || field.getTypeName().equals("long")){
				field.setFieldValue(instance, doubleValue.floatValue());
			}else if (field.getTypeName().equals(java.lang.Byte.class.getName()) || field.getTypeName().equals("byte")){
				field.setFieldValue(instance, doubleValue.byteValue());
			}else if (field.getTypeName().equals(java.lang.Short.class.getName()) || field.getTypeName().equals("short")){
				field.setFieldValue(instance, doubleValue.shortValue());
			}else if (field.getTypeName().equals(java.lang.Long.class.getName()) || field.getTypeName().equals("long")){
				field.setFieldValue(instance, doubleValue.longValue());
			}else if (field.getTypeName().equals(java.lang.Double.class.getName()) || field.getTypeName().equals("double")){
			  field.setFieldValue(instance, doubleValue);
			}
		}
	}
	
	private JSONValue serializeIterable(Iterable objects){
		JSONArray result = new JSONArray();
		int index = 0;
		for (Object obj : objects){
			result.set(index, serialize(obj, TypeOracle.Instance.getClassType(obj.getClass())));
			index++;
		}
		return result;
	}
	
	private JSONValue serializePureObject(Object object, ClassType type){
		JSONObject result = new JSONObject();
		
		for (Field field : ReflectionUtils.getAllFields(type, DataMember.class)){
			Object value = field.getFieldValue(object);
			
			if (value == null){
				result.put(field.getName(), JSONNull.getInstance());
			}else if (value instanceof Boolean){
				result.put(field.getName(), JSONBoolean.getInstance((Boolean)value));
			} else if (value instanceof Number){
				result.put(field.getName(), new JSONNumber(((Number)value).doubleValue()));
			} else{
				result.put(field.getName(), new JSONString(value.toString()));
			}
		}
		
		return result;
	}
}
