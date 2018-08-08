package com.fanneng.android.web.js;

import android.os.Handler;
import android.os.Looper;
import android.webkit.ValueCallback;

import com.tencent.smtt.sdk.WebView;


public class JsEntraceAccessImpl extends BaseJsEntraceAccess {

    private WebView mWebView;
    private Handler mHandler = new Handler(Looper.getMainLooper());


    public static JsEntraceAccessImpl getInstance(WebView webView) {
        return new JsEntraceAccessImpl(webView);
    }

    private JsEntraceAccessImpl(WebView webView) {
        super(webView);
        this.mWebView = webView;
    }



    private void callSafeCallJs(final String s, final ValueCallback valueCallback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callJs(s, valueCallback);
            }
        });
    }

    @Override
    public void callJs(String params, final ValueCallback<String> callback) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            callSafeCallJs(params, callback);
            return;
        }

        super.callJs(params,callback);

    }






   /* private void safeCallJs(final String method, final ValueCallback<String>valueCallback, final String... params){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                quickCallJs(method,valueCallback,params);
            }
        });
    }
    @Override
    public void quickCallJs(String method, ValueCallback<String> callback, String... params) {
       if(Thread.currentThread()!=Looper.getMainLooper().getThread()){
           safeCallJs(method,callback,params);
           return;
       }
        super.quickCallJs(method, callback, params);
    }
*/

}
