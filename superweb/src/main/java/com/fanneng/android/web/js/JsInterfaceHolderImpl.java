package com.fanneng.android.web.js;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.fanneng.android.web.SuperWebX5;
import com.tencent.smtt.sdk.WebView;

import java.util.Map;
import java.util.Set;

public class JsInterfaceHolderImpl extends JsBaseInterfaceHolder {

    public static JsInterfaceHolderImpl getJsInterfaceHolder(WebView webView, SuperWebX5.SecurityType securityType) {

        return new JsInterfaceHolderImpl(webView,securityType);
    }

    private WebView mWebView;
    private SuperWebX5.SecurityType mSecurityType;
    JsInterfaceHolderImpl(WebView webView, SuperWebX5.SecurityType securityType) {
        super(securityType);
        this.mWebView = webView;
        this.mSecurityType=securityType;
    }

    @Override
    public JsInterfaceHolder addJavaObjects(ArrayMap<String, Object> maps) {



        if(!checkSecurity()){
            return this;
        }
        Set<Map.Entry<String, Object>> sets = maps.entrySet();
        for (Map.Entry<String, Object> mEntry : sets) {


            Object v = mEntry.getValue();
            boolean t = checkObject(v);
            if (!t)
                throw new JsInterfaceObjectException("this object has not offer method javascript to call ,please check addJavascriptInterface annotation was be added");

            else
                addJavaObjectDirect(mEntry.getKey(), v);
        }

        return this;
    }

    @Override
    public JsInterfaceHolder addJavaObject(String k, Object v) {

        if(!checkSecurity()){
            return this;
        }
        boolean t = checkObject(v);
        if (!t)
            throw new JsInterfaceObjectException("this object has not offer method javascript to call , please check addJavascriptInterface annotation was be added");

        else
            addJavaObjectDirect(k, v);
        return this;
    }

    private JsInterfaceHolder addJavaObjectDirect(String k, Object v) {
        Log.i("Info", "k" + k + "  v:" + v);
        this.mWebView.addJavascriptInterface(v, k);
        return this;
    }


}
