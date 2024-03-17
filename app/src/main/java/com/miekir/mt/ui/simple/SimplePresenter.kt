//package com.miekir.mt.ui.simple
//
//import com.miekir.mvp.common.log.L
//import com.miekir.mvp.common.tools.ThreadTools
//import com.miekir.mvp.common.widget.loading.LoadingType
//import com.miekir.mvp.presenter.BasePresenter
//import com.miekir.mvp.task.launchModelTask
//import io.reactivex.Observable
//import io.reactivex.ObservableEmitter
//import io.reactivex.ObservableOnSubscribe
//import kotlinx.coroutines.delay
//
///**
// * 比如父类有一个泛型V: IView，且定义一个变量view，是V类型的，
// * 子类提供一个方法，获取的是IView的实现类的实例给到view，而view可以使用这个实例的具体方法，是因为子类指定了泛型
// * 是具体类型，如下SimpleActivity替换IView的位置即可
// * class SimplePresenter : BaseKtPresenter<IView>() {
// * 开发过程中，如果该业务为某个Activity/Fragment独有，则泛型直接写具体的Activity/Fragment，如果是多个界面共同
// * 拥有的重复业务，则需要写具体的I...View，然后泛型写这个接口类（如升级等业务）
// */
//class SimplePresenter : BasePresenter<SimpleActivity>() {
//    fun testOnComplete() {
//        Observable.create(object : ObservableOnSubscribe<String> {
//            override fun subscribe(emitter: ObservableEmitter<String>) {
//                emitter.onNext("OK")
//            }
//        }).subscribe({
//            L.d("next")
//        }, {
//            L.d("err")
//        }, {
//            L.d("onComplete")
//        })
//    }
//
//    /**
//     * 测试省略了view接口的简易版MVP
//     */
//    fun testInterface() {
//        launchModelTask({
//            L.d("是否在主线程：" + ThreadTools.isMainThread())
//            delay(1000)
//            "dd"
//        }, onSuccess = {
//            // ① 泛型可以是抽象的IView，不用写接口
//            //view<SimpleActivity>()?.onLoginResult(success, obj, error)
//            // ② 泛型必须是具体的IMainView，必须写接口（如果直接暴力写Activity则不用写）
//            //view?.onLoginResult(success, obj, error)
//            // ③ 泛型可以是抽象的IView，不用写接口，需要实现初始化
//            //mView()?.onLoginResult(success, obj, error)
//            // ④ Presenter的泛型需要指定为具体的实体类，SimpleActivity
//            //mView?.onLoginResult(success, obj, error)
//        })
//    }
//
//    //override fun mView(): SimpleActivity? = initView()
//
//    /*fun submitRegister(
//        email: String,
//        code: String,
//        password: String,
//        callback: (result: TaskResult<User>) -> Unit
//    ) {
//        launchTask({
//            User()
//        }, onResult = { success, result, code, message ->
//            callback.invoke(TaskResult(success, result, code, message))
//        })
//    }*/
//
//    /**
//     * 测试取消任务
//     */
//    fun testCancel() {
//        launchModelTask({
//            delay(10000)
//            "正常结束"
//        }, onSuccess = {
//
//        }, loadingType = LoadingType.INVISIBLE)
//    }
//
//    fun testLazyPresenter() {
//        launchModelTask({
//            "Lazy Presenter OK"
//        }, onSuccess = {
//            view?.onLoginResult(true, it, "")
//        })
//    }
//}