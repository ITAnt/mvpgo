package com.miekir.mvp.common.exception;

import android.net.ParseException;
import android.os.NetworkOnMainThreadException;
import android.text.TextUtils;

import com.miekir.mvp.common.log.L;
import com.miekir.mvp.common.tools.DevTools;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * 默认的异常信息处理类
 * @author Miekir
 */
final class DefaultExceptionHandler extends AbstractExceptionHandler {

    @Override
    public ResultException handleException(Throwable t) {
        String message = "";
        ResultException result = new ResultException(getFailedCode(), message);
        if (t instanceof ResultException) {
            ResultException taskException = (ResultException) t;
            result.code = taskException.code;
            message = taskException.errorMessage;
        } else if (t instanceof UnknownHostException) {
            message = "网络不可用";
        } else if (t instanceof SocketTimeoutException) {
            message = "请求网络超时";
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            result.code = httpException.code();
            if (TextUtils.isEmpty(message)) {
                message = handleHttpCode(httpException.code(), httpException.message());
            }
        } else if (t instanceof ParseException || t instanceof JSONException) {
            message = "数据解析错误";
        } else if (t instanceof NetworkOnMainThreadException) {
            message = "不允许在主线程访问网络";
        } else {
            message = t.getMessage();
        }
        if (DevTools.isDebug()) {
            t.printStackTrace();
        }
        if (TextUtils.isEmpty(message)) {
            message = "未知错误";
        }
        L.e(t.toString(), "ExceptionTag");
        result.errorMessage = message;
        return result;
    }

    @Override
    public int getSuccessCode() {
        return ResultException.Code.SUCCESS;
    }

    @Override
    public int getFailedCode() {
        return ResultException.Code.COMMON;
    }

    private String handleHttpCode(int code, String message) {
        String msg;
        if (code >= 500 && code < 600) {
            msg = "服务器处理请求出错";
        } else if (code >= 400 && code < 500) {
            msg = "服务器无法处理请求";
        } else if (code >= 300 && code < 400) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = message;
        }
        L.e("服务器错误：" + message);
        return msg;
    }
}

