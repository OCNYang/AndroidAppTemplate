package com.app.webview_x5

import android.content.Context
import android.util.Log
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk


object X5 {
    val TAG = X5::class.java.simpleName

    // 初始化 SDK 环境，在 App 启动后尽可能早地调用初始化接口，进行内核预加载：
    fun init(appContext: Context, openDownload: Boolean = true) {
        dex2oat()
        QbSdk.setDownloadWithoutWifi(openDownload)
        QbSdk.initX5Environment(appContext.applicationContext, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                // 内核初始化完成，可能为系统内核，也可能为系统内核
                Log.e(TAG, "x5 init done.")
            }

            /**
             * 预初始化结束
             * 由于X5内核体积较大，需要依赖网络动态下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
             * @param isX5 是否使用X5内核
             */
            override fun onViewInitFinished(isX5: Boolean) {
                Log.e(TAG, "x5 init with: ${if (isX5) "x5-core" else "system-core"}")
            }
        })
    }

    fun getWebViewCrashInfo(appContext: Context): String {
        return com.tencent.smtt.sdk.WebView.getCrashExtraMessage(appContext.applicationContext)
    }

    private fun dex2oat() {
        // 在调用TBS初始化、创建WebView之前进行如下配置
        val map: HashMap<String, Any> = hashMapOf()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
    }

    fun get(context: Context) = com.tencent.smtt.sdk.WebView(context)
}