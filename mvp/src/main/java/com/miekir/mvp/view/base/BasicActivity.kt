package com.miekir.mvp.view.base

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.EditText
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentOnAttachListener
import androidx.lifecycle.Lifecycle
import com.miekir.mvp.MvpManager
import com.miekir.mvp.common.log.L
import com.miekir.mvp.common.tools.KeyboardTools
import com.miekir.mvp.view.result.ActivityResult
import java.util.concurrent.LinkedBlockingQueue
import kotlin.math.absoluteValue

/**
 * 基础Activity，不做屏幕适配
 */
abstract class BasicActivity : AppCompatActivity(), IView {
    /**
     * 点击空白处是否隐藏软键盘
     */
    private var touchSpaceHideKeyboard = false

    /**
     * 权限申请
     */
    private val mPermissionList = ArrayList<String>()
    private val mPermissionQueue = LinkedBlockingQueue<Runnable>()
    @Volatile
    private var mPermissionCallback: ((granted: Boolean, temp: Boolean, detail : Map<String, Boolean>) -> Unit)? = null
    private val mPermissionLauncher = registerForActivityResult<Array<String>, Map<String, Boolean>>(ActivityResultContracts.RequestMultiplePermissions()) { isGranted: Map<String, Boolean> ->
        // 回调这里是主线程
        var isTemp = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in mPermissionList) {
                isTemp = shouldShowRequestPermissionRationale(permission)
                if (!isTemp) {
                    break
                }
            }
        } else {
            isTemp = false
        }

        if (isGranted.containsValue(false)) {
            mPermissionCallback?.invoke(false, isTemp, isGranted)
        } else {
            mPermissionCallback?.invoke(true, isTemp, isGranted)
        }
        mPermissionCallback = null
        waitingPermissionResult = false

        mPermissionQueue.poll()?.run()
    }

    /**
     * 代替startActivityForResult
     */
    @Volatile
    private var mActivityResultCallback: ActivityResult? = null
    private val mActivityQueue = LinkedBlockingQueue<Runnable>()
    private val mActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback { result ->
        if (mActivityResultCallback == null) {
            return@ActivityResultCallback
        }
        if (result.resultCode == RESULT_OK) {
            mActivityResultCallback?.onResultOK(result.data)
        } else {
            mActivityResultCallback?.onResultFail(result.resultCode)
        }
        mActivityResultCallback?.onResult()

        mActivityResultCallback = null
        waitingActivityResult = false
        mActivityQueue.poll()?.run()
    })

    @Volatile
    private var waitingPermissionResult = false
    @Volatile
    private var waitingActivityResult = false

    private var containsFragments = false
    /**
     * 用于监听界面是否有Fragment
     */
    private val fragmentAttachListener: FragmentOnAttachListener = FragmentOnAttachListener { _, _ ->
        containsFragments = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 系统调节字体大小不影响本APP，必须放到super.onCreate前面
        super.onCreate(savedInstanceState)
        supportFragmentManager.addFragmentOnAttachListener(fragmentAttachListener)
        //进入页面隐藏输入框
        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        handleEnterAnimation()
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        handleEnterAnimation()
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?) {
        super.startActivityForResult(intent, requestCode, options)
        handleEnterAnimation()
    }

    override fun finish() {
        super.finish()
        handleExitAnimation()
    }

    /**
     * 启动其他Activity时，是否使用配置的Activity动画
     */
    open fun enableStartOtherActivityAnim():Boolean {
        return false
    }

    /**
     * 销毁当前Activity时，是否使用配置的Activity动画
     */
    open fun enableDestroyCurrentActivityAnim():Boolean {
        return false
    }

    /**
     * 执行进入的动画
     */
    private fun handleEnterAnimation() {
        if (enableStartOtherActivityAnim()) {
            MvpManager.getInstance().activityAnimation?.enterAnimation(this)
        }
    }

    /**
     * 执行退出的动画
     */
    private fun handleExitAnimation() {
        if (enableDestroyCurrentActivityAnim()) {
            MvpManager.getInstance().activityAnimation?.exitAnimation(this)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!waitingPermissionResult) {
            mPermissionQueue.peek()?.run()
        }

        if (!waitingActivityResult) {
            mActivityQueue.peek()?.run()
        }
    }

    /**
     * 申请权限扩展
     */
    fun requestPermissionsForResult(permissions: MutableList<String>, callback: ((granted: Boolean, temp: Boolean, detail : Map<String, Boolean>) -> Unit)? = null) {
        if (permissions.isEmpty()) {
            callback?.invoke(true, false, hashMapOf())
            return
        }
        // 在主线程发起申请
        if ((lifecycle.currentState == Lifecycle.State.RESUMED || lifecycle.currentState == Lifecycle.State.STARTED) && !waitingPermissionResult) {
            // 界面可见，且没有已弹出的权限申请对话框，则直接申请
            waitingPermissionResult = true
            mPermissionList.clear()
            mPermissionCallback = callback
            try {
                mPermissionList.addAll(LinkedHashSet<String>(permissions))
                mPermissionLauncher.launch(mPermissionList.toTypedArray())
            } catch (e: Exception) {
                L.e(e.toString())
                waitingPermissionResult = false
                mPermissionList.clear()
                mPermissionCallback = null
                mPermissionQueue.poll()?.run()
                // 让调用者决定要不要继续，如果调用者catch了，那就继续，调用者不catch，那就Game Over
                throw e
            }
        } else {
            // 界面不可见，或者有正在申请的权限对话框，则入队等待
            mPermissionQueue.offer(Runnable {
                requestPermissionsForResult(permissions, callback)
            })
        }
    }

    fun openActivityForResult(intent: Intent, result: ActivityResult) {
        // 在主线程发起申请
        if ((lifecycle.currentState == Lifecycle.State.RESUMED || lifecycle.currentState == Lifecycle.State.STARTED) && !waitingActivityResult) {
            waitingActivityResult = true
            mActivityResultCallback = result
            try {
                mActivityResultLauncher.launch(intent)
            } catch (e: Exception) {
                L.e(e.toString())
                waitingActivityResult = false
                mActivityResultCallback = null
                mActivityQueue.poll()?.run()
                throw e
            }
        } else {
            mActivityQueue.offer(Runnable {
                openActivityForResult(intent, result)
            })
        }
    }

    /**
     * 由于屏幕旋转、语言更改、字体大小变化导致Activity重建会，如果界面存在Fragment，可能会出现数据混乱的问题
     * 此方法表示是否允许有Fragment存在的界面保存数据重建（可能造成数据混乱），默认不允许
     * false：禁用Activity重建，即在上述情况发生时，界面存在的Fragment会重新创建，而不是使用旧的；
     * true：保持默认，即允许Activity重建
     */
    open fun enableRecreateWithFragments(): Boolean {
        return false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (enableRecreateWithFragments()) {
            // 无论界面是否包含Fragment都允许保存数据进行重建，可能会数据混乱
            super.onSaveInstanceState(outState)
        } else {
            if (containsFragments) {
                // 界面包含Fragment时不使用旧数据重建
                super.onSaveInstanceState(Bundle())
            } else {
                // 界面不包含Fragment时使用旧数据重建（原生默认）
                super.onSaveInstanceState(outState)
            }
        }
    }

    /**
     * 点击空白处隐藏软键盘，需要在根布局配置
     * android:focusable="true"
     * android:focusableInTouchMode="true"
     * 否则焦点乱飞，比如点击提交按钮，焦点被上一个输入框获取了
     */
    fun enableTouchSpaceHideKeyboard() {
        touchSpaceHideKeyboard = true
    }

    private var lastX: Float = 0.0f
    private var lastY: Float = 0.0f
    private var isMoving: Boolean = false
    private val moveSlop:Int by lazy { ViewConfiguration.get(this).scaledTouchSlop * 2 }
    /**
     * 点击空白隐藏软键盘
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (touchSpaceHideKeyboard) {
            if (ev.action == MotionEvent.ACTION_DOWN) {
                isMoving = false
                lastX = ev.rawX
                lastY = ev.rawY
            } else if (ev.action == MotionEvent.ACTION_UP) {
                isMoving = (lastY-ev.rawY).absoluteValue > moveSlop || (lastX-ev.getRawX()).absoluteValue > moveSlop
            }
            if (ev.action == MotionEvent.ACTION_UP && isShouldHideKeyboard(currentFocus, ev) && !isMoving) {
                KeyboardTools.hideInputMethod(this, window.decorView)
                currentFocus?.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 是否要隐藏软键盘
     */
    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if ((v is EditText)) {
            val l = intArrayOf(0, 0)
            v.getLocationOnScreen(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.rawX > left && event.rawX < right && event.rawY > top && event.rawY < bottom)
        }
        return false
    }

    /**
     * Android 12之后，主界面按返回居然不销毁Activity，哪个家伙拍脑袋想出来的（可能是根Activity才有这个特性）
     */
    /*override fun onBackPressed() {
        finish()
    }*/

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.removeFragmentOnAttachListener(fragmentAttachListener)
        mPermissionQueue.clear()
        mActivityQueue.clear()
    }
}