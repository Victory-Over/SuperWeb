package com.fanneng.android.web.progress;


import com.tencent.smtt.sdk.WebView;

/**
 * 进度条实现类
 */
public class IndicatorHandler implements IndicatorController, ProgressLifeCyclic {
    BaseProgressSpec baseProgressSpec;

    @Override
    public void progress(WebView v, int newProgress) {

        if (newProgress == 0) {
            reset();
        } else if (newProgress > 0 && newProgress <= 10) {
            showProgressBar();
        } else if (newProgress > 10 && newProgress < 95) {
            setProgressBar(newProgress);
        } else {
            setProgressBar(newProgress);
            finish();
        }

    }

    @Override
    public BaseProgressSpec offerIndicator() {
        return this.baseProgressSpec;
    }

    public void reset() {

        if (baseProgressSpec != null) {
            baseProgressSpec.reset();
        }
    }

    @Override
    public void finish() {
        if (baseProgressSpec != null) {
            baseProgressSpec.hide();
        }
    }

    @Override
    public void setProgressBar(int n) {
        if (baseProgressSpec != null) {
            baseProgressSpec.setProgress(n);
        }
    }

    @Override
    public void showProgressBar() {

        if (baseProgressSpec != null) {
            baseProgressSpec.show();
        }
    }

    public static IndicatorHandler getInstance() {
        return new IndicatorHandler();
    }


    public IndicatorHandler inJectProgressView(BaseProgressSpec baseProgressSpec) {
        this.baseProgressSpec = baseProgressSpec;
        return this;
    }
}
