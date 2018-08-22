package com.fanneng.android.web.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * describe：视频播放类
 *
 * @author ：鲁宇峰 on 2018/8/22 13：44
 *         email：luyufengc@enn.cn
 */
public class VideoPlayActivity extends AppCompatActivity {
    private String videoUrl;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        getIntentData();
        initView();
        startPlay(videoUrl);
    }

    /**
     * 跳转至此页面
     *
     * @param context
     * @param videoUrl 视频地址
     */
    public static void show(Context context, String videoUrl) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra("videoUrl", videoUrl);
        context.startActivity(intent);
    }

    /**
     * 获取上个页面传过来的数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        videoUrl = intent.getStringExtra("videoUrl");
    }

    private void initView() {
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(client);
    }

    /**
     * 使用X5WebView播放视频
     *
     * @param videoUrl 视频地址
     */
    private void startPlay(String videoUrl) {
        webView.loadUrl(videoUrl);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        webView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        webView.setWebChromeClient(new WebChromeClient());
    }

    private WebViewClient client = new WebViewClient() {
        /**
         * 防止加载网页时调起系统浏览器
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };
}
