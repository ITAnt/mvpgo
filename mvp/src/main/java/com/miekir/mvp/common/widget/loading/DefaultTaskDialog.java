package com.miekir.mvp.common.widget.loading;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miekir.common.R;

/**
 * @author 詹子聪
 * @date 2021-11-30 19:59
 */
public class DefaultTaskDialog extends TaskDialog {
    private CircleView lv_circle;
    private TextView tv_loading;

    @Override
    public Dialog newLoadingDialog(Context context) {
        // 首先得到整个View
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_mvp_loading_view, null);
        // 获取整个布局
        LinearLayout dialogLayout = (LinearLayout) dialogView.findViewById(R.id.dialog_view);
        // 页面中的LoadingView
        lv_circle = (CircleView) dialogView.findViewById(R.id.lv_circle);
        // 页面中显示文本
        tv_loading = (TextView) dialogView.findViewById(R.id.tv_loading);
        if (!TextUtils.isEmpty(mMessage)) {
            tv_loading.setText(mMessage);
        } else {
            tv_loading.setVisibility(View.GONE);
        }
        Dialog dialog = new Dialog(context, R.style.LoadingDialog);
        dialog.setContentView(dialogLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mDialog = dialog;
        return dialog;
    }

    @Override
    public void onDismiss() {
        if (lv_circle != null) {
            lv_circle.stopAnim();
        }
        lv_circle = null;
    }

    @Override
    public void onShow() {
        if (!TextUtils.isEmpty(mMessage) && tv_loading != null) {
            tv_loading.setText(mMessage);
        }

        if (lv_circle != null) {
            lv_circle.startAnim();
        }
    }
}
