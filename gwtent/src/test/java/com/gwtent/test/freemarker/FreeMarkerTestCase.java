package com.gwtent.test.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import freemarker.ext.beans.BeanModel;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import junit.framework.TestCase;

public class FreeMarkerTestCase extends TestCase {
	
	public static class SourceAppender{
		private final String id;
		
		public SourceAppender(String id){
			this.id = id;
		}
		
		public String setProperty(String name, String value){
			return "Called";
		}

		public String getId() {
			return id;
		}
	}
	
	private String[] getVariable(){
		return new String[] {"ABC=DEF", "U=XYZ"};
	}
	
	public SimpleHash hashMapFromStringList(String[] strs){
		SimpleHash result = new SimpleHash();
		for (String str : strs){
			int pos = str.indexOf("=");
			if (pos >= 0){
				String key = str.substring(0, pos);
				String value = str.substring(pos + 1);
				result.put(key, value);
			}
		}
		
		result.put("btnHome", new BeanModel(new SourceAppender("btnHome"), BeansWrapper.getDefaultInstance()));
		
		return result;
	}
	
	public void testBasic() throws IOException, TemplateException{
		Configuration freemarkerCfg = new Configuration();
		freemarkerCfg.setClassForTemplateLoading(this.getClass(), "/com/gwtent/test/freemarker/");
//		freemarkerCfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
		//freemarkerCfg.setDirectoryForTemplateLoading(new File("templateDirectory"));
		Template temp = freemarkerCfg.getTemplate("testhtml.html");
		//System.out.println(temp.getRootTreeNode().toString());
    Writer out = new OutputStreamWriter(System.out);
    temp.process(hashMapFromStringList(getVariable()), out);
    out.flush(); 
	}
}
