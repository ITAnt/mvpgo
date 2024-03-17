package com.miekir.mt.net.coroutine

import com.miekir.mt.net.BaseResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming

/**

 */
/**
 * @date : 2021/4/5 01:14
 * @author : 詹子聪
 *  请求接口，统一规则，BaseUrl以/结尾，具体请求不能以/开头
 * 注意：函数必须是suspend的
 */
interface ApiService {
    @GET("test")
    //suspend fun test(): Any
    suspend fun test(): BaseResponse<String>

    @Streaming
    @GET("upgrade.apk")
    suspend fun getFile(): Call<ResponseBody>
}