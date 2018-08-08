package com.fanneng.android.web;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;

import com.fanneng.android.web.utils.LogUtils;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;


/**
 * 配置类(设置/获取/清除cookies等操作)
 */

public class SuperWebX5Config {


    static final String SUPERWEB_CACHE_PATCH = "/superweb_cache";

    static final String DOWNLOAD_PATH = "download";

    public static String SUPERWEB_FILE_PATH;
    public static final String FILE_CACHE_PATH = "superweb-cache";

    public static boolean DEBUG = false;
    static final boolean isKikatOrBelowKikat = Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT;
//    static final boolean isKikatOrBelowKikat= true;


    public static final int WEBVIEW_DEFAULT_TYPE = 1;
    public static final int WEBVIEW_SUPERWEB_SAFE_TYPE = 2;
    public static final int WEBVIEW_CUSTOM_TYPE = 3;

    public static int WEBVIEW_TYPE = WEBVIEW_DEFAULT_TYPE;


    //获取Cookie
    public static String getCookiesByUrl(String url) {
        return CookieManager.getInstance() == null ? null : CookieManager.getInstance().getCookie(url);
    }


    public static void removeExpiredCookies() {
        CookieManager mCookieManager = null;
        if ((mCookieManager = CookieManager.getInstance()) != null) { //同步清除{
            mCookieManager.removeExpiredCookie();
            toSyncCookies();
        }
    }

    public static void removeAllCookies() {
        removeAllCookies(null);

    }

    // 解决兼容 Android 4.4 java.lang.NoSuchMethodError: android.webkit.CookieManager.removeSessionCookies
    public static void removeSessionCookies() {
        removeSessionCookies(null);
    }

    public static void removeSessionCookies(ValueCallback<Boolean> callback) {

        if (callback == null)
            callback = getDefaultIgnoreCallback();
        if (CookieManager.getInstance() == null) {
            callback.onReceiveValue(new Boolean(false));
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeSessionCookie();
            toSyncCookies();
            callback.onReceiveValue(new Boolean(true));
            return;
        }
        CookieManager.getInstance().removeSessionCookies(callback);
        toSyncCookies();

    }

    //Android  4.4  NoSuchMethodError: android.webkit.CookieManager.removeAllCookies
    public static void removeAllCookies(@Nullable ValueCallback<Boolean> callback) {

        if (callback == null)
            callback = getDefaultIgnoreCallback();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookie();
            toSyncCookies();
            callback.onReceiveValue(!CookieManager.getInstance().hasCookies());
            return;
        }
        CookieManager.getInstance().removeAllCookies(callback);
        toSyncCookies();
    }

    private static ValueCallback<Boolean> getDefaultIgnoreCallback() {

        return new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean ignore) {
                LogUtils.i("Info", "removeExpiredCookies:" + ignore);
            }
        };
    }


    /*public static void syncCookie(Context context, String cookies, String url) {

        try {
           // createCookiesSyncInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setCookie(url, cookies);
            toSyncCookies();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/


    private static boolean isInit = false;

    static synchronized void initCookiesManager(Context context) {
        if (!isInit) {
            createCookiesSyncInstance(context);
            isInit = true;
        }
    }

    public static String getCachePath(Context context) {
        return context.getCacheDir().getAbsolutePath() + SUPERWEB_CACHE_PATCH;
    }

    public static String getDatabasesCachePath(Context context) {
        return context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
    }


    public static void syncCookie(String url, String cookies) {

        CookieManager mCookieManager = CookieManager.getInstance();
        if (mCookieManager != null) {
            mCookieManager.setCookie(url, cookies);
            toSyncCookies();
        }
    }

    private static void createCookiesSyncInstance(Context context) {


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context);
        }
    }

    private static void toSyncCookies() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
            return;
        }
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {

                CookieManager.getInstance().flush();

            }
        });
    }

}
