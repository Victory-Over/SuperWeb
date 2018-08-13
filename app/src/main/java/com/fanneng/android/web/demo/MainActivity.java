package com.fanneng.android.web.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import static com.fanneng.android.web.demo.CommonActivity.TYPE_KEY;

/**
 * describe：
 *
 * @author ：鲁宇峰 on 2018/8/8 13：44
 *         email：luyufengc@enn.cn
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatas();
        initView();

    }

    private void initView() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                return new RecyclerView.ViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.item_function, parent, false)) {
                };

            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                holder.itemView.findViewById(R.id.bt_function).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, CommonActivity.class).putExtra(TYPE_KEY, position));
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

    private List<String> getDatas() {

        if (datas != null && datas.size() > 0) {
            return datas;
        } else {
            datas = new ArrayList<>();
            initDatas();
            return datas;
        }

    }

    private void initDatas() {

        datas.add("文件浏览功能(pdf/ppt/doc/excel/txt)");

        datas.add("文件上传(JS)");

        datas.add("文件下载功能");

        datas.add("Android与JS交互");

        datas.add("自定义交互(进度条、下拉回弹、ToolBar联动)");

        datas.add("待续。。。");
    }

}
