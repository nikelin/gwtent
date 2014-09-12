/*******************************************************************************
 *  Copyright 2001, 2007 JamesLuo(JamesLuo.au@gmail.com)
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *  
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 * 
 *  Contributors:
 *******************************************************************************/


package com.gwtent.gen.aop;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.gwtent.gen.GenExclusion;
import com.gwtent.gen.GenExclusionCompositeImpl;
import com.gwtent.gen.GenUtils;

public class GenExclusionCompositeAOP extends GenExclusionCompositeImpl {
	private final AspectCollector aspectCollector;
	
	private GenExclusionCompositeAOP(AspectCollector aspectCollector){
		this.aspectCollector = aspectCollector;
		
		addGenExclusion(new GenExclusion(){

			public boolean exclude(JClassType classType) {
				if (classType.isInterface() != null)
					return true;
				
				//For now, comment these, next time we need found a way to AOP without Aspectable interface.
//				if (! GenExclusionCompositeAOP.this.aspectCollector.getPointcut().getClassFilter().matches(GenUtils.GWTTypeToClass(classType)))
//					return true;
				
				return false;
			}
			
		});
	}
	
	private static GenExclusionCompositeAOP INSTANCE = null;
	
	public static GenExclusionCompositeAOP getInstance(AspectCollector aspectCollector){
		if (INSTANCE == null)
			INSTANCE = new GenExclusionCompositeAOP(aspectCollector);
		
		return INSTANCE;
	}
}
