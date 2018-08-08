package com.fanneng.android.web.js;

import android.os.Build;
import android.webkit.JavascriptInterface;

import com.fanneng.android.web.SuperWebX5;
import com.fanneng.android.web.SuperWebX5Config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * JS实现
 */

public abstract class JsBaseInterfaceHolder implements JsInterfaceHolder{

    private SuperWebX5.SecurityType mSecurityType;
    protected JsBaseInterfaceHolder(SuperWebX5.SecurityType securityType){
      this.mSecurityType =securityType;
    }
    @Override
    public boolean checkObject(Object v) {

        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.JELLY_BEAN_MR1)
            return true;
        if(SuperWebX5Config.WEBVIEW_TYPE== SuperWebX5Config.WEBVIEW_SUPERWEB_SAFE_TYPE)
            return true;
        boolean tag=false;
        Class clazz=v.getClass();

        Method[] mMethods= clazz.getMethods();

        for(Method mMethod:mMethods){

            Annotation[]mAnnotations= mMethod.getAnnotations();

            for(Annotation mAnnotation:mAnnotations){

                if(mAnnotation instanceof JavascriptInterface){
                    tag=true;
                    break;
                }

            }
            if(tag)
                break;
        }

        return tag;
    }

    protected boolean checkSecurity(){
        return mSecurityType!= SuperWebX5.SecurityType.strict ?true: SuperWebX5Config.WEBVIEW_TYPE== SuperWebX5Config.WEBVIEW_SUPERWEB_SAFE_TYPE?true: Build.VERSION.SDK_INT> Build.VERSION_CODES.JELLY_BEAN_MR1;
    }


}
