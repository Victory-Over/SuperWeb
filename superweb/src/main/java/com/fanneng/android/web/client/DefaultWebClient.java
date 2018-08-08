package com.fanneng.android.web.client;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.fanneng.android.web.utils.DefaultMsgConfig;
import com.fanneng.android.web.SuperWebX5Config;
import com.fanneng.android.web.utils.PermissionInterceptor;
import com.fanneng.android.web.utils.LogUtils;
import com.fanneng.android.web.utils.SuperWebX5Utils;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 默认的web客户端
 */
public class DefaultWebClient extends WrapperWebViewClient {

	private WebViewClientCallbackManager mWebViewClientCallbackManager;
	private WeakReference<Activity> mWeakReference = null;
	private static final int CONSTANTS_ABNORMAL_BIG = 7;
	private WebViewClient mWebViewClient;
	private boolean webClientHelper = false;
	private static final String WEBVIEWCLIENTPATH = "com.tencent.smtt.sdk.WebViewClient";
	public static final String INTENT_SCHEME = "intent://";
	public static final String WEBCHAT_PAY_SCHEME = "weixin://wap/pay?";

	public static final int DERECT_OPEN_OTHER_APP = 1001;
	public static final int ASK_USER_OPEN_OTHER_APP = DERECT_OPEN_OTHER_APP >> 2;
	public static final int DISALLOW_OPEN_OTHER_APP = DERECT_OPEN_OTHER_APP >> 4;
	private boolean isInterceptUnkownScheme = true;

	public static final String ALIPAYS_SCHEME = "alipays://";
	public static final String HTTP_SCHEME = "http://";
	public static final String HTTPS_SCHEME = "https://";

	private static final boolean hasAlipayLib;

	static {
		boolean tag = true;
		try {
			Class.forName("com.alipay.sdk.app.PayTask");
		} catch (Throwable ignore) {
			tag = false;
		}
		hasAlipayLib = tag;

		LogUtils.i("Info", "static  hasAlipayLib:" + hasAlipayLib);
	}


	public int schemeHandleType = ASK_USER_OPEN_OTHER_APP;
	private DefaultMsgConfig.WebViewClientMsgCfg mMsgCfg = null;
	private WebView mWebView;

	DefaultWebClient(Builder builder) {
		super(builder.client);
		this.mWebView = builder.webView;
		this.mWebViewClient = builder.client;
		mWeakReference = new WeakReference<Activity>(builder.activity);
		this.mWebViewClientCallbackManager = builder.manager;
		this.webClientHelper = builder.webClientHelper;
		isInterceptUnkownScheme = builder.isInterceptUnkownScheme;

		LogUtils.i(TAG, "schemeHandleType:" + schemeHandleType);
		if (builder.schemeHandleType <= 0) {
			schemeHandleType = ASK_USER_OPEN_OTHER_APP;
		} else {
			schemeHandleType = builder.schemeHandleType;
		}
		this.mMsgCfg = builder.mCfg;
	}


	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		LogUtils.i(TAG, "shouldOverrideUrlLoading --->  url:" + url);


		int tag = -1;

		if (SuperWebX5Utils.isOverriedMethod(mWebViewClient, "shouldOverrideUrlLoading", WEBVIEWCLIENTPATH + ".shouldOverrideUrlLoading", WebView.class, String.class) && (((tag = 1) > 0) && super.shouldOverrideUrlLoading(view, url))) {
			return true;
		}

		if (url.startsWith(HTTP_SCHEME) || url.startsWith(HTTPS_SCHEME)) {
			return (webClientHelper && hasAlipayLib && isAlipay(view, url));
		}

		if (!webClientHelper) {
			return false;
		}
		if (handleLinked(url)) { //电话 ， 邮箱 ， 短信
			return true;
		}
		if (url.startsWith(INTENT_SCHEME)) { //Intent scheme
			handleIntentUrl(url);
			return true;
		}

		if (url.startsWith(WEBCHAT_PAY_SCHEME)) { //微信支付
			startActivity(url);
			return true;
		}
		if (url.startsWith(ALIPAYS_SCHEME) && openOtherPage(url)) {//支付宝
			return true;
		}

		if (queryActivies(url) > 0 && handleOtherScheme(url)) { //打开Scheme 相对应的页面
			LogUtils.i(TAG, "intercept OtherAppScheme");
			return true;
		}
		if (isInterceptUnkownScheme) { // 手机里面没有页面能匹配到该链接 ， 也就是无法处理的scheme返回True，拦截下来。
			LogUtils.i(TAG, "intercept InterceptUnkownScheme : " + url);
			return true;
		}

		if (tag > 0)
			return false;


