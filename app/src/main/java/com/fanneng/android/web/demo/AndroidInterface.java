package com.fanneng.android.web.demo;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.fanneng.android.web.SuperWebX5;

/**
 * describe：公共加载fragment类
 *
 * @author ：鲁宇峰 on 2018/8/8 13：44
 *         email：luyufengc@enn.cn
 */
public class AndroidInterface {


    private SuperWebX5 superWeb;
    private Context context;

    public AndroidInterface(SuperWebX5 superWeb, Context context) {
        this.superWeb = superWeb;
        this.context = context;
    }

    private Handler deliver = new Handler(Looper.getMainLooper());

    @JavascriptInterface
    public void callAndroid(final String msg) {


        deliver.post(new Runnable() {
            @Override
            public void run() {

                Log.i("Info", "main Thread:" + Thread.currentThread());
                Toast.makeText(context.getApplicationContext(), "" + msg, Toast.LENGTH_LONG).show();
            }
        });


        Log.i("Info", "Thread:" + Thread.currentThread());

            //对外接口


    }

}
