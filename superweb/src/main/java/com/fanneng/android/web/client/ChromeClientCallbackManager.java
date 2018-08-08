package com.fanneng.android.web.client;


import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.sdk.WebView;


public class ChromeClientCallbackManager {


    private ReceivedTitleCallback mReceivedTitleCallback;
    private GeoLocation mGeoLocation;

    public ReceivedTitleCallback getReceivedTitleCallback() {
        return mReceivedTitleCallback;
    }



    public ChromeClientCallbackManager setReceivedTitleCallback(ReceivedTitleCallback receivedTitleCallback) {
        mReceivedTitleCallback = receivedTitleCallback;
        return this;
    }
    public ChromeClientCallbackManager setGeoLocation(GeoLocation geoLocation){
       this.mGeoLocation=geoLocation;
        return this;
    }

    public interface ReceivedTitleCallback{
         void onReceivedTitle(WebView view, String title);
    }

    public SuperWebCompatInterface mSuperWebCompatInterface;
    public SuperWebCompatInterface getSuperWebCompatInterface(){
        return mSuperWebCompatInterface;
    }
    public void setSuperWebCompatInterface(SuperWebCompatInterface superWebCompatInterface){
        this.mSuperWebCompatInterface=superWebCompatInterface;
    }

     public interface SuperWebCompatInterface{
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result);
        public void onReceivedTitle(WebView view, String title);
        public void onProgressChanged(WebView view, int newProgress);
    }

    public static class GeoLocation {
        /*1 表示定位开启, 0 表示关闭*/
        public int tag=1;


    }
}
