package com.miekir.mt.ui.java;

import android.os.Bundle;

import com.miekir.mt.databinding.ActivityMainBinding;
import com.miekir.mvp.common.tools.ToastTools;
import com.miekir.mvp.view.base.BasicBindingActivity;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;

/**
 * @author : 詹子聪
 * @date : 2021-7-9 21:55
 */
public class JavaActivity extends BasicBindingActivity<ActivityMainBinding> implements IJavaView {
    private JavaPresenter javaPresenter;

    @NotNull
    @Override
    public ActivityMainBinding onBindingInflate() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onInit(Bundle savedInstanceState) {
//        javaPresenter = PresenterFactory.create(this, JavaPresenter.class);
//        binding.btnTest.setOnClickListener(v -> {
//            javaPresenter.goLogin();
//        });
    }

    @Override
    public void onLoginResult() {
        ToastTools.showShort("login result");
    }

    @Override
    public Unit onLoginResult(Boolean aBoolean, String s, Integer integer, String s1) {
        return null;
    }
}
