package com.miekir.mvp.task

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class TaskJob {
    private var context: CoroutineContext? = null
    private var job: Job? = null
    private var exceptionHandler: CoroutineExceptionHandler? = null

    fun setup(context: CoroutineContext, job: Job?, exceptionHandler: CoroutineExceptionHandler) {
        this.context = context
        this.job = job
        this.exceptionHandler = exceptionHandler
    }

    fun cancel() {
        job?.run {
            if (isActive) {
                cancel()
                exceptionHandler?.handleException(context!!, CancelException())
            }
        }
        context = null
        job = null
        exceptionHandler = null
    }
}