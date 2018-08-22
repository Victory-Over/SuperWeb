package com.fanneng.android.web;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.fanneng.android.web.client.ChromeClientCallbackManager;
import com.fanneng.android.web.client.DefaultChromeClient;
import com.fanneng.android.web.client.DefaultWebClient;
import com.fanneng.android.web.client.DefaultWebCreator;
import com.fanneng.android.web.client.DefaultWebLifeCycleImpl;
import com.fanneng.android.web.client.MiddleWareWebChromeBase;
import com.fanneng.android.web.client.MiddleWareWebClientBase;
import com.fanneng.android.web.client.WebDefaultSettingsManager;
import com.fanneng.android.web.client.WebLifeCycle;
import com.fanneng.android.web.client.WebListenerManager;
import com.fanneng.android.web.client.WebSecurityCheckLogic;
import com.fanneng.android.web.client.WebSecurityController;
import com.fanneng.android.web.client.WebSecurityControllerImpl;
import com.fanneng.android.web.client.WebSecurityLogicImpl;
import com.fanneng.android.web.client.WebSettings;
import com.fanneng.android.web.client.WebViewClientCallbackManager;
import com.fanneng.android.web.file.DefaultDownLoaderImpl;
import com.fanneng.android.web.file.DownLoadResultListener;
import com.fanneng.android.web.file.IFileUploadChooser;
import com.fanneng.android.web.js.JsEntraceAccess;
import com.fanneng.android.web.js.JsEntraceAccessImpl;
import com.fanneng.android.web.js.JsInterfaceHolder;
import com.fanneng.android.web.js.JsInterfaceHolderImpl;
import com.fanneng.android.web.progress.BaseIndicatorView;
import com.fanneng.android.web.progress.IndicatorController;
import com.fanneng.android.web.progress.IndicatorHandler;
import com.fanneng.android.web.progress.WebCreator;
import com.fanneng.android.web.utils.DefaultMsgConfig;
import com.fanneng.android.web.utils.LogUtils;
import com.fanneng.android.web.utils.PermissionInterceptor;
import com.fanneng.android.web.utils.SuperWebX5Utils;
import com.fanneng.android.web.video.EventHandlerImpl;
import com.fanneng.android.web.video.EventInterceptor;
import com.fanneng.android.web.video.IEventHandler;
import com.fanneng.android.web.video.IVideo;
import com.fanneng.android.web.video.VideoImpl;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 核心类 初始化操作
 */
public class SuperWebX5 {

    private static final String TAG = SuperWebX5.class.getSimpleName();
    private Activity mActivity;
    private ViewGroup mViewGroup;
    private WebCreator mWebCreator;
    private WebSettings mWebSettings;
    private SuperWebX5 mSuperWebX5 = null;
    private IndicatorController mIndicatorController;
    private WebChromeClient mWebChromeClient;
    private WebViewClient mWebViewClient;
    private boolean enableProgress;
    private Fragment mFragment;
    private IEventHandler mIEventHandler;
    private ArrayMap<String, Object> mJavaObjects = new ArrayMap<>();
    private int TAG_TARGET = 0;
    private WebListenerManager mWebListenerManager;
    private DownloadListener mDownloadListener = null;
    private ChromeClientCallbackManager mChromeClientCallbackManager;
    private WebSecurityController<WebSecurityCheckLogic> mWebSecurityController = null;
    private WebSecurityCheckLogic mWebSecurityCheckLogic = null;
    private WebChromeClient mTargetChromeClient;
    private SecurityType mSecurityType = SecurityType.default_check;
    private static final int ACTIVITY_TAG = 0;
    private static final int FRAGMENT_TAG = 1;
    private SuperWebJsInterfaceX5Compat mSuperWebJsInterfaceCompat = null;
    private JsEntraceAccess mJsEntraceAccess = null;
    private ILoader mILoader = null;
    private WebLifeCycle mWebLifeCycle;
    private IVideo mIVideo = null;
    private boolean webClientHelper = false;
    private DefaultMsgConfig mDefaultMsgConfig;
    private PermissionInterceptor mPermissionInterceptor;

    private boolean isInterceptUnkownScheme = false;
    private int openOtherAppWays = -1;
    private MiddleWareWebClientBase mMiddleWrareWebClientBaseHeader;
    private MiddleWareWebChromeBase mMiddleWareWebChromeBaseHeader;


