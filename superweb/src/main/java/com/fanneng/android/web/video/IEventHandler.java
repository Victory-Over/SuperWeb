package com.fanneng.android.web.video;

import android.view.KeyEvent;


public interface IEventHandler {

    boolean onKeyDown(int keyCode, KeyEvent event);


    boolean back();
}
