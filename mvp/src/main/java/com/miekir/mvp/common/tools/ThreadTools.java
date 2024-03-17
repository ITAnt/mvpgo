package com.miekir.mvp.common.tools;

import android.os.Looper;

public final class ThreadTools {
    private ThreadTools() {}

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
