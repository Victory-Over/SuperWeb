package com.fanneng.android.web.client;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fanneng.android.web.IWebLayout;
import com.fanneng.android.web.SuperWebX5Config;
import com.fanneng.android.web.progress.WebCreator;
import com.fanneng.android.web.progress.WebProgress;
import com.fanneng.android.web.progress.BaseIndicatorView;
import com.fanneng.android.web.progress.BaseProgressSpec;
import com.fanneng.android.web.utils.LogUtils;
import com.fanneng.android.web.utils.SuperWebX5Utils;
import com.tencent.smtt.sdk.WebView;


public class DefaultWebCreator implements WebCreator {

    private Activity mActivity;
    private ViewGroup mViewGroup;
    private boolean isNeedDefaultProgress;
    private int index;
    private BaseIndicatorView progressView;
    private ViewGroup.LayoutParams mLayoutParams = null;
    private int color = -1;
    private int height_dp;
    private IWebLayout mIWebLayout;
    private boolean isCreated=false;


    public DefaultWebCreator(@NonNull Activity activity, @Nullable ViewGroup viewGroup, ViewGroup.LayoutParams lp, int index, int color, int height_dp, WebView webView, IWebLayout webLayout) {
        this.mActivity = activity;
        this.mViewGroup = viewGroup;
        this.isNeedDefaultProgress = true;
        this.index = index;
        this.color = color;
        this.mLayoutParams = lp;
        this.height_dp = height_dp;
        this.mWebView = webView;
        this.mIWebLayout=webLayout;
    }

    public DefaultWebCreator(@NonNull Activity activity, @Nullable ViewGroup viewGroup, ViewGroup.LayoutParams lp, int index, @Nullable WebView webView, IWebLayout webLayout) {
        this.mActivity = activity;
        this.mViewGroup = viewGroup;
        this.isNeedDefaultProgress = false;
        this.index = index;
        this.mLayoutParams = lp;
        this.mWebView = webView;
        this.mIWebLayout=webLayout;
    }

    public DefaultWebCreator(@NonNull Activity activity, @Nullable ViewGroup viewGroup, ViewGroup.LayoutParams lp, int index, BaseIndicatorView progressView, WebView webView, IWebLayout webLayout) {
        this.mActivity = activity;
        this.mViewGroup = viewGroup;
        this.isNeedDefaultProgress = false;
        this.index = index;
        this.mLayoutParams = lp;
        this.progressView = progressView;
        this.mWebView = webView;
        this.mIWebLayout=webLayout;
    }

    private WebView mWebView = null;
    private FrameLayout mFrameLayout = null;
    private View targetProgress;

    public WebView getWebView() {
        return mWebView;
    }

    public void setWebView(WebView webView) {
        mWebView = webView;
    }

    public FrameLayout getFrameLayout() {
        return mFrameLayout;
    }

    public void setFrameLayout(FrameLayout frameLayout) {
        mFrameLayout = frameLayout;
    }

    public View getTargetProgress() {
        return targetProgress;
    }

    public void setTargetProgress(View targetProgress) {
        this.targetProgress = targetProgress;
    }

    @Override
    public DefaultWebCreator create() {


        if(isCreated){
            return this;
        }
        isCreated=true;
        ViewGroup mViewGroup = this.mViewGroup;
        if (mViewGroup == null) {
            mViewGroup = createGroupWithWeb();
            mActivity.setContentView(mViewGroup);
        } else {
            if (index == -1)
                mViewGroup.addView(createGroupWithWeb(), mLayoutParams);
            else
                mViewGroup.addView(createGroupWithWeb(), index, mLayoutParams);
        }
        return this;
    }

    @Override
    public WebView get() {
        return mWebView;
    }

    @Override
    public FrameLayout getGroup() {
        return mFrameLayout;
    }

    private BaseProgressSpec mBaseProgressSpec;

    private ViewGroup createGroupWithWeb() {
        Activity mActivity = this.mActivity;

        FrameLayout mFrameLayout = new FrameLayout(mActivity);
        mFrameLayout.setBackgroundColor(Color.WHITE);
        com.tencent.smtt.sdk.WebView mWebView = null;
        View target=mIWebLayout==null?(this.mWebView= (WebView) web()):webLayout();
        FrameLayout.LayoutParams mLayoutParams = new FrameLayout.LayoutParams(-1, -1);
        mFrameLayout.addView(target, mLayoutParams);
        if (isNeedDefaultProgress) {
            FrameLayout.LayoutParams lp = null;
            WebProgress mWebProgress = new WebProgress(mActivity);
            if (height_dp > 0)
                lp = new FrameLayout.LayoutParams(-2, SuperWebX5Utils.dp2px(mActivity, height_dp));
            else
                lp = mWebProgress.offerLayoutParams();
            if (color != -1)
                mWebProgress.setColor(color);
            lp.gravity = Gravity.TOP;
            mFrameLayout.addView((View) (this.mBaseProgressSpec = mWebProgress), lp);
            mWebProgress.setVisibility(View.GONE);
        } else if (!isNeedDefaultProgress && progressView != null) {
//            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2);
            mFrameLayout.addView((View) (this.mBaseProgressSpec = (BaseProgressSpec) progressView), progressView.offerLayoutParams());
        }
        return this.mFrameLayout=mFrameLayout;

    }

    private WebView web() {
        WebView mWebView = null;
        if (this.mWebView != null) {
            mWebView = this.mWebView;
            SuperWebX5Config.WEBVIEW_TYPE = SuperWebX5Config.WEBVIEW_CUSTOM_TYPE;
        } else {
            mWebView = new WebView(mActivity);
            SuperWebX5Config.WEBVIEW_TYPE = SuperWebX5Config.WEBVIEW_DEFAULT_TYPE;
        }

        return mWebView;
    }

    private View webLayout(){
        WebView mWebView = null;
        if((mWebView=mIWebLayout.getWeb())==null){
            mWebView=web();
            mIWebLayout.getLayout().addView(mWebView,-1,-1);
            LogUtils.i("Info","add webview");

        }else{
            SuperWebX5Config.WEBVIEW_TYPE= SuperWebX5Config.WEBVIEW_CUSTOM_TYPE;
        }
        this.mWebView=mWebView;
        return mIWebLayout.getLayout();

    }

    @Override
    public BaseProgressSpec offer() {
        return mBaseProgressSpec;
    }
}
