package com.fanneng.android.web.client;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.util.ArrayMap;

import com.fanneng.android.web.SuperWebX5;
import com.fanneng.android.web.SuperWebX5Config;
import com.tencent.smtt.sdk.WebView;


public class WebSecurityLogicImpl implements WebSecurityCheckLogic {
    public static WebSecurityLogicImpl getInstance() {
        return new WebSecurityLogicImpl();
    }

    public WebSecurityLogicImpl(){}

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void dealHoneyComb(WebView view) {
        if (Build.VERSION_CODES.HONEYCOMB > Build.VERSION.SDK_INT || Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1)
            return;
        view.removeJavascriptInterface("searchBoxJavaBridge_");
        view.removeJavascriptInterface("accessibility");
        view.removeJavascriptInterface("accessibilityTraversal");
    }

    @Override
    public void dealJsInterface(ArrayMap<String, Object> objects, SuperWebX5.SecurityType securityType) {

        if (securityType== SuperWebX5.SecurityType.strict&& SuperWebX5Config.WEBVIEW_TYPE!= SuperWebX5Config.WEBVIEW_SUPERWEB_SAFE_TYPE&& Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            objects.clear();
            objects = null;
            System.gc();
        }

    }
}
