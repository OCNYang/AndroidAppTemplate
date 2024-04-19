package com.app.template

import com.app.base.BaseApplication
import com.app.webview_x5.X5

class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        X5.init(this)
    }
}