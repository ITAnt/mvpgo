package com.miekir.mvp.common.exception;

import androidx.annotation.NonNull;

/**
 * @author : 詹子聪
 * @date : 2021/4/7 09:18
 */
public class ResultException extends Exception {
    public static class Code {
        public static final int SUCCESS = 0;
        public static final int COMMON = -1;
        public static final int CANCEL = -2;
        public static final int TIMEOUT = -3;
    }
    public int code;
    public String errorMessage = "";

    public ResultException(int code) {
        this.code = code;
    }

    public ResultException(@NonNull String message) {
        super(message);
        this.code = ExceptionManager.getInstance().getFailedCode();
        this.errorMessage = message;
    }

    public ResultException(int code, @NonNull String message) {
        super(message);
        this.code = code;
        this.errorMessage = message;
    }
}
