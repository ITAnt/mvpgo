package com.miekir.mvp.task

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.miekir.mvp.MvpManager
import com.miekir.mvp.common.context.GlobalContext
import com.miekir.mvp.common.exception.ExceptionManager
import com.miekir.mvp.common.exception.ResultException
import com.miekir.mvp.common.widget.loading.DefaultTaskDialog
import com.miekir.mvp.common.widget.loading.LoadingType
import com.miekir.mvp.common.widget.loading.TaskDialog
import com.miekir.mvp.presenter.BasePresenter
import com.miekir.mvp.view.base.IView
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*---------------------------------以下为协程实现耗时任务-------------------------------------*/
/**
 * 协程方式实现耗时任务，有无加载框，有无回调都可以自定义
 * 注意：此回调如使用view::onLoginResult方法传递，会把对Activity的弱引用转为强引用，
 * 需要配合清单文件的configChanges使用，否则任务执行过程中，屏幕旋转后会发生内存泄露
 * @param loadingMessage 任务加载时的提示
 *
 * 可以增加URL参数（或Object类型）以知道是哪个host的请求失败了
 */
inline fun <V : IView, reified T> BasePresenter<V>.launchModelTask(
    noinline taskBody: suspend () -> T?,
    loadingType: LoadingType = LoadingType.INVISIBLE,
    taskDialog: TaskDialog? = null,
    loadingMessage: String = "",
    dialogContext: Context? = null,
    timeoutMillis: Long = -1L,
    noinline onSuccess: ((result: T?) -> Unit)? = null,
    noinline onFailure: ((code: Int, message: String) -> Unit)? = null,
    noinline onCancel: (() -> Unit)? = null,
    noinline onResult: (() -> Unit)? = null,): TaskJob {

    var successCallback: ((result: T?) -> Unit)? = onSuccess
    var failureCallback: ((code: Int, message: String) -> Unit)? = onFailure
    var cancelCallback: (() -> Unit)? = onCancel
    var resultCallback: (() -> Unit)? = onResult

    // 按需弹出加载框
    var mTaskDialog: TaskDialog? = taskDialog
    val presenter = this
    val taskJob = TaskJob()

    // 超时设置
    var timeoutHandler: Handler? = null

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        GlobalContext.runOnUiThread {
            // 取消超时回调
            timeoutHandler?.removeMessages(0)

            // 耗时任务完成后，回到主线程
            removeLoadingDialog(mTaskDialog)
            mTaskDialog?.dismiss()
            mTaskDialog = null

            // 获取具体错误类型
            if (exception is CancelException) {
                // 主动取消
                cancelCallback?.invoke()
            } else {
                // 任务异常
                val eResult = ExceptionManager.getInstance().exceptionHandler.handleException(exception)
                failureCallback?.invoke(eResult.code, eResult.errorMessage)
            }
            resultCallback?.invoke()

            successCallback = null
            failureCallback = null
            cancelCallback = null
            resultCallback = null
        }
    }

    val taskContext = CoroutineName("ViewModel Task") + Dispatchers.Main + coroutineExceptionHandler
    val job = viewModelScope.launch(taskContext) {
        if (timeoutMillis > 0) {
            timeoutHandler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    super.handleMessage(msg)
                    cancel()

                    mTaskDialog?.dismiss()
                    failureCallback?.invoke(ResultException.Code.TIMEOUT, "Timeout")
                    resultCallback?.invoke()

                    successCallback = null
                    failureCallback = null
                    cancelCallback = null
                    resultCallback = null
                }
            }
            timeoutHandler?.sendEmptyMessageDelayed(0, timeoutMillis)
        }

        if (loadingType != LoadingType.INVISIBLE) {
            if (mTaskDialog == null) {
                mTaskDialog = MvpManager.getInstance().taskDialog
            }
            if (mTaskDialog == null) {
                mTaskDialog = DefaultTaskDialog()
            }

            var context:Context? = dialogContext
            if (context == null) {
                context = when (view) {
                    is Context -> view as Context
                    is Fragment -> (view as Fragment).context
                    is View -> (view as View).context
                    else -> null
                }
            }
            if (context == null) {
                throw IllegalArgumentException("Task dialog context is null")
            }

            mTaskDialog?.setupWithJob(context, presenter, null, loadingType, loadingMessage)
            mTaskDialog?.show()
            addLoadingDialog(mTaskDialog)
        }

        var returnType: T? = null
        // 在“子线程”执行耗时任务，try catch取消异常
        withContext(CoroutineName("ViewModel Task") + Dispatchers.IO + coroutineExceptionHandler) {
            returnType = taskBody.invoke()
        }

        // 取消超时回调
        timeoutHandler?.removeMessages(0)

        // 耗时任务完成后，回到主线程
        removeLoadingDialog(mTaskDialog)
        mTaskDialog?.dismiss()
        mTaskDialog = null

        // 调用一下，防止有些不需要使用到结果的接口不断提交失败，及时发现隐藏的重大错误如登录过期等
        if (returnType is ITask) {
            (returnType as ITask).valid()
        }

        successCallback?.invoke(returnType)
        resultCallback?.invoke()

        successCallback = null
        failureCallback = null
        cancelCallback = null
        resultCallback = null
    }

    taskJob.setup(taskContext, job, coroutineExceptionHandler)

    GlobalContext.runOnUiThread {
        mTaskDialog?.setJob(taskJob)
    }
    return taskJob
}

