package com.fanneng.android.web.net

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import rx.Observable

/**
 * 项目 ： SuperWeb
 * 作者 ： Chuckifan
 * 时间 ： 2019/1/8 17:36
 * 内容 ：
 */
interface DownloadApi{

    @GET
    @Streaming
    fun download(@Url url:String): Observable<ResponseBody>
}