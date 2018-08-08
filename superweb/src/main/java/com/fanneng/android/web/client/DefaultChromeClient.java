package com.fanneng.android.web.client;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;

import com.fanneng.android.web.file.ActionActivity;
import com.fanneng.android.web.utils.DefaultMsgConfig;
import com.fanneng.android.web.file.IFileUploadChooser;
import com.fanneng.android.web.video.IVideo;
import com.fanneng.android.web.progress.IndicatorController;
import com.fanneng.android.web.R;
import com.fanneng.android.web.SuperWebX5Config;
import com.fanneng.android.web.SuperWebX5Permissions;
import com.fanneng.android.web.file.FileUpLoadChooserImpl;
import com.fanneng.android.web.file.FileUploadPop;
import com.fanneng.android.web.utils.PermissionInterceptor;
import com.fanneng.android.web.utils.LogUtils;
import com.fanneng.android.web.utils.SuperWebX5Utils;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebStorage;
import com.tencent.smtt.sdk.WebView;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.fanneng.android.web.file.ActionActivity.KEY_FROM_INTENTION;

public class DefaultChromeClient extends MiddleWareWebChromeBase implements FileUploadPop<IFileUploadChooser> {


    private WeakReference<Activity> mActivityWeakReference = null;
    private AlertDialog promptDialog = null;
    private AlertDialog confirmDialog = null;
    private JsPromptResult pJsResult = null;
    private JsResult cJsResult = null;
    private String TAG = DefaultChromeClient.class.getSimpleName();
    private ChromeClientCallbackManager mChromeClientCallbackManager;
    public static final String ChromePath = "com.tencent.smtt.sdk.WebChromeClient";
    private WebChromeClient mWebChromeClient;
    private boolean isWrapper = false;
    private IFileUploadChooser mIFileUploadChooser;
    private IVideo mIVideo;
    private DefaultMsgConfig.ChromeClientMsgCfg mChromeClientMsgCfg;
    private PermissionInterceptor mPermissionInterceptor;
    private WebView mWebView;
    private String origin = null;
    private GeolocationPermissionsCallback mCallback = null;
    public static final int FROM_CODE_INTENTION = 0x18;
    public static final int FROM_CODE_INTENTION_LOCATION = FROM_CODE_INTENTION << 2;
    private IndicatorController mIndicatorController;