/**
 * 运行任务，任务自动切换主线程、子线程，生命周期与scope一致，默认是全局scope
 */
inline fun <T> launchGlobalTask(
    noinline taskBody: suspend () -> T?,
    noinline onSuccess: ((result: T?) -> Unit)? = null,
    noinline onFailure: ((code: Int, message: String) -> Unit)? = null,
    noinline onCancel: (() -> Unit)? = null,
    noinline onResult: (() -> Unit)? = null,
    timeoutMillis: Long = -1L,
    scope: CoroutineScope = GlobalScope
): TaskJob {
    var successCallback: ((result: T?) -> Unit)? = onSuccess
    var failureCallback: ((code: Int, message: String) -> Unit)? = onFailure
    var cancelCallback: (() -> Unit)? = onCancel
    var resultCallback: (() -> Unit)? = onResult

    val taskJob = TaskJob()
    // 超时设置
    var timeoutHandler: Handler? = null
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        // 协程出现异常，不可继续切协程，只能使用线程调度
        GlobalContext.runOnUiThread {
            // 取消超时回调
            timeoutHandler?.removeMessages(0)
            if (exception is CancelException) {
                cancelCallback?.invoke()
            } else {
                val eResult = ExceptionManager.getInstance().exceptionHandler.handleException(exception)
                failureCallback?.invoke(eResult.code, eResult.errorMessage)
            }
            resultCallback?.invoke()

            successCallback = null
            failureCallback = null
            cancelCallback = null
            resultCallback = null
        }
    }

    val context = CoroutineName("Global Task") + Dispatchers.IO + coroutineExceptionHandler
    val job = scope.launch(context) {

        if (timeoutMillis > 0) {
            timeoutHandler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    super.handleMessage(msg)
                    cancel()

                    failureCallback?.invoke(ResultException.Code.TIMEOUT, "Timeout")
                    resultCallback?.invoke()

                    successCallback = null
                    failureCallback = null
                    cancelCallback = null
                    resultCallback = null
                }
            }
            timeoutHandler?.sendEmptyMessageDelayed(0, timeoutMillis)
        }

        val resultT = taskBody()
        withContext(Dispatchers.Main) {
            // 取消超时回调
            timeoutHandler?.removeMessages(0)
            // 调用一下，防止有些不需要使用到结果的接口不断提交失败，及时发现隐藏的重大错误如登录过期等
            if (resultT is ITask) {
                (resultT as ITask).valid()
            }
            successCallback?.invoke(resultT)
            resultCallback?.invoke()

            successCallback = null
            failureCallback = null
            cancelCallback = null
            resultCallback = null
        }
    }

    taskJob.setup(context, job, coroutineExceptionHandler)

    return taskJob
}