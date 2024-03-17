package com.miekir.mvp.common.exception;


/**
 * @author : 詹子聪
 * 处理异常的类
 * @date : 2021-6-25 21:46
 */
public abstract class AbstractExceptionHandler {
    public abstract ResultException handleException(Throwable t);

    public abstract int getSuccessCode();
    public abstract int getFailedCode();

    /**
     * 配合ResponseInterceptor使用
     * @return 是否需要回调错误
     */
    public boolean needFailCallback() {
        return true;
    }
}