    public DefaultChromeClient(Activity activity,
                               IndicatorController indicatorController,
                               WebChromeClient chromeClient,
                               ChromeClientCallbackManager chromeClientCallbackManager,
                               @Nullable IVideo iVideo,
                               DefaultMsgConfig.ChromeClientMsgCfg chromeClientMsgCfg, PermissionInterceptor permissionInterceptor, WebView webView) {
        super( chromeClient);
        this.mIndicatorController=indicatorController;
        isWrapper = chromeClient != null ? true : false;
        this.mWebChromeClient = chromeClient;
        mActivityWeakReference = new WeakReference<Activity>(activity);
        this.mChromeClientCallbackManager = chromeClientCallbackManager;
        this.mIVideo = iVideo;
        this.mChromeClientMsgCfg = chromeClientMsgCfg;
        this.mPermissionInterceptor = permissionInterceptor;
        this.mWebView = webView;
    }





    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);

        if(this.mIndicatorController!=null){
            this.mIndicatorController.progress(view,newProgress);
        }
        ChromeClientCallbackManager.SuperWebCompatInterface mSuperWebCompatInterface = null;
        if (SuperWebX5Config.WEBVIEW_TYPE == SuperWebX5Config.WEBVIEW_SUPERWEB_SAFE_TYPE && mChromeClientCallbackManager != null && (mSuperWebCompatInterface = mChromeClientCallbackManager.getSuperWebCompatInterface()) != null) {
            mSuperWebCompatInterface.onProgressChanged(view, newProgress);
        }

    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        ChromeClientCallbackManager.ReceivedTitleCallback mCallback = null;
        if (mChromeClientCallbackManager != null && (mCallback = mChromeClientCallbackManager.getReceivedTitleCallback()) != null)
            mCallback.onReceivedTitle(view, title);

        if (SuperWebX5Config.WEBVIEW_TYPE == SuperWebX5Config.WEBVIEW_SUPERWEB_SAFE_TYPE && mChromeClientCallbackManager != null && (mChromeClientCallbackManager.getSuperWebCompatInterface()) != null)
            mChromeClientCallbackManager.getSuperWebCompatInterface().onReceivedTitle(view, title);
        if (isWrapper)
            super.onReceivedTitle(view, title);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {


        if (SuperWebX5Utils.isOverriedMethod(mWebChromeClient, "onJsAlert", "public boolean " + ChromePath + ".onJsAlert", WebView.class, String.class, String.class, JsResult.class)) {

            return super.onJsAlert(view, url, message, result);
        }

        Activity mActivity = this.mActivityWeakReference.get();
        if (mActivity == null||mActivity.isFinishing()) {
            result.cancel();
            return true;
        }
        //
        try {
            SuperWebX5Utils.show(view,
                    message,
                    Snackbar.LENGTH_SHORT,
                    Color.WHITE,
                    mActivity.getResources().getColor(R.color.black),
                    null,
                    -1,
                    null);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            if(LogUtils.isDebug())
                LogUtils.i(TAG,throwable.getMessage());
        }

        result.confirm();

        return true;
    }



    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
        LogUtils.i(TAG, "onGeolocationPermissionsHidePrompt");
    }

    //location
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissionsCallback callback) {

        LogUtils.i(TAG, "onGeolocationPermissionsShowPrompt:" + origin + "   callback:" + callback);
        if (SuperWebX5Utils.isOverriedMethod(mWebChromeClient, "onGeolocationPermissionsShowPrompt", "public void " + ChromePath + ".onGeolocationPermissionsShowPrompt", String.class, GeolocationPermissionsCallback.class)) {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
            return;
        }
        onGeolocationPermissionsShowPromptInternal(origin, callback);
    }



    private void onGeolocationPermissionsShowPromptInternal(String origin, GeolocationPermissionsCallback callback) {

        if (mPermissionInterceptor != null) {
            if (mPermissionInterceptor.intercept(this.mWebView.getUrl(), SuperWebX5Permissions.LOCATION, "location")) {
                callback.invoke(origin, false, false);
                return;
            }
        }

        Activity mActivity = mActivityWeakReference.get();
        if (mActivity == null) {
            callback.invoke(origin, false, false);
            return;
        }

        List<String> deniedPermissions = null;
        if ((deniedPermissions = SuperWebX5Utils.getDeniedPermissions(mActivity, SuperWebX5Permissions.LOCATION)).isEmpty()) {
            callback.invoke(origin, true, false);
        } else {

            ActionActivity.Action mAction = ActionActivity.Action.createPermissionsAction(deniedPermissions.toArray(new String[]{}));
            mAction.setFromIntention(FROM_CODE_INTENTION_LOCATION);
            ActionActivity.setPermissionListener(mPermissionListener);
            this.mCallback = callback;
            this.origin = origin;
            ActionActivity.start(mActivity, mAction);
        }


    }

    private ActionActivity.PermissionListener mPermissionListener = new ActionActivity.PermissionListener() {
        @Override
        public void onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults, Bundle extras) {


            if (extras.getInt(KEY_FROM_INTENTION) == FROM_CODE_INTENTION_LOCATION) {
                boolean t = true;
                for (int p : grantResults) {
                    if (p != PackageManager.PERMISSION_GRANTED) {
                        t = false;
                        break;
                    }
                }

                if (mCallback != null) {
                    if (t) {
                        mCallback.invoke(origin, true, false);
                    } else {
                        mCallback.invoke(origin, false, false);
                    }

                    mCallback = null;
                    origin = null;
                }

            }

        }
    };

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {


        try {
            if (SuperWebX5Utils.isOverriedMethod(mWebChromeClient, "onJsPrompt", "public boolean " + ChromePath + ".onJsPrompt", WebView.class, String.class, String.class, String.class, JsPromptResult.class)) {

                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
            if (SuperWebX5Config.WEBVIEW_TYPE == SuperWebX5Config.WEBVIEW_SUPERWEB_SAFE_TYPE && mChromeClientCallbackManager != null && mChromeClientCallbackManager.getSuperWebCompatInterface() != null) {

                LogUtils.i(TAG, "mChromeClientCallbackManager.getSuperWebCompatInterface():" + mChromeClientCallbackManager.getSuperWebCompatInterface());
                if (mChromeClientCallbackManager.getSuperWebCompatInterface().onJsPrompt(view, url, message, defaultValue, result))
                    return true;
            }
            showJsPrompt(message, result, defaultValue);
        } catch (Exception e) {
        }

        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {

        LogUtils.i(TAG, message);
        if (SuperWebX5Utils.isOverriedMethod(mWebChromeClient, "onJsConfirm", "public boolean " + ChromePath + ".onJsConfirm", WebView.class, String.class, String.class, JsResult.class)) {

            return super.onJsConfirm(view, url, message, result);
        }
        showJsConfirm(message, result);
        return true; //
    }


    private void toDismissDialog(Dialog dialog) {
        if (dialog != null)
            dialog.dismiss();

    }


    private void showJsConfirm(String message, final JsResult result) {

        Activity mActivity = this.mActivityWeakReference.get();
        if (mActivity == null||mActivity.isFinishing()) {
            result.cancel();
            return;
        }

        if (confirmDialog == null)
            confirmDialog = new AlertDialog.Builder(mActivity)//
                    .setMessage(message)//
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            toDismissDialog(confirmDialog);
                            toCancelJsresult(cJsResult);
                        }
                    })//
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            toDismissDialog(confirmDialog);
                            if (DefaultChromeClient.this.cJsResult != null)
                                DefaultChromeClient.this.cJsResult.confirm();

                        }
                    }).create();
        this.cJsResult = result;
        confirmDialog.show();

    }

    private void toCancelJsresult(JsResult result) {
        if (result != null)
            result.cancel();
    }

    private void showJsPrompt(String message, final JsPromptResult js, String defaultstr) {

        Activity mActivity = this.mActivityWeakReference.get();
        if (mActivity == null||mActivity.isFinishing()) {
            js.cancel();
            return;
        }
        if (promptDialog == null) {

            final EditText et = new EditText(mActivity);
            et.setText(defaultstr);
            promptDialog = new AlertDialog.Builder(mActivity)//
                    .setView(et)//
                    .setTitle(message)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            toDismissDialog(promptDialog);
                            toCancelJsresult(pJsResult);
                        }
                    })//
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            toDismissDialog(promptDialog);

                            if (DefaultChromeClient.this.pJsResult != null)
                                DefaultChromeClient.this.pJsResult.confirm(et.getText().toString());

                        }
                    }).create();
        }
        this.pJsResult = js;
        promptDialog.show();


    }

    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {


        if (SuperWebX5Utils.isOverriedMethod(mWebChromeClient, "onExceededDatabaseQuota", ChromePath + ".onExceededDatabaseQuota", String.class, String.class, long.class, long.class, long.class, WebStorage.QuotaUpdater.class)) {

            super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
            return;
        }
        quotaUpdater.updateQuota(totalQuota * 2);
    }

    @Override
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {


        if (SuperWebX5Utils.isOverriedMethod(mWebChromeClient, "onReachedMaxAppCacheSize", ChromePath + ".onReachedMaxAppCacheSize", long.class, long.class, WebStorage.QuotaUpdater.class)) {

            super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
            return;
        }
        quotaUpdater.updateQuota(requiredStorage * 2);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        LogUtils.i(TAG, "openFileChooser>=5.0");
        if (SuperWebX5Utils.isOverriedMethod(mWebChromeClient, "onShowFileChooser", ChromePath + ".onShowFileChooser", WebView.class, ValueCallback.class, WebChromeClient.FileChooserParams.class)) {

            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }
        openFileChooserAboveL(webView, filePathCallback, fileChooserParams);
        return true;
    }

    private void openFileChooserAboveL(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {


        Activity mActivity = this.mActivityWeakReference.get();
        if (mActivity == null||mActivity.isFinishing()){
            filePathCallback.onReceiveValue(new Uri[]{});
            return;
        }
        IFileUploadChooser mIFileUploadChooser = this.mIFileUploadChooser;
        this.mIFileUploadChooser = mIFileUploadChooser = new FileUpLoadChooserImpl.Builder()
                .setWebView(webView)
                .setActivity(mActivity)
                .setUriValueCallbacks(filePathCallback)
                .setFileChooserParams(fileChooserParams)
                .setFileUploadMsgConfig(mChromeClientMsgCfg.getFileUploadMsgConfig())
                .setPermissionInterceptor(this.mPermissionInterceptor)
                .build();
        mIFileUploadChooser.openFileChooser();

    }

    // Android  >= 4.1
    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        /*believe me , i never want to do this */
        LogUtils.i(TAG, "openFileChooser>=4.1");
        if (SuperWebX5Utils.isOverriedMethod(mWebChromeClient, "openFileChooser", ChromePath + ".openFileChooser", ValueCallback.class, String.class, String.class)) {
            super.openFileChooser(uploadFile, acceptType, capture);
            return;
        }
        createAndOpenCommonFileLoader(uploadFile);
    }

    //  Android < 3.0
    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        if (SuperWebX5Utils.isOverriedMethod(mWebChromeClient, "openFileChooser", ChromePath + ".openFileChooser", ValueCallback.class)) {
            super.openFileChooser(valueCallback);
            return;
        }
        Log.i(TAG, "openFileChooser<3.0");
        createAndOpenCommonFileLoader(valueCallback);
    }

    //  Android  >= 3.0
    public void openFileChooser(ValueCallback valueCallback, String acceptType) {
        Log.i(TAG, "openFileChooser>3.0");

        if (SuperWebX5Utils.isOverriedMethod(mWebChromeClient, "openFileChooser", ChromePath + ".openFileChooser", ValueCallback.class, String.class)) {
            super.openFileChooser(valueCallback, acceptType);
            return;
        }
        createAndOpenCommonFileLoader(valueCallback);
    }


    private void createAndOpenCommonFileLoader(ValueCallback valueCallback) {
        Activity mActivity = this.mActivityWeakReference.get();
        if (mActivity == null||mActivity.isFinishing()){
            valueCallback.onReceiveValue(new Object());
            return;
        }
        this.mIFileUploadChooser = new FileUpLoadChooserImpl.Builder()
                .setWebView(this.mWebView)
                .setActivity(mActivity)
                .setUriValueCallback(valueCallback)
                .setFileUploadMsgConfig(mChromeClientMsgCfg.getFileUploadMsgConfig())
                .setPermissionInterceptor(this.mPermissionInterceptor)
                .build();
        this.mIFileUploadChooser.openFileChooser();

    }

    @Override
    public IFileUploadChooser pop() {
        Log.i(TAG, "offer:" + mIFileUploadChooser);
        IFileUploadChooser mIFileUploadChooser = this.mIFileUploadChooser;
        this.mIFileUploadChooser = null;
        return mIFileUploadChooser;
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        super.onConsoleMessage(consoleMessage);
        LogUtils.i(TAG, "consoleMessage:" + consoleMessage.message() + "  lineNumber:" + consoleMessage.lineNumber());
        return true;
    }




    @Override
    public void onHideCustomView() {
        if (SuperWebX5Utils.isOverriedMethod(mWebChromeClient, "onHideCustomView", ChromePath + ".onHideCustomView")) {
            LogUtils.i(TAG, "onHide:" + true);
            super.onHideCustomView();
            return;
        }

        LogUtils.i(TAG, "Video:" + mIVideo);
        if (mIVideo != null)
            mIVideo.onHideCustomView();

    }


}
