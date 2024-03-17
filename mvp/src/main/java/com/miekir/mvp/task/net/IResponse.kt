package com.miekir.mvp.task.net

import com.miekir.mvp.task.ITask

/**
 * 网络返回的实体，基础类
 */
interface IResponse: ITask {
    fun getServerMessage(): String?
    fun getServerCode(): Int
}