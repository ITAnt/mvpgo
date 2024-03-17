//package com.miekir.mt.net
//
//import com.itant.mvp.task.net.RetrofitManager
//import com.miekir.mt.BuildConfig
//import com.miekir.mt.net.coroutine.ApiService
//import com.miekir.mt.net.rx.ApiServiceRx
//
///**
// * @date : 2021-6-23 23:00
// * @author : 詹子聪
// *
// */
//object ApiHelper {
//    val defaultNet: ApiService by lazy {
//        RetrofitManager.getDefault().createApiService(BuildConfig.BASE_URL, ApiService::class.java)
//    }
//
//    val defaultNetRx: ApiServiceRx by lazy {
//        RetrofitManager.getDefault().createApiService(BuildConfig.BASE_URL, ApiServiceRx::class.java)
//    }
//
//}