    private SuperWebX5(SuperBuilder superBuilder) {
        this.mActivity = superBuilder.mActivity;
        this.mViewGroup = superBuilder.mViewGroup;
        this.enableProgress = superBuilder.enableProgress;
        mWebCreator = superBuilder.mWebCreator == null ? configWebCreator(superBuilder.v, superBuilder.index, superBuilder.mLayoutParams, superBuilder.mIndicatorColor, superBuilder.mIndicatorColorWithHeight, superBuilder.mWebView, superBuilder.mWebLayout) : superBuilder.mWebCreator;
        mIndicatorController = superBuilder.mIndicatorController;
        this.mWebChromeClient = superBuilder.mWebChromeClient;
        this.mWebViewClient = superBuilder.mWebViewClient;
        mSuperWebX5 = this;
        this.mWebSettings = superBuilder.mWebSettings;
        this.mIEventHandler = superBuilder.mIEventHandler;
        TAG_TARGET = ACTIVITY_TAG;
        if (superBuilder.mJavaObject != null && superBuilder.mJavaObject.isEmpty())
            this.mJavaObjects.putAll((Map<? extends String, ?>) superBuilder.mJavaObject);
        this.mChromeClientCallbackManager = superBuilder.mChromeClientCallbackManager;
        this.mWebViewClientCallbackManager = superBuilder.mWebViewClientCallbackManager;

        this.mSecurityType = superBuilder.mSecurityType;
        this.mILoader = new LoaderImpl(mWebCreator.create().get(), superBuilder.headers);
        this.mWebLifeCycle = new DefaultWebLifeCycleImpl(mWebCreator.get());
        mWebSecurityController = new WebSecurityControllerImpl(mWebCreator.get(), this.mSuperWebX5.mJavaObjects, mSecurityType);
        this.webClientHelper = superBuilder.webclientHelper;

        this.isInterceptUnkownScheme = superBuilder.isInterceptUnkownScheme;
        if (superBuilder.openOtherPage != null) {
            this.openOtherAppWays = superBuilder.openOtherPage.code;
        }
        this.mMiddleWrareWebClientBaseHeader = superBuilder.header;
        this.mMiddleWareWebChromeBaseHeader = superBuilder.mChromeMiddleWareHeader;


        init();
        setDownloadListener(superBuilder.mDownLoadResultListeners, superBuilder.isParallelDownload, superBuilder.icon);
    }


    private SuperWebX5(SuperBuilderFragment superBuilderFragment) {
        TAG_TARGET = FRAGMENT_TAG;
        this.mActivity = superBuilderFragment.mActivity;
        this.mFragment = superBuilderFragment.mFragment;
        this.mViewGroup = superBuilderFragment.mViewGroup;
        this.mIEventHandler = superBuilderFragment.mIEventHandler;
        this.enableProgress = superBuilderFragment.enableProgress;
        mWebCreator = superBuilderFragment.mWebCreator == null ? configWebCreator(superBuilderFragment.v, superBuilderFragment.index, superBuilderFragment.mLayoutParams, superBuilderFragment.mIndicatorColor, superBuilderFragment.height_dp, superBuilderFragment.mWebView, superBuilderFragment.webLayout) : superBuilderFragment.mWebCreator;
        mIndicatorController = superBuilderFragment.mIndicatorController;
        this.mWebChromeClient = superBuilderFragment.mWebChromeClient;
        this.mWebViewClient = superBuilderFragment.mWebViewClient;
        mSuperWebX5 = this;
        this.mWebSettings = superBuilderFragment.mWebSettings;
        if (superBuilderFragment.mJavaObject != null && superBuilderFragment.mJavaObject.isEmpty())
            this.mJavaObjects.putAll((Map<? extends String, ?>) superBuilderFragment.mJavaObject);
        this.mChromeClientCallbackManager = superBuilderFragment.mChromeClientCallbackManager;
        this.mWebViewClientCallbackManager = superBuilderFragment.mWebViewClientCallbackManager;
        this.mSecurityType = superBuilderFragment.mSecurityType;
        this.mILoader = new LoaderImpl(mWebCreator.create().get(), superBuilderFragment.additionalHttpHeaders);
        this.mWebLifeCycle = new DefaultWebLifeCycleImpl(mWebCreator.get());
        mWebSecurityController = new WebSecurityControllerImpl(mWebCreator.get(), this.mSuperWebX5.mJavaObjects, this.mSecurityType);
        this.webClientHelper = superBuilderFragment.webClientHelper;


        this.isInterceptUnkownScheme = superBuilderFragment.isInterceptUnkownScheme;
        if (superBuilderFragment.openOtherPage != null) {
            this.openOtherAppWays = superBuilderFragment.openOtherPage.code;
        }
        this.mMiddleWrareWebClientBaseHeader = superBuilderFragment.header;
        this.mMiddleWareWebChromeBaseHeader = superBuilderFragment.mChromeMiddleWareHeader;

        init();
        setDownloadListener(superBuilderFragment.mDownLoadResultListeners, superBuilderFragment.isParallelDownload, superBuilderFragment.icon);

    }

    private void init() {
        if (this.mDownloadListener == null)
            mDefaultMsgConfig = new DefaultMsgConfig();
        doCompat();
        doSafeCheck();
    }

    public DefaultMsgConfig getDefaultMsgConfig() {
        return this.mDefaultMsgConfig;
    }

    private void doCompat() {


        mJavaObjects.put("agentWebX5", mSuperWebJsInterfaceCompat = new SuperWebJsInterfaceX5Compat(this, mActivity));

        LogUtils.i("Info", "SuperWebX5Config.isUseSuperWebView:" + SuperWebX5Config.WEBVIEW_TYPE + "  mChromeClientCallbackManager:" + mChromeClientCallbackManager);
        if (SuperWebX5Config.WEBVIEW_TYPE == SuperWebX5Config.WEBVIEW_SUPERWEB_SAFE_TYPE) {
            this.mChromeClientCallbackManager.setSuperWebCompatInterface((ChromeClientCallbackManager.SuperWebCompatInterface) mWebCreator.get());
            this.mWebViewClientCallbackManager.setPageLifeCycleCallback((WebViewClientCallbackManager.PageLifeCycleCallback) mWebCreator.get());
        }

    }

