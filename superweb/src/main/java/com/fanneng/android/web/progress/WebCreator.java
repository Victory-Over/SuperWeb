package com.fanneng.android.web.progress;

import android.widget.FrameLayout;

public interface WebCreator extends ProgressManager {
    WebCreator create();

    com.tencent.smtt.sdk.WebView get();

    FrameLayout getWebParentLayout();
}
