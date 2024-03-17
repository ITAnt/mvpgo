@file:JvmName("JsonUtils")
package com.miekir.mvp.common.extension

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
/**
 * 普通对象转JSON对象
 */
fun Any.toJsonObject(): JsonObject {
    val gson = Gson()
    return JsonParser.parseString(gson.toJson(this)).asJsonObject
}