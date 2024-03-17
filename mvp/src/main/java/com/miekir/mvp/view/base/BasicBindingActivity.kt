package com.miekir.mvp.view.base

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.viewbinding.ViewBinding
import com.miekir.mvp.common.tools.KeyboardTools

/**
 * 基础Activity，不做屏幕适配
 *
 * ViewBinding支持不同布局里面所有ID相同的横竖屏界面，随着屏幕旋转会自动切换不同xml，前提是手机打开自动旋转，清单文件
 * 不要写死方向，configChanges不能有orientation
 * 如果可能要切换渲染的xml非常多
 * 方案一：inflate不同的xml + findViewById大法，适用于界面的id全相同，仅仅是布局样式不一样；
 * 方案二：activity提供rootView，rootView add/remove对应的V自定义布局，在自定义的布局类里使用ViewBinding，
 * 只需要把数据实体传进去即可，适用于不同情况展示不同内容和布局的activity
 */
abstract class BasicBindingActivity<VB : ViewBinding> : BasicActivity() {
    var rootView: View? = null
    lateinit var binding: VB

    /**
     * 布局文件绑定
     */
    abstract fun onBindingInflate(): VB

    /**
     * 视图初始化
     * @param savedInstanceState == null表示Activity第一次创建
     */
    abstract fun onInit(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 默认全竖屏（不要在代码设置方向，可能会在8.0闪退）
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = onBindingInflate()
        rootView = binding.root
        setContentView(rootView)
        onInit(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        rootView = null
    }

    /*override fun onPause() {
        // 必须要在onPause隐藏键盘，在onDestroy就太晚了
        ViewTools.hideInputMethod(this, rootView)
        super.onPause()
    }*/

//    /**
//     * ViewBinding延迟加载，使用：val vb: ActivityMainBinding = bindView {ActivityMainBinding.inflate(layoutInflater)}.value
//     */
//    inline fun <reified B : ViewBinding> bindView(crossinline bind: ((View) -> B)) = object : Lazy<B> {
//        private var vb: B? = null
//
//        override val value: B
//            // 如果vb不为空，则返回，否则执行bind方法，传进一个View参数，返回bind的返回值(即ViewBinding实例)
//            get() = vb ?: bind(findViewById<ViewGroup>(android.R.id.content).getChildAt(0)).also {
//                vb = it
//            }
//
//        override fun isInitialized() = vb != null
//    }
}