import com.miekir.mt.ui.main.MainActivity
import com.miekir.mvp.common.log.L
import com.miekir.mvp.presenter.BasePresenter
import com.miekir.mvp.task.launchModelTask

//package com.miekir.mt.ui.main
//
////import com.github.michaelbull.retry.policy.constantDelay
////import com.github.michaelbull.retry.policy.limitAttempts
////import com.github.michaelbull.retry.policy.plus
////import com.github.michaelbull.retry.retry
//import com.itant.mvp.observer.CommonObserver
//import com.itant.mvp.task.RxTask
//import com.miekir.mt.net.ApiHelper.defaultNet
//import com.miekir.mt.net.ApiHelper.defaultNetRx
//import com.miekir.mt.net.BaseResponse
//import com.miekir.mvp.common.extension.lifeScope
//import com.miekir.mvp.common.log.L
//import com.miekir.mvp.common.tools.ThreadTools
//import com.miekir.mvp.common.tools.ToastTools
//import com.miekir.mvp.common.widget.loading.LoadingType
//import com.miekir.mvp.task.launchModelTask
//import com.miekir.mvp.presenter.BasePresenter
//import com.itant.mvp.observer.listener.CancelListener
//import com.itant.mvp.observer.listener.ResultListener
//import com.itant.mvp.observer.listener.StartListener
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class MainPresenter : BasePresenter<IMainView>() {
//    /**
//     * 异步处理有网络任务，需要处理返回值，有加载框
//     */
//    fun testRxNetResult() {
//        /*val observer = NetObserver.Builder<String>(this)
//            .onResult { success, result, code, message ->
//                view?.onLoginResult(success, result, code, message)
//            }.create()*/
//        val observer = CommonObserver.Builder<BaseResponse<String>>(this)
//            .onResult(ResultListener { success, result, code, message ->
//                view?.onLoginResult(success, result.resultObj, code, message)
//            }).create()
//        RxTask.launchRxTask(defaultNetRx.testNetBean(), observer)
//    }
//
//    /**
//     * 异步处理任务，不需要处理返回值，默认没有加载框
//     */
//    fun testRxIgnoreResult() {
//        RxTask.launchRxTask(this, defaultNetRx.testNetBean())
//    }
//
//    /**
//     * 测试普通实体
//     */
//    fun testRxCommonResult() {
//        // 直接把回调函数传过去
//        val observer = CommonObserver.Builder<String>(this)
//            .message("Test Loading")
//            .loadType(LoadingType.VISIBLE)
//            .onResult(ResultListener { success, result, code, message ->
//                view?.onLoginResult(success, result, code, message)
//            })
//            .onStart(StartListener {
//
//            })
//            .onCancel(CancelListener {
//                ToastTools.showShort("取消啦")
//            })
//            .create()
//
//
//
////        launchKtRxTask<String> {
////            try {
////                Thread.sleep(20000)
////            } catch (e: Exception) {
////            }
////            "demo complete"
////        }.startForResult(observer)
//
////        Observable.create<String> { emitter ->
////            try {
////                Thread.sleep(20000)
////            } catch (e: Exception) {
////
////            }
////            emitter.onNext("demo")
////            emitter.onComplete()
////        }.startForResult(observer)
//    }
//
//    fun testCoroutine() {
//        lifeScope?.launch(Dispatchers.Main) {
//            println("main 挂起")
//            withContext(Dispatchers.IO) {
//                println("launchTask1 耗时开始")
//                delay(10000)
//                println("launchTask1 耗时完成")
//            }
//            println("main 继续")
//            view?.onLoginResult(true, "协程归来", 0, "")
//        }
//    }
//
//    fun testCoroutineTask() {
//        launchModelTask({
//            L.d("是否在主线程：" + ThreadTools.isMainThread())
//            delay(10000)
//            "dd"
//        })
//    }
//
//    fun testCoroutineNet() {
////        launchTask({
////            getNet().test().content
////        }, onResult = { success, obj, error ->
////            view?.onLoginResult(success, obj, error)
////        })
//
//        launchModelTask(
//            {
//                defaultNet.test().resultObj
//            }, onSuccess = {
//
//            }
//        )
//    }
//
////    fun testRetry() {
////        launchTask({
////            var message:String? = ""
////            retry(limitAttempts(5) + constantDelay(delayMillis = 3000L)) {
////                throw TaskException(
////                    Code.FAILURE,
////                    "retry failed"
////                )
////            }
////            message
////        })
////    }
//
//}

class MainPresenter : BasePresenter<MainActivity>() {
    fun testNull() {
        launchModelTask(
            {
                getString()
            }, onSuccess = {
                L.e("result: ${it}")
            }, onFailure = { code, message ->
                L.e("code:${code}, message:${message}")
            }
        )
    }

    private suspend fun getString():String? {
        return null
    }
}