package com.miekir.mt.ui

import android.content.Intent
import android.os.Bundle
import com.miekir.mt.databinding.ActivityMainBinding
import com.miekir.mvp.common.extension.openActivity
import com.miekir.mvp.common.extension.setSingleClick
import com.miekir.mvp.view.base.BasicBindingActivity

class SplashActivity : BasicBindingActivity<ActivityMainBinding>() {
    override fun onBindingInflate() = ActivityMainBinding.inflate(layoutInflater)

    override fun onInit(savedInstanceState: Bundle?) {
        val transitionIntent = Intent(this, SplashActivity::class.java)
        binding.btnTest.setSingleClick {
            //startActivity(transitionIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            openActivity<SplashActivity>()
            //finish()
        }

//        startActivity(Intent(this, JavaActivity::class.java))
        //startActivity(Intent(this, FragmentTestActivity::class.java))
    }
}