    public WebLifeCycle getWebLifeCycle() {
        return this.mWebLifeCycle;
    }

    private void doSafeCheck() {

        WebSecurityCheckLogic mWebSecurityCheckLogic = this.mWebSecurityCheckLogic;
        if (mWebSecurityCheckLogic == null) {
            this.mWebSecurityCheckLogic = mWebSecurityCheckLogic = WebSecurityLogicImpl.getInstance();
        }
        mWebSecurityController.check(mWebSecurityCheckLogic);

    }

    private WebCreator configWebCreator(BaseIndicatorView progressView, int index, ViewGroup.LayoutParams lp, int mIndicatorColor, int height_dp, WebView webView, IWebLayout webLayout) {

        if (progressView != null && enableProgress) {
            return new DefaultWebCreator(mActivity, mViewGroup, lp, index, progressView, webView, webLayout);
        } else {
            return enableProgress ?
                    new DefaultWebCreator(mActivity, mViewGroup, lp, index, mIndicatorColor, height_dp, webView, webLayout)
                    : new DefaultWebCreator(mActivity, mViewGroup, lp, index, webView, webLayout);
        }
    }

    private void loadData(String data, String mimeType, String encoding) {
        mWebCreator.get().loadData(data, mimeType, encoding);
    }

    private void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String history) {
        mWebCreator.get().loadDataWithBaseURL(baseUrl, data, mimeType, encoding, history);
    }


    public JsEntraceAccess getJsEntraceAccess() {

        JsEntraceAccess mJsEntraceAccess = this.mJsEntraceAccess;
        if (mJsEntraceAccess == null) {
            this.mJsEntraceAccess = mJsEntraceAccess = JsEntraceAccessImpl.getInstance(mWebCreator.get());
        }
        return mJsEntraceAccess;
    }


    public SuperWebX5 clearWebCache() {

        SuperWebX5Utils.clearWebViewAllCache(mActivity);
        return this;
    }


    public static SuperBuilder with(@NonNull Activity activity) {
        if (activity == null)
            throw new NullPointerException("activity can not null");
        return new SuperBuilder(activity);
    }

    public static SuperBuilderFragment with(@NonNull Fragment fragment) {


        Activity mActivity = null;
        if ((mActivity = fragment.getActivity()) == null)
            throw new NullPointerException("activity can not null");
        return new SuperBuilderFragment(mActivity, fragment);
    }

    private EventInterceptor mEventInterceptor;

    private EventInterceptor getInterceptor() {

        if (this.mEventInterceptor != null)
            return this.mEventInterceptor;

        if (mIVideo instanceof VideoImpl) {
            return this.mEventInterceptor = (EventInterceptor) this.mIVideo;
        }

        return null;

    }

    public boolean handleKeyEvent(int keyCode, KeyEvent keyEvent) {

        if (mIEventHandler == null) {
            mIEventHandler = EventHandlerImpl.getInstantce(mWebCreator.get(), getInterceptor());
        }
        return mIEventHandler.onKeyDown(keyCode, keyEvent);
    }

    public boolean back() {

        if (mIEventHandler == null) {
            mIEventHandler = EventHandlerImpl.getInstantce(mWebCreator.get(), getInterceptor());
        }
        return mIEventHandler.back();
    }


    public WebCreator getWebCreator() {
        return this.mWebCreator;
    }

    public IEventHandler getIEventHandler() {
        return this.mIEventHandler == null ? (this.mIEventHandler = EventHandlerImpl.getInstantce(mWebCreator.get(), getInterceptor())) : this.mIEventHandler;
    }

    private JsInterfaceHolder mJsInterfaceHolder = null;

    public WebSettings getWebSettings() {
        return this.mWebSettings;
    }

    public IndicatorController getIndicatorController() {
        return this.mIndicatorController;
    }


    private SuperWebX5 ready() {

        SuperWebX5Config.initCookiesManager(mActivity.getApplicationContext());
        WebSettings mWebSettings = this.mWebSettings;
        if (mWebSettings == null) {
            this.mWebSettings = mWebSettings = WebDefaultSettingsManager.getInstance();
        }
        if (mWebListenerManager == null && mWebSettings instanceof WebDefaultSettingsManager) {
            mWebListenerManager = (WebListenerManager) mWebSettings;
        }
        mWebSettings.toSetting(mWebCreator.get());
        if (mJsInterfaceHolder == null) {
            mJsInterfaceHolder = JsInterfaceHolderImpl.getJsInterfaceHolder(mWebCreator.get(), this.mSecurityType);
        }
        if (mJavaObjects != null && !mJavaObjects.isEmpty()) {
            mJsInterfaceHolder.addJavaObjects(mJavaObjects);
        }
        mWebListenerManager.setDownLoader(mWebCreator.get(), getLoadListener());
        mWebListenerManager.setWebChromeClient(mWebCreator.get(), getChromeClient());
        mWebListenerManager.setWebViewClient(mWebCreator.get(), getClient());


        return this;
    }


    private void setDownloadListener(List<DownLoadResultListener> downLoadResultListeners, boolean isParallelDl, int icon) {
        DownloadListener mDownloadListener = this.mDownloadListener;
        if (mDownloadListener == null) {
            this.mDownloadListener = mDownloadListener = new DefaultDownLoaderImpl.Builder().setActivity(mActivity)
                    .setEnableIndicator(true)//
                    .setForce(false)//
                    .setDownLoadResultListeners(downLoadResultListeners)//
                    .setDownLoadMsgConfig(mDefaultMsgConfig.getDownLoadMsgConfig())//
                    .setParallelDownload(isParallelDl)//
                    .setPermissionInterceptor(this.mPermissionInterceptor)
                    .setIcon(icon)
                    .create();

        }
    }

    private DownloadListener getLoadListener() {
        DownloadListener mDownloadListener = this.mDownloadListener;
        return mDownloadListener;
    }

    private WebViewClientCallbackManager mWebViewClientCallbackManager = null;

    private WebChromeClient getChromeClient() {
        IndicatorController mIndicatorController = (this.mIndicatorController == null) ? IndicatorHandler.getInstance().inJectProgressView(mWebCreator.offer()) : this.mIndicatorController;

        DefaultChromeClient mDefaultChromeClient =
                new DefaultChromeClient(this.mActivity, this.mIndicatorController = mIndicatorController, this.mWebChromeClient, this.mChromeClientCallbackManager, this.mIVideo = getIVideo(), mDefaultMsgConfig.getChromeClientMsgCfg(), this.mPermissionInterceptor, mWebCreator.get());

        LogUtils.i(TAG, "WebChromeClient:" + this.mWebChromeClient);
        MiddleWareWebChromeBase header = this.mMiddleWareWebChromeBaseHeader;
        if (header != null) {
            MiddleWareWebChromeBase tail = header;
            int count = 1;
            MiddleWareWebChromeBase tmp = header;
            while (tmp.next() != null) {
                tail = tmp = tmp.next();
                count++;
            }
            LogUtils.i(TAG, "MiddleWareWebClientBase middleware count:" + count);
            tail.setWebChromeClient(mDefaultChromeClient);
            return this.mTargetChromeClient = header;
        } else {
            return this.mTargetChromeClient = mDefaultChromeClient;
        }
    }


    private IVideo getIVideo() {
        return mIVideo == null ? new VideoImpl(mActivity, mWebCreator.get()) : mIVideo;
    }


    public JsInterfaceHolder getJsInterfaceHolder() {
        return this.mJsInterfaceHolder;
    }

    private WebViewClient getClient() {

        LogUtils.i(TAG, "getWebViewClient:" + this.mMiddleWrareWebClientBaseHeader);
        DefaultWebClient mDefaultWebClient = DefaultWebClient
                .createBuilder()
                .setActivity(this.mActivity)
                .setClient(this.mWebViewClient)
                .setManager(this.mWebViewClientCallbackManager)
                .setWebClientHelper(this.webClientHelper)
                .setPermissionInterceptor(this.mPermissionInterceptor)
                .setWebView(this.mWebCreator.get())
                .setInterceptUnkownScheme(this.isInterceptUnkownScheme)
                .setSchemeHandleType(this.openOtherAppWays)
                .setCfg(this.mDefaultMsgConfig.getWebViewClientMsgCfg())
                .build();
        MiddleWareWebClientBase header = this.mMiddleWrareWebClientBaseHeader;
        if (header != null) {
            MiddleWareWebClientBase tail = header;
            int count = 1;
            MiddleWareWebClientBase tmp = header;
            while (tmp.next() != null) {
                tail = tmp = tmp.next();
                count++;
            }
            LogUtils.i(TAG, "MiddleWareWebClientBase middleware count:" + count);
            tail.setWebViewClient(mDefaultWebClient);
            return header;
        } else {
            return mDefaultWebClient;
        }

    }


    public ILoader getLoader() {
        return this.mILoader;
    }


    private SuperWebX5 go(String url) {

        IndicatorController mIndicatorController = null;
        if (!TextUtils.isEmpty(url) && (mIndicatorController = getIndicatorController()) != null && mIndicatorController.offerIndicator() != null) {
            getIndicatorController().offerIndicator().show();
        }
        this.getLoader().loadUrl(url);
        return this;
    }

    private boolean isKillProcess = false;

    public void destroy() {
        this.mWebLifeCycle.onDestroy();
    }

    public void destroyAndKill() {
        destroy();
        if (!SuperWebX5Utils.isMainProcess(mActivity)) {
            LogUtils.i("Info", "退出进程");
            System.exit(0);
        }
    }

    public void uploadFileResult(int requestCode, int resultCode, Intent data) {

        IFileUploadChooser mIFileUploadChooser = null;

        if (mTargetChromeClient instanceof DefaultChromeClient) {
            DefaultChromeClient mDefaultChromeClient = (DefaultChromeClient) mTargetChromeClient;
            mIFileUploadChooser = mDefaultChromeClient.pop();
        }

        if (mIFileUploadChooser == null)
            mIFileUploadChooser = mSuperWebJsInterfaceCompat.pop();
        Log.i("Info", "file upload:" + mIFileUploadChooser);
        if (mIFileUploadChooser != null)
            mIFileUploadChooser.fetchFilePathFromIntent(requestCode, resultCode, data);

        if (mIFileUploadChooser != null)
            mIFileUploadChooser = null;
    }

    public PermissionInterceptor getPermissionInterceptor() {
        return mPermissionInterceptor;
    }


    public static class SuperBuilder {

        private Activity mActivity;
        private ViewGroup mViewGroup;
        private boolean isNeedProgress;
        private int index = -1;
        private BaseIndicatorView v;
        private IndicatorController mIndicatorController = null;
        /*默认进度条是打开的*/
        private boolean enableProgress = true;
        private ViewGroup.LayoutParams mLayoutParams = null;
        private WebViewClient mWebViewClient;
        private WebChromeClient mWebChromeClient;
        private int mIndicatorColor = -1;
        private WebSettings mWebSettings;
        private WebCreator mWebCreator;
        private WebViewClientCallbackManager mWebViewClientCallbackManager = new WebViewClientCallbackManager();
        private SecurityType mSecurityType = SecurityType.default_check;

        private ChromeClientCallbackManager mChromeClientCallbackManager = new ChromeClientCallbackManager();

        private Map<String, String> headers = null;
        private IWebLayout mWebLayout;


        private ArrayMap<String, Object> mJavaObject = null;
        private int mIndicatorColorWithHeight = -1;
        private WebView mWebView;
        private boolean webclientHelper = true;
        public ArrayList<DownLoadResultListener> mDownLoadResultListeners;
        private boolean isParallelDownload = false;
        private int icon = -1;
        private PermissionInterceptor mPermissionInterceptor;


        private MiddleWareWebClientBase tail;
        private MiddleWareWebClientBase header;
        private MiddleWareWebChromeBase mChromeMiddleWareTail;
        private MiddleWareWebChromeBase mChromeMiddleWareHeader;
        private DefaultWebClient.OpenOtherPageWays openOtherPage;
        private boolean isInterceptUnkownScheme;

        private void addJavaObject(String key, Object o) {
            if (mJavaObject == null)
                mJavaObject = new ArrayMap<>();
            mJavaObject.put(key, o);
        }


        private void setIndicatorColor(int indicatorColor) {
            mIndicatorColor = indicatorColor;
        }

        private SuperBuilder(Activity activity) {
            this.mActivity = activity;
        }

        private SuperBuilder enableProgress() {
            this.enableProgress = true;
            return this;
        }

        private SuperBuilder closeProgress() {
            this.enableProgress = false;
            return this;
        }


        private SuperBuilder(WebCreator webCreator) {
            this.mWebCreator = webCreator;
        }


        public ConfigIndicatorBuilder setSuperWebParent(ViewGroup viewGroup, ViewGroup.LayoutParams lp) {
            this.mViewGroup = viewGroup;
            mLayoutParams = lp;
            return new ConfigIndicatorBuilder(this);
        }

        public ConfigIndicatorBuilder setSuperWebParent(ViewGroup viewGroup, ViewGroup.LayoutParams lp, int position) {
            this.mViewGroup = viewGroup;
            mLayoutParams = lp;
            this.index = position;
            return new ConfigIndicatorBuilder(this);
        }

        public ConfigIndicatorBuilder createContentViewTag() {

            this.mViewGroup = null;
            this.mLayoutParams = null;
            return new ConfigIndicatorBuilder(this);
        }


        private void addHeader(String k, String v) {
            if (headers == null)
                headers = new ArrayMap<>();

            headers.put(k, v);

        }


        private PreSuperWeb buildSuperWeb() {
            return new PreSuperWeb(HookManager.hookSuperWeb(new SuperWebX5(this), this));
        }

        private IEventHandler mIEventHandler;


        public void setIndicatorColorWithHeight(int indicatorColorWithHeight) {
            mIndicatorColorWithHeight = indicatorColorWithHeight;
        }
    }

    public static class PreSuperWeb {
        private SuperWebX5 mSuperWebX5;
        private boolean isReady = false;

        PreSuperWeb(SuperWebX5 agentWebX5) {
            this.mSuperWebX5 = agentWebX5;
        }


        public PreSuperWeb ready() {
            if (!isReady) {
                mSuperWebX5.ready();
                isReady = true;
            }
            return this;
        }

        public SuperWebX5 go(@Nullable String url) {
            if (!isReady) {
//                throw new IllegalStateException(" please call ready before go  to finish all webview settings");  //i want to do this , but i cannot;
                ready();
            }
            return mSuperWebX5.go(url);
        }


    }

    public static class ConfigIndicatorBuilder {

        private SuperBuilder mSuperBuilder;

        private ConfigIndicatorBuilder(SuperBuilder superBuilder) {
            this.mSuperBuilder = superBuilder;
        }

        public IndicatorBuilder useDefaultIndicator() {
            this.mSuperBuilder.isNeedProgress = true;
            mSuperBuilder.enableProgress();
            return new IndicatorBuilder(mSuperBuilder);
        }

        public CommonSuperBuilder customProgress(BaseIndicatorView view) {
            this.mSuperBuilder.v = view;

            mSuperBuilder.isNeedProgress = false;
            return new CommonSuperBuilder(mSuperBuilder);
        }

        public CommonSuperBuilder closeProgressBar() {
            mSuperBuilder.closeProgress();
            return new CommonSuperBuilder(mSuperBuilder);
        }


    }

    public static class CommonSuperBuilder {
        private SuperBuilder mSuperBuilder;


        private CommonSuperBuilder(SuperBuilder superBuilder) {
            this.mSuperBuilder = superBuilder;

        }

        public CommonSuperBuilder openParallelDownload() {
            this.mSuperBuilder.isParallelDownload = true;
            return this;
        }

        public CommonSuperBuilder setNotifyIcon(@DrawableRes int icon) {
            this.mSuperBuilder.icon = icon;
            return this;
        }

        public CommonSuperBuilder setWebViewClient(@Nullable WebViewClient webViewClient) {
            this.mSuperBuilder.mWebViewClient = webViewClient;
            return this;
        }


        public CommonSuperBuilder setWebChromeClient(@Nullable WebChromeClient webChromeClient) {
            this.mSuperBuilder.mWebChromeClient = webChromeClient;
            return this;
        }

        public CommonSuperBuilder useMiddleWareWebClient(@NonNull MiddleWareWebClientBase middleWrareWebClientBase) {
            if (middleWrareWebClientBase == null) {
                return this;
            }
            if (this.mSuperBuilder.header == null) {
                this.mSuperBuilder.header = this.mSuperBuilder.tail = middleWrareWebClientBase;
            } else {
                this.mSuperBuilder.tail.enq(middleWrareWebClientBase);
                this.mSuperBuilder.tail = middleWrareWebClientBase;
            }
            return this;
        }

        public CommonSuperBuilder setOpenOtherPageWays(@Nullable DefaultWebClient.OpenOtherPageWays openOtherPageWays) {
            this.mSuperBuilder.openOtherPage = openOtherPageWays;
            return this;
        }

        public CommonSuperBuilder interceptUnkownScheme() {
            this.mSuperBuilder.isInterceptUnkownScheme = true;
            return this;
        }

        public CommonSuperBuilder useMiddleWareWebChrome(@NonNull MiddleWareWebChromeBase middleWareWebChromeBase) {
            if (middleWareWebChromeBase == null) {
                return this;
            }
            if (this.mSuperBuilder.mChromeMiddleWareHeader == null) {
                this.mSuperBuilder.mChromeMiddleWareHeader = this.mSuperBuilder.mChromeMiddleWareTail = middleWareWebChromeBase;
            } else {
                this.mSuperBuilder.mChromeMiddleWareTail.enq(middleWareWebChromeBase);
                this.mSuperBuilder.mChromeMiddleWareTail = middleWareWebChromeBase;
            }
            return this;
        }

        public CommonSuperBuilder setEventHandler(@Nullable IEventHandler iEventHandler) {
            this.mSuperBuilder.mIEventHandler = iEventHandler;
            return this;
        }

        public CommonSuperBuilder setWebSettings(WebSettings webSettings) {
            this.mSuperBuilder.mWebSettings = webSettings;
            return this;
        }


        public CommonSuperBuilder(@Nullable IndicatorController indicatorController) {
            this.mSuperBuilder.mIndicatorController = indicatorController;
        }


        public CommonSuperBuilder addJavascriptInterface(String name, Object o) {
            mSuperBuilder.addJavaObject(name, o);
            return this;
        }

        public CommonSuperBuilder setWebCreator(@Nullable WebCreator webCreator) {
            this.mSuperBuilder.mWebCreator = webCreator;
            return this;
        }

        public CommonSuperBuilder setReceivedTitleCallback(@Nullable ChromeClientCallbackManager.ReceivedTitleCallback receivedTitleCallback) {
            this.mSuperBuilder.mChromeClientCallbackManager.setReceivedTitleCallback(receivedTitleCallback);
            return this;
        }

        public CommonSuperBuilder setSecutityType(@Nullable SecurityType secutityType) {
            this.mSuperBuilder.mSecurityType = secutityType;
            return this;
        }

        public CommonSuperBuilder setWebView(@Nullable WebView webView) {
            this.mSuperBuilder.mWebView = webView;
            return this;
        }

        public CommonSuperBuilder setWebLayout(@NonNull IWebLayout webLayout) {
            this.mSuperBuilder.mWebLayout = webLayout;
            return this;
        }

        public CommonSuperBuilder additionalHttpHeader(String k, String v) {
            this.mSuperBuilder.addHeader(k, v);
            return this;
        }

        public CommonSuperBuilder closeWebViewClientHelper() {
            mSuperBuilder.webclientHelper = false;
            return this;
        }

        public CommonSuperBuilder setPermissionInterceptor(PermissionInterceptor permissionInterceptor) {
            this.mSuperBuilder.mPermissionInterceptor = permissionInterceptor;
            return this;
        }

        public CommonSuperBuilder addDownLoadResultListener(DownLoadResultListener downLoadResultListener) {

            if (this.mSuperBuilder.mDownLoadResultListeners == null) {
                this.mSuperBuilder.mDownLoadResultListeners = new ArrayList<>();
            }
            this.mSuperBuilder.mDownLoadResultListeners.add(downLoadResultListener);
            return this;
        }

        public PreSuperWeb createSuperWeb() {
            return mSuperBuilder.buildSuperWeb();
        }

    }

    public static enum SecurityType {
        default_check, strict;
    }

    public static class IndicatorBuilder {

        private SuperBuilder mSuperBuilder = null;

        private IndicatorBuilder(SuperBuilder builder) {
            this.mSuperBuilder = builder;
        }

        public CommonSuperBuilder setIndicatorColor(int color) {
            mSuperBuilder.setIndicatorColor(color);
            return new CommonSuperBuilder(mSuperBuilder);
        }

        public CommonSuperBuilder defaultProgressBarColor() {
            mSuperBuilder.setIndicatorColor(-1);
            return new CommonSuperBuilder(mSuperBuilder);
        }

        public CommonSuperBuilder setIndicatorColorWithHeight(@ColorInt int color, int height_dp) {
            mSuperBuilder.setIndicatorColor(color);
            mSuperBuilder.setIndicatorColorWithHeight(height_dp);
            return new CommonSuperBuilder(mSuperBuilder);
        }


    }


    public static final class SuperBuilderFragment {
        private Activity mActivity;
        private Fragment mFragment;

        private ViewGroup mViewGroup;
        private boolean isNeedDefaultProgress;
        private int index = -1;
        private BaseIndicatorView v;
        private IndicatorController mIndicatorController = null;
        /*默认进度条是打开的*/
        private boolean enableProgress = true;
        private ViewGroup.LayoutParams mLayoutParams = null;
        private WebViewClient mWebViewClient;
        private WebChromeClient mWebChromeClient;
        private int mIndicatorColor = -1;
        private WebSettings mWebSettings;
        private WebCreator mWebCreator;
        private Map<String, String> additionalHttpHeaders = null;
        private IEventHandler mIEventHandler;
        private int height_dp = -1;
        private ArrayMap<String, Object> mJavaObject;
        private ChromeClientCallbackManager mChromeClientCallbackManager = new ChromeClientCallbackManager();
        private SecurityType mSecurityType = SecurityType.default_check;
        private WebView mWebView;
        private WebViewClientCallbackManager mWebViewClientCallbackManager = new WebViewClientCallbackManager();
        private boolean webClientHelper = true;
        private List<DownLoadResultListener> mDownLoadResultListeners = null;
        private IWebLayout webLayout;
        private boolean isParallelDownload;
        private int icon = -1;
        private PermissionInterceptor mPermissionInterceptor;
        private MiddleWareWebClientBase tail;
        private MiddleWareWebClientBase header;
        private MiddleWareWebChromeBase mChromeMiddleWareTail;
        private MiddleWareWebChromeBase mChromeMiddleWareHeader;
        private DefaultWebClient.OpenOtherPageWays openOtherPage;
        private boolean isInterceptUnkownScheme;


        public SuperBuilderFragment(@NonNull Activity activity, @NonNull Fragment fragment) {
            mActivity = activity;
            mFragment = fragment;
        }

        public IndicatorBuilderForFragment setSuperWebParent(@NonNull ViewGroup v, @NonNull ViewGroup.LayoutParams lp) {
            this.mViewGroup = v;
            this.mLayoutParams = lp;
            return new IndicatorBuilderForFragment(this);
        }

        private PreSuperWeb buildSuperWeb() {
            if (this.mViewGroup == null)
                throw new NullPointerException("ViewGroup is null,please check you params");
            return new PreSuperWeb(HookManager.hookSuperWeb(new SuperWebX5(this), this));
        }

        private void addJavaObject(String key, Object o) {
            if (mJavaObject == null)
                mJavaObject = new ArrayMap<>();
            mJavaObject.put(key, o);
        }

        private void addHeader(String k, String v) {

            if (additionalHttpHeaders == null)
                additionalHttpHeaders = new ArrayMap<>();
            additionalHttpHeaders.put(k, v);

        }
    }

    public static class IndicatorBuilderForFragment {
        SuperBuilderFragment superBuilderFragment = null;

        public IndicatorBuilderForFragment(SuperBuilderFragment superBuilderFragment) {
            this.superBuilderFragment = superBuilderFragment;
        }

        public CommonBuilderForFragment useDefaultIndicator(int color) {
            this.superBuilderFragment.enableProgress = true;
            this.superBuilderFragment.mIndicatorColor = color;
            return new CommonBuilderForFragment(superBuilderFragment);
        }

        public CommonBuilderForFragment useDefaultIndicator() {
            this.superBuilderFragment.enableProgress = true;
            return new CommonBuilderForFragment(superBuilderFragment);
        }

        public CommonBuilderForFragment closeDefaultIndicator() {
            this.superBuilderFragment.enableProgress = false;
            this.superBuilderFragment.mIndicatorColor = -1;
            this.superBuilderFragment.height_dp = -1;
            return new CommonBuilderForFragment(superBuilderFragment);
        }

        public CommonBuilderForFragment setCustomIndicator(@NonNull BaseIndicatorView v) {
            if (v != null) {
                this.superBuilderFragment.enableProgress = true;
                this.superBuilderFragment.v = v;
                this.superBuilderFragment.isNeedDefaultProgress = false;
            } else {
                this.superBuilderFragment.enableProgress = true;
                this.superBuilderFragment.isNeedDefaultProgress = true;
            }

            return new CommonBuilderForFragment(superBuilderFragment);
        }

        public CommonBuilderForFragment setIndicatorColorWithHeight(@ColorInt int color, int height_dp) {
            this.superBuilderFragment.mIndicatorColor = color;
            this.superBuilderFragment.height_dp = height_dp;
            return new CommonBuilderForFragment(this.superBuilderFragment);
        }

    }


    public static class CommonBuilderForFragment {
        private SuperBuilderFragment mSuperBuilderFragment;

        public CommonBuilderForFragment(SuperBuilderFragment superBuilderFragment) {
            this.mSuperBuilderFragment = superBuilderFragment;
        }

        public CommonBuilderForFragment setEventHanadler(@Nullable IEventHandler iEventHandler) {
            mSuperBuilderFragment.mIEventHandler = iEventHandler;
            return this;
        }

        public CommonBuilderForFragment closeWebViewClientHelper() {
            mSuperBuilderFragment.webClientHelper = false;
            return this;
        }

        public CommonBuilderForFragment setWebCreator(@Nullable WebCreator webCreator) {
            this.mSuperBuilderFragment.mWebCreator = webCreator;
            return this;
        }

        public CommonBuilderForFragment setWebChromeClient(@Nullable WebChromeClient webChromeClient) {
            this.mSuperBuilderFragment.mWebChromeClient = webChromeClient;
            return this;

        }

        public CommonBuilderForFragment setPermissionInterceptor(PermissionInterceptor permissionInterceptor) {
            this.mSuperBuilderFragment.mPermissionInterceptor = permissionInterceptor;
            return this;
        }

        public CommonBuilderForFragment setWebViewClient(@Nullable WebViewClient webChromeClient) {
            this.mSuperBuilderFragment.mWebViewClient = webChromeClient;
            return this;
        }

        public CommonBuilderForFragment useMiddleWareWebClient(@NonNull MiddleWareWebClientBase middleWrareWebClientBase) {
            if (middleWrareWebClientBase == null) {
                return this;
            }
            if (this.mSuperBuilderFragment.header == null) {
                this.mSuperBuilderFragment.header = this.mSuperBuilderFragment.tail = middleWrareWebClientBase;
            } else {
                this.mSuperBuilderFragment.tail.enq(middleWrareWebClientBase);
                this.mSuperBuilderFragment.tail = middleWrareWebClientBase;
            }
            return this;
        }

        public CommonBuilderForFragment setOpenOtherPageWays(@Nullable DefaultWebClient.OpenOtherPageWays openOtherPageWays) {
            this.mSuperBuilderFragment.openOtherPage = openOtherPageWays;
            return this;
        }

        public CommonBuilderForFragment interceptUnkownScheme() {
            this.mSuperBuilderFragment.isInterceptUnkownScheme = true;
            return this;
        }

        public CommonBuilderForFragment useMiddleWareWebChrome(@NonNull MiddleWareWebChromeBase middleWareWebChromeBase) {
            if (middleWareWebChromeBase == null) {
                return this;
            }
            if (this.mSuperBuilderFragment.mChromeMiddleWareHeader == null) {
                this.mSuperBuilderFragment.mChromeMiddleWareHeader = this.mSuperBuilderFragment.mChromeMiddleWareTail = middleWareWebChromeBase;
            } else {
                this.mSuperBuilderFragment.mChromeMiddleWareTail.enq(middleWareWebChromeBase);
                this.mSuperBuilderFragment.mChromeMiddleWareTail = middleWareWebChromeBase;
            }
            return this;
        }


        public CommonBuilderForFragment setWebSettings(@Nullable WebSettings webSettings) {
            this.mSuperBuilderFragment.mWebSettings = webSettings;
            return this;
        }

        public PreSuperWeb createSuperWeb() {
            return this.mSuperBuilderFragment.buildSuperWeb();
        }

        public CommonBuilderForFragment setReceivedTitleCallback(@Nullable ChromeClientCallbackManager.ReceivedTitleCallback receivedTitleCallback) {
            this.mSuperBuilderFragment.mChromeClientCallbackManager.setReceivedTitleCallback(receivedTitleCallback);
            return this;
        }

        public CommonBuilderForFragment addJavascriptInterface(@NonNull String name, @NonNull Object o) {
            this.mSuperBuilderFragment.addJavaObject(name, o);
            return this;
        }

        public CommonBuilderForFragment setSecurityType(SecurityType type) {
            this.mSuperBuilderFragment.mSecurityType = type;
            return this;
        }

        public CommonBuilderForFragment setWebView(@Nullable WebView webView) {
            this.mSuperBuilderFragment.mWebView = webView;
            return this;
        }

        public CommonBuilderForFragment additionalHttpHeaders(String k, String v) {
            this.mSuperBuilderFragment.addHeader(k, v);

            return this;
        }

        public CommonBuilderForFragment openParallelDownload() {
            this.mSuperBuilderFragment.isParallelDownload = true;
            return this;
        }

        public CommonBuilderForFragment setNotifyIcon(@DrawableRes int icon) {
            this.mSuperBuilderFragment.icon = icon;
            return this;
        }

        public CommonBuilderForFragment setWebLayout(@Nullable IWebLayout iWebLayout) {
            this.mSuperBuilderFragment.webLayout = iWebLayout;
            return this;
        }


        public CommonBuilderForFragment addDownLoadResultListener(DownLoadResultListener downLoadResultListener) {

            if (this.mSuperBuilderFragment.mDownLoadResultListeners == null) {
                this.mSuperBuilderFragment.mDownLoadResultListeners = new ArrayList<>();
            }
            this.mSuperBuilderFragment.mDownLoadResultListeners.add(downLoadResultListener);
            return this;
        }
    }


}
