package com.miekir.mvp.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * 基础Fragment，不做屏幕适配
 * 注意不要在子类的onDestroy里调用binding了
 */
abstract class BasicBindingFragment<VB : ViewBinding> : BasicFragment() {
    var bindingNullable: VB? = null
    val binding get() = bindingNullable!!

    /**
     * 根布局
     */
    var rootView: View? = null

    /**
     * 布局文件绑定
     */
    abstract fun onBindingInflate(): VB
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindingNullable = onBindingInflate()
        rootView = bindingNullable?.root
        return rootView
    }

    override fun onDestroyView() {
        rootView = null
        bindingNullable = null
        super.onDestroyView()
    }
}