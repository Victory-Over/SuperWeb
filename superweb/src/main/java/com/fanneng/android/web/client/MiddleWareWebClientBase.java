package com.fanneng.android.web.client;


import com.fanneng.android.web.utils.LogUtils;
import com.tencent.smtt.sdk.WebViewClient;

public class MiddleWareWebClientBase extends WrapperWebViewClient {
    private MiddleWareWebClientBase mMiddleWrareWebClientBase;
    private String TAG = this.getClass().getSimpleName();

     MiddleWareWebClientBase(MiddleWareWebClientBase client) {
        super(client);
        this.mMiddleWrareWebClientBase = client;
    }

    MiddleWareWebClientBase(WebViewClient client) {
        super(client);
    }
    public MiddleWareWebClientBase(){
         super(null);
    }

    public MiddleWareWebClientBase next() {
        LogUtils.i(TAG, "next");
        return this.mMiddleWrareWebClientBase;
    }


    @Override
    public final void setWebViewClient(WebViewClient webViewClient) {
        super.setWebViewClient(webViewClient);

    }
     public MiddleWareWebClientBase enq(MiddleWareWebClientBase middleWrareWebClientBase){
        setWebViewClient(middleWrareWebClientBase);
        this.mMiddleWrareWebClientBase = middleWrareWebClientBase;
        return middleWrareWebClientBase;
    }


}
