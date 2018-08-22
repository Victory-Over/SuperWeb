package com.fanneng.android.web.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fanneng.android.web.NestedScrollSuperWebView;
import com.fanneng.android.web.SuperWebX5;
import com.fanneng.android.web.demo.interactive.CoolIndicatorLayout;
import com.fanneng.android.web.demo.interactive.SlidingLayout;
import com.fanneng.android.web.demo.interactive.WebLayout;
import com.fanneng.android.web.progress.BaseIndicatorView;

/**
 * describe：与Toolbar联动  需设置主题为AppTheme
 *
 * @author ：鲁宇峰 on 2018/8/21 15：56
 *         email：luyufengc@enn.cn
 */
public class ToolbarActivity extends AppCompatActivity implements View.OnClickListener {

    private SuperWebX5 mSuperWebX5;
    private CoordinatorLayout main;
    private Toolbar toolbar;
    private WebLayout webLayout;
    /**
     * 后退
     */
    private TextView btnBack;
    /**
     * 前进
     */
    private TextView btnForward;
    /**
     * 刷新
     */
    private TextView btnRefresh;
    /**
     * 菜单
     */
    private TextView btnMenu;

    public static void show(Context context) {
        Intent intent = new Intent(context, ToolbarActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_toolbar);

        initView();

        setSupportActionBar(toolbar);

        NestedScrollSuperWebView webView = new NestedScrollSuperWebView(this);

        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(-1, -1);
        lp.setBehavior(new AppBarLayout.ScrollingViewBehavior());

        mSuperWebX5 = SuperWebX5.with(this)
                //lp记得设置behavior属性
                .setSuperWebParent(main, lp, 1)
                .customProgress(getIndicatorView())
                .setWebView(webView)
                .setWebLayout(getWebLayout())
                .createSuperWeb()
                .ready()
                .go("http://m.jd.com/");

    }

    private void initView() {
        main = findViewById(R.id.main);
        toolbar = findViewById(R.id.toolbar);
        btnBack = findViewById(R.id.btn_back);
        btnForward = findViewById(R.id.btn_forward);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnMenu = findViewById(R.id.btn_menu);
        btnBack.setOnClickListener(this);
        btnForward.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                if (mSuperWebX5.getWebCreator().get().canGoBack()) {
                    mSuperWebX5.back();
                } else {
                    Toast.makeText(this, "无法后退", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_forward:
                if (mSuperWebX5.getWebCreator().get().canGoForward()) {
                    mSuperWebX5.getWebCreator().get().goForward();
                } else {
                    Toast.makeText(this, "无法前进", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_refresh:
                mSuperWebX5.getWebCreator().get().reload();
                break;
            default:
                Toast.makeText(this, "这是菜单选项", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mSuperWebX5.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public BaseIndicatorView getIndicatorView() {
        return new CoolIndicatorLayout(this);
    }

    public WebLayout getWebLayout() {
        webLayout = new WebLayout(this);
        SlidingLayout slidingLayout = (SlidingLayout) webLayout.getLayout();
        TextView tv = slidingLayout.getBackgroundView().findViewById(R.id.tv_sliding);
        tv.setText("网页由 http://m.jd.com/ 提供");
        return webLayout;
    }
}
