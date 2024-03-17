package com.miekir.mt.ui.main

import com.miekir.mvp.view.base.IView


interface IMainView : IView {
    fun onMainTaskCallback()
    fun onLoginResult(success: Boolean, result: String?, code: Int, errorMessage: String?)
}