package com.miekir.mvp.common.widget.loading;

import android.app.Dialog;
import android.content.Context;

import com.miekir.mvp.common.context.GlobalContext;
import com.miekir.mvp.presenter.BasePresenter;
import com.miekir.mvp.task.TaskJob;

/**
 * @author 詹子聪
 * @date 2021-11-30 19:47
 */
public abstract class TaskDialog {
    private BasePresenter mPresenter;
    private TaskJob mJob;
    private LoadingType mLoadingType;
    protected Dialog mDialog;
    protected String mMessage;

    /**
     * @return 对话框实例
     */
    public abstract Dialog newLoadingDialog(Context context);

    /**
     * 对话框消失时，不用再主动调用dialog的dismiss()方法
     */
    public abstract void onDismiss();

    /**
     * 对话框显示时，不用再主动调用dialog的show()方法
     */
    public abstract void onShow();

    /**
     * 设置当前加载框对应的job
     * @param job 任务
     */
    public void setJob(TaskJob job) {
        this.mJob = job;
    }

    /**
     * @param loadingType 对话框类型（任务），是否可手动取消
     * @param message     对话框的消息
     */
    public void setupWithJob(
            Context context,
            final BasePresenter presenter,
            final TaskJob job,
            final LoadingType loadingType,
            final String message
    ) {
        // 如果是不可见任务，则不需要弹出对话框，任务的生命周期不与对话框绑定
        if (loadingType == LoadingType.INVISIBLE) {
            return;
        }
        mPresenter = presenter;
        createDialog(context, job, loadingType, message);
    }

    /**
     * Activity旋转，需要重新创建，否则会一直持有旧Activity的引用，导致内存泄露，如果一段时间后调用dismiss，会闪退，所以应该发现旋转时马上dismiss
     */
    public void recreate(Context context) {
        dismiss();
        createDialog(context, mJob, mLoadingType, mMessage);
        show();
    }

    /**
     * 确保在主线程初始化对话框
     */
    private void createDialog(Context context, TaskJob job, LoadingType loadingType, String message) {
        // 如果是不可见任务，则不需要弹出对话框，任务的生命周期不与对话框绑定
        if (loadingType == LoadingType.INVISIBLE) {
            return;
        }

        mLoadingType = loadingType;
        mMessage = message;

        // 创建自定义样式的Dialog
        mDialog = newLoadingDialog(context);
        mDialog.setCanceledOnTouchOutside(false);
        if (loadingType == LoadingType.STICKY) {
            // 任务不能从对话框取消，不用持有任务的引用
            mDialog.setCancelable(false);
        } else {
            mDialog.setCancelable(true);
            if (loadingType == LoadingType.VISIBLE) {
                // 任务可以从对话框取消，需要持有任务的引用
                mJob = job;
                mDialog.setOnCancelListener(dialog -> close());
            }
        }
    }

    /**
     * 显示对话框
     */
    public void show() {
        if (mDialog == null) {
            return;
        }

        try {
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
            onShow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭对话框
     */
    public void close() {
        Runnable runnable = () -> {
            dismiss();
            if (mPresenter != null) {
                mPresenter.removeLoadingDialog(TaskDialog.this);
            }
            mPresenter = null;

            if (mJob != null) {
                mJob.cancel();
            }
            mJob = null;
        };
        GlobalContext.runOnUiThread(runnable);
    }

    /**
     * 仅仅关闭对话框，不销毁任务进程
     */
    public void dismiss() {
        try {
            onDismiss();
            if (mDialog != null) {
                mDialog.setOnCancelListener(null);
                mDialog.dismiss();
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        mDialog = null;
    }
}
