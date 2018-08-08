package com.fanneng.android.web;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.fanneng.android.web.file.FileUpLoadChooserImpl;
import com.fanneng.android.web.file.FileUploadPop;
import com.fanneng.android.web.file.IFileUploadChooser;

import java.lang.ref.WeakReference;

/**
 * JS
 */
public class SuperWebJsInterfaceX5Compat implements SuperWebX5Compat, FileUploadPop<IFileUploadChooser> {

    private SuperWebX5 mSuperWebX5;
    private WeakReference<SuperWebX5> mReference = null;
    private WeakReference<Activity> mActivityWeakReference = null;

    public SuperWebJsInterfaceX5Compat(SuperWebX5 superWebX5, Activity activity) {
        this.mReference = new WeakReference<SuperWebX5>(superWebX5);
        mActivityWeakReference = new WeakReference<Activity>(activity);
    }

    private IFileUploadChooser mIFileUploadChooser;

    @JavascriptInterface
    public void uploadFile() {
        if (mActivityWeakReference.get() != null && mReference.get() != null) {
            mIFileUploadChooser = new FileUpLoadChooserImpl.Builder()
                    .setActivity(mActivityWeakReference.get())
                    .setJsChannelCallback(new FileUpLoadChooserImpl.JsChannelCallback() {
                        @Override
                        public void call(String value) {
                            if (mReference.get() != null)
                                mReference.get().getJsEntraceAccess().quickCallJs("uploadFileResult", value);
                        }
                    }).setFileUploadMsgConfig(mReference.get().getDefaultMsgConfig().getChromeClientMsgCfg().getFileUploadMsgConfig())
                    .setPermissionInterceptor(mReference.get().getPermissionInterceptor())
                    .setWebView(mReference.get().getWebCreator().get())
                    .build();
            mIFileUploadChooser.openFileChooser();
            return;
        }

    }

    @Override
    public IFileUploadChooser pop() {
        IFileUploadChooser mIFileUploadChooser = this.mIFileUploadChooser;
        this.mIFileUploadChooser = null;
        return mIFileUploadChooser;
    }
}
