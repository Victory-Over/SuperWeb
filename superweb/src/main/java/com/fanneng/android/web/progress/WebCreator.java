package com.fanneng.android.web.progress;

import android.view.ViewGroup;

public interface WebCreator extends ProgressManager {
    WebCreator create();

    com.tencent.smtt.sdk.WebView get();

    ViewGroup getGroup();
}
