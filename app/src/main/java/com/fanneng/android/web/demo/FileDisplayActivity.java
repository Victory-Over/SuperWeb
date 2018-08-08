package com.fanneng.android.web.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.fanneng.android.web.file.FileReaderView;

/**
 * describe：文件阅读类
 *
 * @author ：鲁宇峰 on 2018/8/7 11：14
 *         email：luyufengc@enn.cn
 */
public class FileDisplayActivity extends AppCompatActivity {

    private FileReaderView mDocumentReaderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_display);
        init();
    }


    public void init() {
        mDocumentReaderView = findViewById(R.id.documentReaderView);
        mDocumentReaderView.show(getIntent().getStringExtra("path"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDocumentReaderView != null) {
            mDocumentReaderView.stop();
        }
    }


    public static void show(Context context, String url) {
        Intent intent = new Intent(context, FileDisplayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("path", url);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }
}
