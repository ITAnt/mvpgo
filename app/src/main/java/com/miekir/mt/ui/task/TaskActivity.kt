package com.miekir.mt.ui.task

import android.os.Bundle
import com.miekir.mt.databinding.ActivityTaskBinding
import com.miekir.mvp.common.extension.lazy
import com.miekir.mvp.common.extension.setSingleClick
import com.miekir.mvp.common.log.L
import com.miekir.mvp.view.base.BasicBindingActivity

class TaskActivity : BasicBindingActivity<ActivityTaskBinding>() {
    private val presenter: TaskPresenter by lazy()

    override fun onBindingInflate() = ActivityTaskBinding.inflate(layoutInflater)

    override fun onInit(savedInstanceState: Bundle?) {
        binding.btnTask.setSingleClick {
            L.e("task")
        }

        binding.btnTest.setSingleClick {
            L.e("test")
        }

        /*requestPermissionsForResult(android.Manifest.permission.ACCESS_COARSE_LOCATION) { granted, temp, detail ->
            L.e("grant ACCESS_COARSE_LOCATION: ${granted}")
        }

        requestPermissionsForResult(android.Manifest.permission.CAMERA) { granted, temp, detail ->
            L.e("grant CAMERA: ${granted}")
        }

        requestPermissionsForResult(android.Manifest.permission.RECORD_AUDIO) { granted, temp, detail ->
            L.e("grant RECORD_AUDIO: ${granted}")
        }*/

        L.e("aaaaaa onCreate $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        L.e("aaaaaa onDestroy $this")
    }

    override fun enableRecreateWithFragments(): Boolean {
        return false
    }
}