		return super.shouldOverrideUrlLoading(view, url);
	}

	private int queryActivies(String url) {

		try {
			if (mWeakReference.get() == null) {
				return 0;
			}
			Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
			PackageManager mPackageManager = mWeakReference.get().getPackageManager();
			List<ResolveInfo> mResolveInfos = mPackageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
			return mResolveInfos == null ? 0 : mResolveInfos.size();
		} catch (URISyntaxException ignore) {
			if (LogUtils.isDebug()) {
				ignore.printStackTrace();
			}
			return 0;
		}
	}

	private AlertDialog askOpenOtherAppDialog = null;

	private boolean handleOtherScheme(final String url) {

		switch (schemeHandleType) {

			case DERECT_OPEN_OTHER_APP: //直接打开其他App
				openOtherPage(url);
				return true;
			case ASK_USER_OPEN_OTHER_APP:  //咨询用户是否打开其他App
				if (mWeakReference.get() != null) {
					askOpenOtherAppDialog = new AlertDialog
							.Builder(mWeakReference.get())//
							.setMessage(String.format(mMsgCfg.getLeaveApp(), getApplicationName(mWebView.getContext())))//
							.setTitle(mMsgCfg.getTitle())
							.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									if (dialog != null) {
										dialog.dismiss();
									}
								}
							})//
							.setPositiveButton(mMsgCfg.getConfirm(), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									if (dialog != null) {
										dialog.dismiss();
									}
									openOtherPage(url);
								}
							})
							.create();
				}
				askOpenOtherAppDialog.show();
				return true;
			default://默认不打开
				return false;
		}
	}

	private String getApplicationName(Context context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName =
				(String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	private boolean openOtherPage(String intentUrl) {
		try {
			Intent intent;
			Activity mActivity = null;
			if ((mActivity = mWeakReference.get()) == null)
				return true;
			PackageManager packageManager = mActivity.getPackageManager();
			intent = new Intent().parseUri(intentUrl, Intent.URI_INTENT_SCHEME);
			ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
			LogUtils.i(TAG, "resolveInfo:" + info + "   package:" + intent.getPackage());
			if (info != null) {  //跳到该应用
				mActivity.startActivity(intent);
				return true;
			}
		} catch (Throwable ignore) {
			if (LogUtils.isDebug()) {
				ignore.printStackTrace();
			}
		}

		return false;
	}

	private Object mPayTask; //alipay

	private boolean isAlipay(final android.webkit.WebView view, String url) {

		try {

			Activity mActivity = null;
			if ((mActivity = mWeakReference.get()) == null)
				return false;
			/**
			 * 推荐采用的新的二合一接口(payInterceptorWithUrl),只需调用一次
			 */
			if (mPayTask == null) {
				Class clazz = Class.forName("com.alipay.sdk.app.PayTask");
				Constructor<?> mConstructor = clazz.getConstructor(Activity.class);
				mPayTask = mConstructor.newInstance(mActivity);
			}
			final PayTask task = (PayTask) mPayTask;
			boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
				@Override
				public void onPayResult(final H5PayResultModel result) {
					final String url = result.getReturnUrl();
					if (!TextUtils.isEmpty(url)) {
						SuperWebX5Utils.runInUiThread(new Runnable() {
							@Override
							public void run() {
								view.loadUrl(url);
							}
						});
					}
				}
			});
			LogUtils.i(TAG, "alipay-isIntercepted:" + isIntercepted + "  url:" + url);
			return isIntercepted;
		} catch (Throwable ignore) {
			if (SuperWebX5Config.DEBUG) {
				ignore.printStackTrace();
			}
		}
		return false;
	}

	public static final String SCHEME_SMS = "sms:";


	private boolean handleLinked(String url) {
		if (url.startsWith(android.webkit.WebView.SCHEME_TEL)
				|| url.startsWith(SCHEME_SMS)
				|| url.startsWith(android.webkit.WebView.SCHEME_MAILTO)
				|| url.startsWith(android.webkit.WebView.SCHEME_GEO)) {
			try {
				Activity mActivity = null;
				if ((mActivity = mWeakReference.get()) == null)
					return false;
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				mActivity.startActivity(intent);
			} catch (ActivityNotFoundException ignored) {
				if (SuperWebX5Config.DEBUG) {
					ignored.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}


	private boolean isAlipay(final WebView view, String url) {

		Activity mActivity = null;
		if ((mActivity = mWeakReference.get()) == null)
			return false;
		final PayTask task = new PayTask(mActivity);
		final String ex = task.fetchOrderInfoFromH5PayUrl(url);
		LogUtils.i("Info", "alipay:" + ex);
		if (!TextUtils.isEmpty(ex)) {
			//System.out.println("paytask:::::" + url);
			AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
				public void run() {
					System.out.println("payTask:::" + ex);
					final H5PayResultModel result = task.h5Pay(ex, true);
					if (!TextUtils.isEmpty(result.getReturnUrl())) {
						SuperWebX5Utils.runInUiThread(new Runnable() {

							@Override
							public void run() {
								view.loadUrl(result.getReturnUrl());
							}
						});
					}
				}
			});

			return true;
		}
		return false;
	}

	private void handleIntentUrl(String intentUrl) {
		try {

			Intent intent = null;
			if (TextUtils.isEmpty(intentUrl) || !intentUrl.startsWith(INTENT_SCHEME))
				return;

			Activity mActivity = null;
			if ((mActivity = mWeakReference.get()) == null)
				return;
			PackageManager packageManager = mActivity.getPackageManager();
			intent = new Intent().parseUri(intentUrl, Intent.URI_INTENT_SCHEME);
			ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
			LogUtils.i("Info", "resolveInfo:" + info + "   package:" + intent.getPackage());
			if (info != null) {  //跳到该应用
				mActivity.startActivity(intent);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	private boolean handleNormalLinked(String url) {
		if (url.startsWith(WebView.SCHEME_TEL) || url.startsWith("sms:") || url.startsWith(WebView.SCHEME_MAILTO)) {
			try {
				Activity mActivity = null;
				if ((mActivity = mWeakReference.get()) == null)
					return false;
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				mActivity.startActivity(intent);
			} catch (ActivityNotFoundException ignored) {
			}
			return true;
		}
		return false;
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		LogUtils.i("Info", "onPageStarted");
		if (SuperWebX5Config.WEBVIEW_TYPE == SuperWebX5Config.WEBVIEW_SUPERWEB_SAFE_TYPE && mWebViewClientCallbackManager.getPageLifeCycleCallback() != null) {
			mWebViewClientCallbackManager.getPageLifeCycleCallback().onPageStarted(view, url, favicon);
		}
		super.onPageStarted(view, url, favicon);

	}


	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
		LogUtils.i("Info", "onReceivedError：" + description + "  CODE:" + errorCode);
	}

	@Override
	public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
		super.onReceivedError(view, request, error);
		LogUtils.i("Info", "onReceivedError:" + error.toString());

	}

	@Override
	public void onPageFinished(WebView view, String url) {
		if (SuperWebX5Config.WEBVIEW_TYPE == SuperWebX5Config.WEBVIEW_SUPERWEB_SAFE_TYPE && mWebViewClientCallbackManager.getPageLifeCycleCallback() != null) {
			mWebViewClientCallbackManager.getPageLifeCycleCallback().onPageFinished(view, url);
		}
		super.onPageFinished(view, url);

		LogUtils.i("Info", "onPageFinished");
	}


	@Override
	public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
		LogUtils.i("Info", "shouldOverrideKeyEvent");
		return super.shouldOverrideKeyEvent(view, event);
	}


	private void startActivity(String url) {


		try {

			if (mWeakReference.get() == null)
				return;

			LogUtils.i("Info", "start wechat pay Activity");
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			mWeakReference.get().startActivity(intent);

		} catch (Exception e) {
			LogUtils.i("Info", "支付异常");
			e.printStackTrace();
		}


	}

	@Override
	public void onScaleChanged(WebView view, float oldScale, float newScale) {


		if (SuperWebX5Utils.isOverriedMethod(mWebViewClient, "onScaleChanged", WEBVIEWCLIENTPATH + ".onScaleChanged", WebView.class, float.class, float.class)) {
			super.onScaleChanged(view, oldScale, newScale);
			return;
		}

		LogUtils.i("Info", "onScaleChanged:" + oldScale + "   n:" + newScale);
		if (newScale - oldScale > CONSTANTS_ABNORMAL_BIG) {
			view.setInitialScale((int) (oldScale / newScale * 100));
		}

	}

	public static Builder createBuilder() {
		return new Builder();
	}

	public static enum OpenOtherPageWays {
		DERECT(DefaultWebClient.DERECT_OPEN_OTHER_APP), ASK(DefaultWebClient.ASK_USER_OPEN_OTHER_APP), DISALLOW(DefaultWebClient.DISALLOW_OPEN_OTHER_APP);
		public int code;

		OpenOtherPageWays(int code) {
			this.code = code;
		}
	}


	public static class Builder {

		private Activity activity;
		private WebViewClient client;
		private WebViewClientCallbackManager manager;
		private boolean webClientHelper;
		private PermissionInterceptor permissionInterceptor;
		private WebView webView;
		private boolean isInterceptUnkownScheme;
		private int schemeHandleType;
		private DefaultMsgConfig.WebViewClientMsgCfg mCfg;

		public Builder setCfg(DefaultMsgConfig.WebViewClientMsgCfg cfg) {
			mCfg = cfg;
			return this;
		}

		public Builder setActivity(Activity activity) {
			this.activity = activity;
			return this;
		}

		public Builder setClient(WebViewClient client) {
			this.client = client;
			return this;
		}

		public Builder setManager(WebViewClientCallbackManager manager) {
			this.manager = manager;
			return this;
		}

		public Builder setWebClientHelper(boolean webClientHelper) {
			this.webClientHelper = webClientHelper;
			return this;
		}

		public Builder setPermissionInterceptor(PermissionInterceptor permissionInterceptor) {
			this.permissionInterceptor = permissionInterceptor;
			return this;
		}

		public Builder setWebView(WebView webView) {
			this.webView = webView;
			return this;
		}

		public Builder setInterceptUnkownScheme(boolean interceptUnkownScheme) {
			this.isInterceptUnkownScheme = interceptUnkownScheme;
			return this;
		}

		public Builder setSchemeHandleType(int schemeHandleType) {
			this.schemeHandleType = schemeHandleType;
			return this;
		}

		public DefaultWebClient build() {
			return new DefaultWebClient(this);
		}
	}


}
