package com.fanneng.android.web.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * describe：功能列表
 *
 * @author ：鲁宇峰 on 2018/8/8 13：44
 *         email：luyufengc@enn.cn
 */
public class FileListActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    private String filePath;
    private List<String> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        copy();
        initDatas();
        initPaths();

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                return new RecyclerView.ViewHolder(LayoutInflater.from(FileListActivity.this).inflate(R.layout.item_function, parent, false)) {
                };

            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                holder.itemView.findViewById(R.id.bt_function).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filePath = getFilePath(position);
                        FileDisplayActivity.show(FileListActivity.this, filePath);
                    }
                });
                ((Button) holder.itemView.findViewById(R.id.bt_function)).setText(getDatas().get(position));
            }

            @Override
            public int getItemCount() {
                return getDatas().size();
            }
        });
    }

    private void initPaths() {
    }

    private void initDatas() {
        datas.add("打开本地doc文件");

        datas.add("打开本地txt文件");

        datas.add("打开本地excel文件");

        datas.add("打开本地ppt文件");

        datas.add("打开本地pdf文件");
    }

    private List<String> getDatas() {

        if (datas != null && datas.size() > 0) {
            return datas;
        } else {
            datas = new ArrayList<>();
            initDatas();
            return datas;
        }

    }

    private String getFilePath(int position) {
        String path = null;
        switch (position) {
            case 0:
                path = getFilesDir().getAbsolutePath() + File.separator + "test.docx";
                break;

            case 1:
                path = getFilesDir().getAbsolutePath() + File.separator +"test.txt";
                break;

            case 2:
                path = getFilesDir().getAbsolutePath() + File.separator +"test.xlsx";
                break;

            case 3:
                path = getFilesDir().getAbsolutePath() + File.separator +"test.pptx";
                break;

            case 4:
                path = getFilesDir().getAbsolutePath() + File.separator +"test.pdf";
                break;

            default:
                break;
        }
        return path;
    }

    public static void show(Context context) {
        Intent intent = new Intent(context, FileListActivity.class);
        context.startActivity(intent);
    }


    private void copy() {
        // 开始复制
        String path = "file" + File.separator;
        copyAssetsFileToAppFiles(path + "test.docx", "test.docx");
        copyAssetsFileToAppFiles(path + "test.pdf", "test.pdf");
        copyAssetsFileToAppFiles(path + "test.pptx", "test.pptx");
        copyAssetsFileToAppFiles(path + "test.txt", "test.txt");
        copyAssetsFileToAppFiles(path + "test.xlsx", "test.xlsx");
    }


    /**
     * 从assets目录中复制某文件内容
     *
     * @param assetFileName assets目录下的文件
     * @param newFileName   复制到/data/data/package_name/files/目录下文件名
     */
    private void copyAssetsFileToAppFiles(String assetFileName, String newFileName) {
        InputStream is = null;
        FileOutputStream fos = null;
        int buffsize = 1024;

        try {
            is = this.getAssets().open(assetFileName);
            fos = this.openFileOutput(newFileName, Context.MODE_PRIVATE);
            int byteCount = 0;
            byte[] buffer = new byte[buffsize];
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
