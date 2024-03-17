package com.miekir.mt.ui

import android.os.Bundle
import com.miekir.mt.databinding.ActivityMainBinding
import com.miekir.mt.ui.simple.ILazyView
import com.miekir.mt.ui.simple.LazyPresenter
import com.miekir.mvp.common.extension.lazy
import com.miekir.mvp.view.base.BasicBindingFragment

/**
 * @date : 2021/4/17 12:42
 * @author : 詹子聪
 *
 */
class SimpleFragment : BasicBindingFragment<ActivityMainBinding>(), ILazyView {
    private val presenter by lazy<ILazyView, LazyPresenter>()

    override fun onBindingInflate(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onLazyInit(savedInstanceState: Bundle?) {
        binding.btnTest.text = "I am Fragment"

        /*(activity as? BasicActivity)?.requestPermissionsForResult(android.Manifest.permission.ACCESS_COARSE_LOCATION) { granted, temp, detail ->
            L.e("grant: ${granted}")
        }

        (activity as? BasicActivity)?.requestPermissionsForResult(android.Manifest.permission.CAMERA) { granted, temp, detail ->
            L.e("grant: ${granted}")
        }*/
    }

    override fun onLazySuccess(result: String) {

    }
}