package com.miekir.mt.ui.fragment

import android.os.Bundle
import com.miekir.mt.databinding.ActivityFragmentBinding
import com.miekir.mt.ui.SimpleFragment
import com.miekir.mvp.view.base.BasicBindingActivity

class FragmentTestActivity : BasicBindingActivity<ActivityFragmentBinding>() {


    override fun onInit(savedInstanceState: Bundle?) {
        supportFragmentManager.beginTransaction()
            .add(binding.fragmentTest.id, SimpleFragment())
            .commit()
    }

    override fun onBindingInflate() = ActivityFragmentBinding.inflate(layoutInflater)
}