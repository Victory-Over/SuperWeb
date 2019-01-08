package com.fanneng.android.web.js;


import androidx.collection.ArrayMap;

public interface JsInterfaceHolder {

    JsInterfaceHolder addJavaObjects(ArrayMap<String, Object> maps);

    JsInterfaceHolder addJavaObject(String k, Object v);

    boolean checkObject(Object v) ;

}
