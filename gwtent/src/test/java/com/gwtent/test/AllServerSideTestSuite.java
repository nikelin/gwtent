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


/**
 * FileName: AllTests.java
 * Date:			04/09/2008 3:47:02 PM
 * Author:		JamesLuo.au@gmail.com
 * purpose:
 * 
 * History:
 * 
 */


package com.gwtent.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllServerSideTestSuite extends TestCase{

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for com.gwtent.client");
    //$JUnit-BEGIN$
    
    
    //$JUnit-END$
    return suite;
  }

}
