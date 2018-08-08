package com.fanneng.android.web;

import android.Manifest;

/**
 * 权限列表
 */
public class SuperWebX5Permissions {


    public static final String[] CAMERA;
    public static final String[] LOCATION;
    public static final String[] STORAGE;

    static {


        CAMERA = new String[]{
                Manifest.permission.CAMERA};


        LOCATION = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};


        STORAGE = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }


}
