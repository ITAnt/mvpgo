package com.miekir.mt.ui.simple

import com.miekir.mvp.presenter.BasePresenter
import com.miekir.mvp.task.launchModelTask

/**
 * 比如父类有一个泛型V: IView，且定义一个变量view，是V类型的，
 * 子类提供一个方法，获取的是IView的实现类的实例给到view，而view可以使用这个实例的具体方法，是因为子类指定了泛型
 * 是具体类型，如下SimpleActivity替换IView的位置即可
 * class SimplePresenter : BaseKtPresenter<IView>() {
 * 开发过程中，如果该业务为某个Activity/Fragment独有，则泛型直接写具体的Activity/Fragment，如果是多个界面共同
 * 拥有的重复业务，则需要写具体的I...View，然后泛型写这个接口类（如升级等业务）
 */
class LazyPresenter : BasePresenter<ILazyView>() {
    fun testLazyPresenter() {
        launchModelTask({
            "Lazy Presenter OK"
        }, onSuccess = {
            it?.run {
                view?.onLazySuccess(this)
            }
        })
    }
}