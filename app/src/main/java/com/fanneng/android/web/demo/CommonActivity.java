package com.fanneng.android.web.demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

/**
 * describe：公共加载fragment类
 *
 * @author ：鲁宇峰 on 2018/8/8 13：44
 *         email：luyufengc@enn.cn
 */
public class CommonActivity extends AppCompatActivity {


    public static final String TYPE_KEY = "type_key";
    private FragmentManager mFragmentManager;
    private SuperWebX5Fragment mSuperWebX5Fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        int key = getIntent().getIntExtra(TYPE_KEY, -1);
        mFragmentManager = this.getSupportFragmentManager();
        openFragment(key);
    }


    private void openFragment(int key) {

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle = null;

        switch (key) {
            //文件浏览功能(pdf/ppt/doc/excel/txt)
            case 0:
                FileListActivity.show(CommonActivity.this);
                finish();
                break;
            //文件上传(Android与JS通信)
            case 1:
                ft.add(R.id.container_framelayout, mSuperWebX5Fragment = SuperWebX5Fragment.getInstance(mBundle = new Bundle()), SuperWebX5Fragment.class.getName());
                mBundle.putString(SuperWebX5Fragment.URL_KEY, "file:///android_asset/upload_file/jsuploadfile.html");
                break;
            //文件下载功能
            case 2:
                checkNotification();


                ft.add(R.id.container_framelayout, mSuperWebX5Fragment = SuperWebX5Fragment.getInstance(mBundle = new Bundle()), SuperWebX5Fragment.class.getName());
                mBundle.putString(SuperWebX5Fragment.URL_KEY, "http://www.wandoujia.com/apps");
                break;
            default:
                break;
        }
        ft.commit();

    }

    private void checkNotification() {
        if (!NotificationsUtils.isNotificationEnabled(this)) {
            new AlertDialog.Builder(this)
                    .setMessage("检测到您没有打开通知权限，是否去打开?")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent localIntent = new Intent();
                            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                            localIntent.setData(Uri.fromParts("package", CommonActivity.this.getPackageName(), null));
                            startActivity(localIntent);
                        }
                    })
                    .create().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSuperWebX5Fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        SuperWebX5Fragment mAgentWebX5Fragment = this.mSuperWebX5Fragment;
        if (mAgentWebX5Fragment != null) {
            FragmentKeyDown mFragmentKeyDown = mAgentWebX5Fragment;
            if (mFragmentKeyDown.onFragmentKeyDown(keyCode, event)) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
