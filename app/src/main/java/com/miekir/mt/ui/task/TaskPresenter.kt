package com.miekir.mt.ui.task

import com.miekir.mvp.common.log.L
import com.miekir.mvp.common.widget.loading.LoadingType
import com.miekir.mvp.presenter.BasePresenter
import com.miekir.mvp.task.TaskJob
import com.miekir.mvp.task.launchGlobalTask
import com.miekir.mvp.task.launchModelTask
import kotlinx.coroutines.delay

class TaskPresenter : BasePresenter<TaskActivity>() {

    fun testModelTask() {
        launchModelTask(
            {
                delay(5_000L)
                6/0
            },
            loadingType = LoadingType.VISIBLE,
            onSuccess = {
                L.e("view model success")
            }, onFailure = { code, message ->
                L.e("view model onFailure ${code} ${message}")
            }, onResult = {
                L.e("view model onResults")
            }, onCancel = {
                L.e("view model onCancel")
            }
        )
    }

    fun testGlobalTask(): TaskJob {
        return launchGlobalTask(
            {
                delay(5_000L)
                1/0
            },
            onSuccess = {
                L.e("global success")
            }, onFailure = { code, message ->
                L.e("global onFailure ${code} ${message}")
            }, onResult = {
                L.e("global onResults")
            }, onCancel = {
                L.e("global onCancel")
            }
        )
    }
}