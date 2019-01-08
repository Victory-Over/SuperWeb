package com.fanneng.android.web.client;


import com.fanneng.android.web.SuperWebX5;
import com.tencent.smtt.sdk.WebView;

import androidx.collection.ArrayMap;


public interface WebSecurityCheckLogic {
    void dealHoneyComb(WebView view);

    void dealJsInterface(ArrayMap<String, Object> objects, SuperWebX5.SecurityType securityType);

}
