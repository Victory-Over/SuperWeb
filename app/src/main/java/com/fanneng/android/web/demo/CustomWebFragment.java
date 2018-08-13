package com.fanneng.android.web.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanneng.android.web.IWebLayout;
import com.fanneng.android.web.SuperWebX5;
import com.fanneng.android.web.client.DefaultWebClient;


public class CustomWebFragment extends SuperWebX5Fragment {

    public static SuperWebX5Fragment getInstance(Bundle bundle) {

        SuperWebX5Fragment mBounceWebFragment = new SuperWebX5Fragment();
        if (mBounceWebFragment != null) {
            mBounceWebFragment.setArguments(bundle);
        }

        return mBounceWebFragment;
    }


    @Override
    public String getUrl() {
        return super.getUrl();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mSuperWebX5 = SuperWebX5.with(this)
                .setSuperWebParent((LinearLayout) view, new LinearLayout.LayoutParams(-1, -1))
                .setIndicatorColorWithHeight(-1, 2)
                .setWebSettings(getSettings())
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .setReceivedTitleCallback(mCallback)
                .setPermissionInterceptor(mPermissionInterceptor)
                .setNotifyIcon(R.mipmap.download)
                .useMiddleWareWebChrome(getMiddleWareWebChrome())
                .useMiddleWareWebClient(getMiddleWareWebClient())
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
                .interceptUnkownScheme()
                .openParallelDownload()
                .setSecurityType(SuperWebX5.SecurityType.strict)
                .addDownLoadResultListener(mDownLoadResultListener)
                .createSuperWeb()
                .ready()
                .go(getUrl());


        // 得到 AgentWeb 最底层的控件
        addBGChild(mSuperWebX5.getWebCreator().getWebParentLayout());
        initView(view);


    }

    @Override
    protected IWebLayout getWebLayout() {
        return new WebLayout(getActivity());
    }


    protected void addBGChild(FrameLayout frameLayout) {

        TextView mTextView = new TextView(frameLayout.getContext());
        mTextView.setText("网页由 "+getUrl()+" 提供\nSuperWeb浏览器提供技术支持");
        mTextView.setTextSize(16);
        mTextView.setTextColor(Color.parseColor("#727779"));
        frameLayout.setBackgroundColor(Color.parseColor("#272b2d"));
        FrameLayout.LayoutParams mFlp = new FrameLayout.LayoutParams(-2, -2);
        mFlp.gravity = Gravity.CENTER_HORIZONTAL;
        final float scale = frameLayout.getContext().getResources().getDisplayMetrics().density;
        mFlp.topMargin = (int) (15 * scale + 0.5f);
        frameLayout.addView(mTextView, 0, mFlp);
    }


}
