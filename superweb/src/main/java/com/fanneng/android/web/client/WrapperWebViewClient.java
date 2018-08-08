package com.fanneng.android.web.client;

import android.graphics.Bitmap;
import android.os.Message;
import android.view.KeyEvent;

import com.tencent.smtt.export.external.interfaces.ClientCertRequest;
import com.tencent.smtt.export.external.interfaces.HttpAuthHandler;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


public class WrapperWebViewClient extends WebViewClient {


    private WebViewClient mWebViewClient;
    WrapperWebViewClient (WebViewClient client){
        this.mWebViewClient=client;
    }

    void setWebViewClient(WebViewClient webViewClient){
        this.mWebViewClient=webViewClient;
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {


        if(mWebViewClient!=null){
           return mWebViewClient.shouldOverrideUrlLoading(view,url);
        }

        return false;
    }





    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

        if(mWebViewClient!=null){
             mWebViewClient.onPageStarted(view,url,favicon);
            return;
        }
        super.onPageStarted(view,url,favicon);
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        if(mWebViewClient!=null){
            mWebViewClient.onPageFinished(view,url);
            return;
        }
        super.onPageFinished(view,url);
    }


    @Override
    public void onLoadResource(WebView view, String url) {
        if(mWebViewClient!=null){
            mWebViewClient.onLoadResource(view,url);
            return;
        }
        super.onLoadResource(view,url);
    }





    @Override
    @Deprecated
    public WebResourceResponse shouldInterceptRequest(WebView view,
                                                      String url) {
        if(mWebViewClient!=null){
            return mWebViewClient.shouldInterceptRequest(view,url);
        }
        return  super.shouldInterceptRequest(view,url);
    }


    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view,
                                                      WebResourceRequest request) {

        if(mWebViewClient!=null){
            return mWebViewClient.shouldInterceptRequest(view,request);
        }
        return shouldInterceptRequest(view, request.getUrl().toString());
    }


    @Override
    @Deprecated
    public void onTooManyRedirects(WebView view, Message cancelMsg,
                                   Message continueMsg) {
        if(mWebViewClient!=null){
             mWebViewClient.onTooManyRedirects(view,cancelMsg,continueMsg);
            return;
        }
       super.onTooManyRedirects(view,cancelMsg,continueMsg);
    }



    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {

        if(mWebViewClient!=null){
            mWebViewClient.onReceivedError(view,errorCode,description,failingUrl);
            return;
        }
        super.onReceivedError(view,errorCode,description,failingUrl);
    }


    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

        if(mWebViewClient!=null){
            mWebViewClient.onReceivedError(view,request,error);
            return;
        }

        super.onReceivedError(view,request,error);
    }

    @Override
    public void onReceivedHttpError(
            WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {

        if(mWebViewClient!=null){
            mWebViewClient.onReceivedHttpError(view,request,errorResponse);
            return;
        }
        super.onReceivedHttpError(view,request,errorResponse);


    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend,
                                   Message resend) {

        if(mWebViewClient!=null){
            mWebViewClient.onFormResubmission(view,dontResend,resend);
            return;
        }
        super.onFormResubmission(view,dontResend,resend);
    }


    @Override
    public void doUpdateVisitedHistory(WebView view, String url,
                                       boolean isReload) {

        if(mWebViewClient!=null){
            mWebViewClient.doUpdateVisitedHistory(view,url,isReload);
            return;
        }
        super.doUpdateVisitedHistory(view,url,isReload);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                   SslError error) {
        if(mWebViewClient!=null){
            mWebViewClient.onReceivedSslError(view,handler,error);
            return;
        }
        super.onReceivedSslError(view,handler,error);
    }


    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        if(mWebViewClient!=null){
            mWebViewClient.onReceivedClientCertRequest(view,request);
            return;
        }
        super.onReceivedClientCertRequest(view,request);
    }


    public void onReceivedHttpAuthRequest(WebView view,
                                          HttpAuthHandler handler, String host, String realm) {
        if(mWebViewClient!=null){
            mWebViewClient.onReceivedHttpAuthRequest(view,handler,host,realm);
            return;
        }
        super.onReceivedHttpAuthRequest(view,handler,host,realm);
    }


    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        if(mWebViewClient!=null){
           return mWebViewClient.shouldOverrideKeyEvent(view,event);

        }

        return super.shouldOverrideKeyEvent(view,event);
    }


    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {

        if(mWebViewClient!=null){
            mWebViewClient.onUnhandledKeyEvent(view,event);
            return;
        }
        super.onUnhandledKeyEvent(view,event);
    }





    public void onScaleChanged(WebView view, float oldScale, float newScale) {

        if(mWebViewClient!=null){
            mWebViewClient.onScaleChanged(view,oldScale,newScale);
            return;
        }
        super.onScaleChanged(view,oldScale,newScale);
    }


    public void onReceivedLoginRequest(WebView view, String realm,
                                       String account, String args) {

        if(mWebViewClient!=null){
            mWebViewClient.onReceivedLoginRequest(view,realm,account,args);
            return;
        }
        super.onReceivedLoginRequest(view,realm,account,args);
    }
}
