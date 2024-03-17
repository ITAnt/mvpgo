package com.miekir.mvp;

import androidx.annotation.NonNull;

import com.miekir.mvp.common.exception.ExceptionManager;
import com.miekir.mvp.common.exception.AbstractExceptionHandler;
import com.miekir.mvp.common.log.ILogHandler;
import com.miekir.mvp.common.log.L;
import com.miekir.mvp.common.log.LogCallback;
import com.miekir.mvp.common.widget.loading.TaskDialog;
import com.miekir.mvp.view.anim.AbstractAnimHandler;

/**
 * @author : 詹子聪
 * 任务初始化
 * @date : 2021-6-25 22:30
 */
public class MvpManager {
    private AbstractAnimHandler mAnimHandler;

    private MvpManager() {}

    private static class Factory {
        public static final MvpManager INSTANCE = new MvpManager();
    }

    public static MvpManager getInstance() {
        return Factory.INSTANCE;
    }

    /**
     * @return 获取Activity切换动画
     */
    public AbstractAnimHandler getActivityAnimation() {
        return mAnimHandler;
    }

    /**
     * 加载对话框
     */
    private Class<? extends TaskDialog> mTaskDialogClass;

    public TaskDialog getTaskDialog() {
        if (mTaskDialogClass != null) {
            try {
                return mTaskDialogClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 设置任务出错时的处理器
     *
     * @param handler 出错处理器
     * @return TaskManager
     */
    public MvpManager exceptionHandler(@NonNull AbstractExceptionHandler handler) {
        ExceptionManager.getInstance().setExceptionHandler(handler);
        return this;
    }

    /**
     * 加载对话框的样式
     * @return TaskManager
     */
    public MvpManager globalTaskDialog(Class<? extends TaskDialog> taskDialogClass) {
        mTaskDialogClass = taskDialogClass;
        return this;
    }

    /**
     * 设置日志
     *
     * @param handler 日志处理器
     * @return TaskManager
     */
    public MvpManager logHandler(@NonNull ILogHandler handler) {
        L.setLogHandler(handler);
        return this;
    }

    /**
     * 设置日志打印回调
     *
     * @param callback 日志打印回调
     * @return TaskManager
     */
    public MvpManager logCallback(@NonNull LogCallback callback) {
        L.setLogCallback(callback);
        return this;
    }

    /**
     * 设置Activity切换动画
     * @param handler 动画实现
     * @return TaskManager
     */
    public MvpManager activityAnimation(AbstractAnimHandler handler) {
        this.mAnimHandler = handler;
        return this;
    }
}
