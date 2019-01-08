package com.fanneng.android.web.js;

import android.os.Build;
import android.webkit.ValueCallback;

import androidx.annotation.RequiresApi;

public interface QuickCallJs {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    void quickCallJs(String method, ValueCallback<String> callback, String... params);
    void quickCallJs(String method, String... params);
    void quickCallJs(String method);
}
