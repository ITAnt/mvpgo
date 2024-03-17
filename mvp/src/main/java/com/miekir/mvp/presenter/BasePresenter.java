package com.miekir.mvp.presenter;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.miekir.mvp.common.widget.loading.TaskDialog;
import com.miekir.mvp.view.base.IView;

import java.lang.ref.WeakReference;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

/**
 * Copyright (C), 2019-2020, Miekir
 *
 * @author Miekir
 * @date 2020/10/7 22:46
 * Description:
 * <p>
 * View (strong)-> Presenter (strong)-> Model (strong)-> Repository
 * View <-(weak) Presenter <-(weak) Model <-(weak) Repository
 * ---------------------------------------------------------------------------------
 * 什么时候用 onSaveInstanceState，什么时候用 ViewModel，什么时候用 Fragment.setRetainInstance?
 * onSaveInstanceState 能够在两种情况下恢复信息：1. 系统配置变化，2. 应用进程被系统回收。由于 onSaveInstanceState 使用 Bundle 来将数据序列化到磁盘，因此我们在使用 onSaveInstanceState 时应当注意：不要存储大量数据，只能存储简单的数据结构及基本类型。因此我们只能存储一些必要的数据，比如用户 ID，当我们重建 Activity 时，应用可以根据恢复的用户 ID 再次去网络请求 或 查询本地数据库，来获取更多信息以恢复页面。
 * ViewModel 保存的数据是在内存中，可以跨越系统配置变化，但是不能在应用进程被系统回收时依然保持数据。ViewModel 可以保持更复杂的数据结构。（似乎最近又出了一个 SavedStateHandle）
 * ViewModel 完全可以替代 Fragment.setRetainInstance。事实上，ViewModel 的内部实现就调用了 Fragment.setRetainInstance。
 * 正确的使用姿势应该是，onSaveInstanceState 和 ViewModel 结合使用。
 * 当系统回收应用进程后，onSaveInstanceState 中的 Bundle 不为空，开发者应当将 Bundle 传给 ViewModel。ViewModel 发现自己缓存的数据为空，因此使用 Bundle 中的数据来加载页面。
 * 当应用配置改变而重建 Activity 时，onSaveInstanceState 中的 Bundle 不为空，开发者应当将 Bundle 传给 ViewModel。由于 ViewModel 自己有缓存的数据，因此最后由 ViewModel 自己决定使用缓存的数据还是 Bundle 中的数据。
 * 链接：https://www.jianshu.com/p/60f2ed95b124
 */
public abstract class BasePresenter<V extends IView> extends ViewModel {
    private WeakReference<V> mViewRef;
    private final CopyOnWriteArraySet<TaskDialog> mLoadingDialog = new CopyOnWriteArraySet<>();

    public void init() {}

    public void addLoadingDialog(TaskDialog dialog) {
        if (dialog == null) {
            return;
        }
        mLoadingDialog.add(dialog);
    }

    public void removeLoadingDialog(TaskDialog dialog) {
        if (dialog == null) {
            return;
        }
        mLoadingDialog.remove(dialog);
    }

    /**
     * Activity生命周期重建，如旋转屏幕等，需要重建对话框，防止崩溃
     */
    public void onActivityRecreate() {
        if (mLoadingDialog.isEmpty()) {
            return;
        }
        IView view = getView();
        if (!(view instanceof Activity) && !(view instanceof Fragment)) {
            return;
        }

        if (view instanceof Fragment) {
            for (TaskDialog dialog : mLoadingDialog) {
                dialog.recreate(((Fragment) view).getContext());
            }
        } else {
            for (TaskDialog dialog : mLoadingDialog) {
                dialog.recreate((Context) view);
            }
        }
    }

    /**
     * 安全地执行方法，供Java使用
     *
     * @param consumer 要执行的操作
     */
    private void post(Consumer<? super V> consumer) {
        //Optional.ofNullable(getView()).ifPresent(consumer);
    }

    public V getView() {
        return mViewRef == null ? null : mViewRef.get();
    }

    public void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
        onActivityRecreate();
    }

    @Override
    protected void onCleared() {
        clearView();
        super.onCleared();
    }

    public void clearView() {
        // 界面被销毁时，可见和不可见的任务都要取消
        mLoadingDialog.clear();
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
