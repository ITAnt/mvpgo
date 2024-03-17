package com.miekir.mvp.common.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import androidx.annotation.Keep
import androidx.preference.PreferenceManager
import com.miekir.mvp.common.context.GlobalContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * SharedPreference封装
 * @param name key
 * @param default 默认值
 * @param preferenceName 配置文件名
 */
@Keep
class SP<T>(val name: String, private val default: T, private val preferenceName:String? = null) : ReadWriteProperty<Any?, T> {

    private val mPreference: SharedPreferences by lazy {
        if (TextUtils.isEmpty(preferenceName)) {
            PreferenceManager.getDefaultSharedPreferences(GlobalContext.getContext())
        } else {
            GlobalContext.getContext().getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getValue(name, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putValue(name, value)
    }

    @SuppressLint("CommitPrefEdits")
    private fun putValue(name: String, value: T) = with(mPreference.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> putString(name, serialize(value))
        }.commit()
    }

    fun getValue(name: String, default: T): T = with(mPreference) {
        val res = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> {
                val stringValue: String? = getString(name, "")
                if (TextUtils.isEmpty(stringValue)) {
                    null
                } else {
                    try {
                        deSerialization<T>(stringValue!!)
                    } catch (e: Exception) {
                        default
                    }
                }
            }
        } ?: return@with default
        return try {
            res as T
        } catch (e: Exception) {
            default
        }
    }

    /**
     * 删除全部数据
     */
    fun clearPreference() {
        mPreference.edit().clear().commit()
    }

    /**
     * 根据key删除存储数据
     */
    fun clearPreference(key: String) {
        mPreference.edit().remove(key).commit()
    }

    /**
     * 序列化对象
     * @return
     */
    private fun <A> serialize(obj: A): String? {
        var serStr: String?

        ByteArrayOutputStream().use { byteArrayOutputStream ->
            ObjectOutputStream(byteArrayOutputStream).use { objectOutputStream ->
                objectOutputStream.writeObject(obj)
                serStr = byteArrayOutputStream.toString("ISO-8859-1")
                serStr = java.net.URLEncoder.encode(serStr, "UTF-8")
            }
        }

        return serStr
    }

    /**
     * 反序列化对象
     * @param str
     */
    @Throws(Exception::class)
    private fun <A> deSerialization(str: String): A {
        val redStr = java.net.URLDecoder.decode(str, "UTF-8")
        val byteArrayInputStream = ByteArrayInputStream(
            redStr.toByteArray(charset("ISO-8859-1"))
        )
        val objectInputStream = ObjectInputStream(
            byteArrayInputStream
        )
        val obj = objectInputStream.readObject() as A
        objectInputStream.close()
        byteArrayInputStream.close()
        return obj
    }


    /**
     * 查询某个key是否已经存在
     * @param key
     * @return
     */
    fun contains(key: String): Boolean {
        return mPreference.contains(key)
    }

    /**
     * @return 返回所有的键值对
     */
    fun getAll(): Map<String, *> {
        return mPreference.all
    }
}