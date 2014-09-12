package com.gwtent.serialization.client;

import com.gwtent.reflection.client.ClassType;

/**
 * 
 * @author James Luo
 *
 * 25/06/2010 3:23:12 PM
 */
public interface DataContractReflectionSerializer extends DataContractSerializer {
	public String serializeObject(Object object, ClassType type);
}
