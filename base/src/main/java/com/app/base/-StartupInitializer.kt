package com.app.base

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.app.webview_x5.X5
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.PrettyFormatStrategy
import com.tencent.mmkv.MMKV

class LoggerInitializer : Initializer<Any> {
    override fun create(context: Context): Any {
        LogX.addLogAdapter(object : AndroidLogAdapter(PrettyFormatStrategy.newBuilder().tag(BaseApplication.TAG).build()) {
            override fun isLoggable(priority: Int, tag: String?) = true // BuildConfig.DEBUG
        })
        Log.i(BaseApplication.TAG, "Logger init done.")
        return Any()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}

class MMKVInitializer : Initializer<MMKV> {
    override fun create(context: Context): MMKV {
        val mmkvRootDir = MMKV.initialize(context)
        Log.i(BaseApplication.TAG, "MMKV init, root dir: $mmkvRootDir")
        return MMKV.defaultMMKV()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}

class X5Initializer : Initializer<X5> {
    override fun create(context: Context): X5 {
        X5.init(context)
        return X5
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}