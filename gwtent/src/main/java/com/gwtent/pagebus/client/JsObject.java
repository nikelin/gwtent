package com.gwtent.pagebus.client;

import com.google.gwt.core.client.JavaScriptObject;

public class JsObject {
  protected JavaScriptObject jsObj;

  protected JsObject() {
  }

  public JsObject(JavaScriptObject jsObj) {
      this.jsObj = jsObj;
  }

  protected boolean isCreated() {
      return jsObj != null;
  }
  
  public JavaScriptObject getJsObj() {
      return jsObj;
  }

  public void setJsObj(JavaScriptObject jsObj) {
      this.jsObj = jsObj;
  }
}
