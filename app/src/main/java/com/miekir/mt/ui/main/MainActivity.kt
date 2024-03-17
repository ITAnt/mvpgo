package com.miekir.mt.ui.main

import MainPresenter
import android.Manifest
import android.os.Bundle
import com.miekir.mt.databinding.ActivityMainBinding
import com.miekir.mvp.common.extension.lazy
import com.miekir.mvp.common.log.L
import com.miekir.mvp.common.tools.ToastTools
import com.miekir.mvp.view.base.BasicBindingActivity
import kotlinx.android.synthetic.main.activity_main.btn_test

class MainActivity : BasicBindingActivity<ActivityMainBinding>(), IMainView {

    private val mainPresenter: MainPresenter by lazy()

    override fun onInit(savedInstanceState: Bundle?) {
//        ActivityTools.swipeActivity(this)

        requestPermissionsForResult(arrayListOf(Manifest.permission.CAMERA)) { grant, temp, detail ->
            if (grant) {
                ToastTools.showShort("granted")
            } else {
                ToastTools.showShort("deny")
            }
        }

        btn_test.setOnClickListener {
//            val testIntent = Intent(this, MainActivity::class.java)
//            intent.putExtra("backKey", "back data")
//            setResult(RESULT_OK, intent)
//            finish()

//            mainPresenter.testRxNetResult()
//            mainPresenter.testCoroutineNet()
//            mainPresenter.testRxCommonResult()

//            mainPresenter.testRetry()
            mainPresenter.testNull()
        }
    }

    override fun onMainTaskCallback() {

    }

    /**
     * 如果一切正常，则errorMessage为空
     */
    override fun onLoginResult(
        success: Boolean,
        result: String?,
        code: Int,
        errorMessage: String?) {
        if (!success) {
            ToastTools.showShort(errorMessage)
            return
        }
        L.d("result:$result")
        ToastTools.showShort(errorMessage)
    }

    override fun onBindingInflate() = ActivityMainBinding.inflate(layoutInflater)
}