package com.miekir.mt.ui.simple

import com.miekir.mvp.view.base.IView

/**
 * @date : 2021/4/17 10:48
 * @author : 詹子聪
 *
 */
interface ILazyView : IView {
    fun onLazySuccess(result: String)
}