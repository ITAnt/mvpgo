package com.miekir.mvp.view.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * 基础Fragment，不做屏幕适配
 */
abstract class BasicFragment : Fragment(), IView {
    /**
     * 当前是否可见
     */
    private var isFirstTimeVisible = true

    /**
     * savedInstanceState == null表示Fragment第一次被创建
     */
    protected var savedInstanceState: Bundle? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.savedInstanceState = savedInstanceState
    }

    override fun onResume() {
        super.onResume()
        if (!isVisible) {
            return
        }
        if (isFirstTimeVisible) {
            isFirstTimeVisible = false
            onLazyInit(savedInstanceState)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isFirstTimeVisible = true
    }

    /**
     * 懒加载，当Fragment可见的时候，再去加载数据；
     * 应用初始化会先调用完所有的setUserVisibleHint再调用onViewCreated，然后切换的时候，就只调用setUserVisibleHint了（已过时）；
     * 在Fragment还不可见的时候，会先执行onCreateView和onStart；
     * savedInstanceState == null表示Fragment第一次被创建；
     */
    protected abstract fun onLazyInit(savedInstanceState: Bundle?)
}