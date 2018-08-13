package com.fanneng.android.web.demo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.widget.LinearLayout;

import org.json.JSONObject;


public class JsFragment extends SuperWebX5Fragment {

    public static final JsFragment getInstance(Bundle bundle) {

        JsFragment mJsSuperWebFragment = new JsFragment();
        if (bundle != null) {
            mJsSuperWebFragment.setArguments(bundle);
        }

        return mJsSuperWebFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        LinearLayout mLinearLayout = (LinearLayout) view;
        LayoutInflater.from(getContext()).inflate(R.layout.fragment_js, mLinearLayout, true);
        super.onViewCreated(view, savedInstanceState);


        Log.i("Info", "add android:" + mSuperWebX5);
        if (mSuperWebX5 != null) {
            mSuperWebX5.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface(mSuperWebX5, this.getActivity()));

        }

        view.findViewById(R.id.one).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.two).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.three).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.four).setOnClickListener(mOnClickListener);


    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View v) {


            switch (v.getId()) {

                case R.id.one:
                    mSuperWebX5.getJsEntraceAccess().quickCallJs("callByAndroid");
                    break;

                case R.id.two:
                    mSuperWebX5.getJsEntraceAccess().quickCallJs("callByAndroidParam", "Hello ! SuperWeb");
                    break;

                case R.id.three:
                    mSuperWebX5.getJsEntraceAccess().quickCallJs("callByAndroidMoreParams", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Log.i("Info", "value:" + value);
                        }
                    }, getJson(), "say:", " Hello! SuperWeb");
                    break;
                case R.id.four:
                    mSuperWebX5.getJsEntraceAccess().quickCallJs("callByAndroidInteraction", "你好Js");
                    break;
                default:
                    break;
            }

        }
    };

    private String getJson() {

        String result = "";
        try {

            JSONObject mJSONObject = new JSONObject();
            mJSONObject.put("id", 1);
            mJSONObject.put("name", "SuperWeb");
            mJSONObject.put("age", 18);
            result = mJSONObject.toString();
        } catch (Exception e) {

        }

        return result;
    }


}
