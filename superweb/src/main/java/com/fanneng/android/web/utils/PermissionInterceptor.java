package com.fanneng.android.web.utils;

/**
 * 权限拦截器
 */
public interface PermissionInterceptor {

    boolean intercept(String url, String[] permissions, String action);

}
