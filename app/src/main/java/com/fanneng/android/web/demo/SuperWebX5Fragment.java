package com.fanneng.android.web.demo;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanneng.android.web.IWebLayout;
import com.fanneng.android.web.SuperWebX5;
import com.fanneng.android.web.client.ChromeClientCallbackManager;
import com.fanneng.android.web.client.DefaultWebClient;
import com.fanneng.android.web.client.MiddleWareWebChromeBase;
import com.fanneng.android.web.client.MiddleWareWebClientBase;
import com.fanneng.android.web.client.WebDefaultSettingsManager;
import com.fanneng.android.web.client.WebSettings;
import com.fanneng.android.web.file.DownLoadResultListener;
import com.fanneng.android.web.utils.PermissionInterceptor;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import java.util.HashMap;


/**
 * describe：
 *
 * @author ：鲁宇峰 on 2018/8/8 13：44
 *         email：luyufengc@enn.cn
 */
public class SuperWebX5Fragment extends Fragment implements FragmentKeyDown {


    private ImageView mBackImageView;
    private View mLineView;
    private ImageView mFinishImageView;
    private TextView mTitleTextView;
    protected SuperWebX5 mSuperWebX5;
    public static final String URL_KEY = "url_key";
    public static final String TAG = SuperWebX5Fragment.class.getSimpleName();
    private MiddleWareWebChromeBase mMiddleWareWebChrome;
    private MiddleWareWebClientBase mMiddleWareWebClient;


    public static SuperWebX5Fragment getInstance(Bundle bundle) {

        SuperWebX5Fragment mSuperWebX5Fragment = new SuperWebX5Fragment();
        if (bundle != null) {
            mSuperWebX5Fragment.setArguments(bundle);
        }

        return mSuperWebX5Fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_superweb, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSuperWebX5 = SuperWebX5.with(this)
                .setSuperWebParent((LinearLayout) view, new LinearLayout.LayoutParams(-1, -1))
                .setIndicatorColorWithHeight(-1, 2)
                .setWebSettings(getSettings())
                .setWebLayout(getWebLayout())
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

        initView(view);

//        mSuperWebX5.getWebCreator().get().setOverScrollMode(android.webkit.WebView.OVER_SCROLL_NEVER);

    }

    /**
     * 权限拦截器
     * 在触发某些敏感的 Action 时候会回调该方法， 比如定位触发 。
     * 例如 https//:www.baidu.com 该 Url 需要定位权限， 返回false ，如果版本大于等于23 ， 会动态申请权限 ，true 该Url对应页面请求定位失败。
     * 该方法是每次都会优先触发的 ， 开发者可以做一些敏感权限拦截
     */
    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {

        @Override
        public boolean intercept(String url, String[] permissions, String action) {
            Log.i(TAG, "url:" + url + "  permission:" + permissions + " action:" + action);
            return false;
        }
    };

