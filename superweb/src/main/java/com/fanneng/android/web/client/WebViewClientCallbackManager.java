package com.fanneng.android.web.client;

import android.graphics.Bitmap;

import com.tencent.smtt.sdk.WebView;


public class WebViewClientCallbackManager {


    private PageLifeCycleCallback mPageLifeCycleCallback;

    public PageLifeCycleCallback getPageLifeCycleCallback() {
        return mPageLifeCycleCallback;
    }

    public void setPageLifeCycleCallback(PageLifeCycleCallback pageLifeCycleCallback) {
        mPageLifeCycleCallback = pageLifeCycleCallback;
    }

    public interface PageLifeCycleCallback {

        void onPageStarted(WebView view, String url, Bitmap favicon);
        void onPageFinished(WebView view, String url);

    }
}
