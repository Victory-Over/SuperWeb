package com.fanneng.android.web.client;


import com.tencent.smtt.sdk.WebChromeClient;

public class MiddleWareWebChromeBase extends WebChromeClientWrapper {

    private MiddleWareWebChromeBase mMiddleWareWebChromeBase;

    public MiddleWareWebChromeBase(WebChromeClient webChromeClient) {
        super(webChromeClient);
    }

    public MiddleWareWebChromeBase(){
        super(null);
    }
    @Override
    public final void setWebChromeClient(WebChromeClient webChromeClient) {
        super.setWebChromeClient(webChromeClient);
    }

    public MiddleWareWebChromeBase enq(MiddleWareWebChromeBase middleWareWebChromeBase) {
        setWebChromeClient(middleWareWebChromeBase);
        this.mMiddleWareWebChromeBase = middleWareWebChromeBase;
        return this.mMiddleWareWebChromeBase;
    }


    public MiddleWareWebChromeBase next() {
        return this.mMiddleWareWebChromeBase;
    }

}