    /**
     * 文件下载结果监听
     */
    protected DownLoadResultListener mDownLoadResultListener = new DownLoadResultListener() {
        @Override
        public void success(String path) {
            Toast.makeText(getContext(),"下载成功,存储地址:"+path,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void error(String path, String resUrl, String cause, Throwable e) {
            Toast.makeText(getContext(),"下载失败",Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 获取WebView设置
     */
    public WebSettings getSettings() {
        return WebDefaultSettingsManager.getInstance();
    }

    /**
     * 获取url
     */
    public String getUrl() {
        String target = "";
        if (TextUtils.isEmpty(target = this.getArguments().getString(URL_KEY))) {
            target = "http://www.fanneng.com";
        }
        return target;
    }

    /**
     * 获取网页title
     */
    protected ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (mTitleTextView != null && !TextUtils.isEmpty(title)) {
                if (title.length() > 10) {
                    title = title.substring(0, 10) + "...";
                }
            }
            mTitleTextView.setText(title);

        }
    };


    /**
     * 获取进度条
     */
    protected WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    };

    /**
     * 采用X5客户端拦截URL
     */
    protected com.tencent.smtt.sdk.WebViewClient mWebViewClient = new com.tencent.smtt.sdk.WebViewClient() {
        private HashMap<String, Long> timer = new HashMap<>();

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }


        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            //intent:// scheme的处理 如果返回false ， 则交给 DefaultWebClient 处理 ， 默认会打开该Activity  ， 如果Activity不存在则跳到应用市场上去.  true 表示拦截
            //例如优酷视频播放 ，intent://play?vid=XODEzMjU1MTI4&refer=&tuid=&ua=Mozilla%2F5.0%20(Linux%3B%20Android%207.0%3B%20SM-G9300%20Build%2FNRD90M%3B%20wv)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Version%2F4.0%20Chrome%2F58.0.3029.83%20Mobile%20Safari%2F537.36&source=exclusive-pageload&cookieid=14971464739049EJXvh|Z6i1re#Intent;scheme=youku;package=com.youku.phone;end;
            //优酷想唤起自己应用播放该视频 ， 下面拦截地址返回 true  则会在应用内 H5 播放 ，禁止优酷唤起播放该视频， 如果返回 false ， DefaultWebClient  会根据intent 协议处理 该地址 ， 首先匹配该应用存不存在 ，如果存在 ， 唤起该应用播放 ， 如果不存在 ， 则跳到应用市场下载该应用 .
            if (url.startsWith("intent://")) {
                return true;
            } else if (url.startsWith("youku")) {
                return true;
            }

            return false;
        }

        int index = 1;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            Log.i(TAG, "url:" + url + " onPageStarted  url:" + getUrl());
            timer.put(url, new Long(System.currentTimeMillis()));
            if (url.equals(getUrl())) {
                pageNavigator(View.GONE);
            } else {
                pageNavigator(View.VISIBLE);
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            Log.i(TAG, "onPageFinished  url:" + url + "  time:" + timer.get(url) + "   index:" + (index++));
            if (timer.get(url) != null) {
                long overTime = System.currentTimeMillis();
                Long startTime = timer.get(url);
                Log.i(TAG, "  page url:" + url + "  used time:" + (overTime - startTime));
            }

        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("Info", "onActivityResult -- >callback:" + requestCode + "   0x254:" + 0x254);
        mSuperWebX5.uploadFileResult(requestCode, resultCode, data);
    }

    protected void initView(View view) {
        mBackImageView = (ImageView) view.findViewById(R.id.iv_back);
        mLineView = view.findViewById(R.id.view_line);

        mFinishImageView = (ImageView) view.findViewById(R.id.iv_finish);
        mTitleTextView = (TextView) view.findViewById(R.id.toolbar_title);

        mBackImageView.setOnClickListener(mOnClickListener);
        mFinishImageView.setOnClickListener(mOnClickListener);

        pageNavigator(View.GONE);
    }


    private void pageNavigator(int tag) {

        mBackImageView.setVisibility(tag);
        mLineView.setVisibility(tag);
    }

    protected IWebLayout getWebLayout() {
        return new WebLayout(getActivity());
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.iv_back:
                    if (!mSuperWebX5.back()) {
                        SuperWebX5Fragment.this.getActivity().finish();
                    }
                    break;
                case R.id.iv_finish:
                    SuperWebX5Fragment.this.getActivity().finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        mSuperWebX5.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mSuperWebX5.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    public boolean onFragmentKeyDown(int keyCode, KeyEvent event) {
        return mSuperWebX5.handleKeyEvent(keyCode, event);
    }

    @Override
    public void onDestroyView() {
        mSuperWebX5.getWebLifeCycle().onDestroy();
        super.onDestroyView();
    }

    public MiddleWareWebChromeBase getMiddleWareWebChrome() {
        return mMiddleWareWebChrome;
    }

    public MiddleWareWebClientBase getMiddleWareWebClient() {
        return mMiddleWareWebClient;
    }
}
