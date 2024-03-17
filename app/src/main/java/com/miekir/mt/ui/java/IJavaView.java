package com.miekir.mt.ui.java;

import com.miekir.mvp.view.base.IView;

import kotlin.Unit;

/**
 * @author : 詹子聪
 * @date : 2021-7-9 22:02
 */
public interface IJavaView extends IView {
    void onLoginResult();

    Unit onLoginResult(Boolean aBoolean, String s, Integer integer, String s1);
}
