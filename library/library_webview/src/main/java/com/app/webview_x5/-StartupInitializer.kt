package com.app.webview_x5

import android.content.Context
import androidx.startup.Initializer


/**
 * App StartUp WebView X5 初始化
 *
 * 初始化是在 `AndroidManifest.xml` 中
 */
class X5Initializer : Initializer<X5> {
    override fun create(context: Context): X5 {
        X5.init(context)
        return X5
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}