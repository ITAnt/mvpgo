package com.miekir.mt.net

import android.text.TextUtils
import com.miekir.mvp.common.exception.ExceptionManager
import com.miekir.mvp.common.exception.ResultException
import com.miekir.mvp.common.tools.ToastTools
import com.miekir.mvp.task.net.IResponse

/**
 * 统一返回封装
 *
 * @author Miekir
 */
class BaseResponse<T> : IResponse {
    /**
     * 返回状态代码
     */
    var code = 0

    /**
     * 返回信息
     */
    var message: String? = null

    var extra: String? = null

    /**
     * 返回的实体数据
     */
    var resultObj: T? = null

    /**
     * 调用一下，防止有些不需要使用到结果的接口不断提交失败，及时发现隐藏的重大错误如登录过期等
     */
    override fun valid(): Boolean {
        if (code != ExceptionManager.getInstance().successCode) {
            if (code == -99) {
                ToastTools.showShort("登录已过期，请重新登录")
                //重新登录 ActivityTools.jump("com.itant.LoginActivity")
            }
            throw ResultException(
                code,
                if (TextUtils.isEmpty(message)) "" else message!!
            )
        }
        return true
    }

    override fun getServerMessage(): String? {
        return message
    }

    override fun getServerCode(): Int {
        return code
    }
}