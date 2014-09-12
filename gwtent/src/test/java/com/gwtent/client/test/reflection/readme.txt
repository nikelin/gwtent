The source code of following from aopalliance
http://aopalliance.sourceforge.net/

Advice.java
AspectException.java
package intercept


We will be keep compatible with aopalliance, but because of
the source code limit of gwt, we will make some change of it.

What's the better way? ship aopalliance.jar with source code 
or copy source code to here? 


27/09/2008 we can't ship aopalliance.jar, it request java reflection which 
gwt not provided.