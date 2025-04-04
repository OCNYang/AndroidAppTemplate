package com.app.base

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import androidx.core.content.pm.PackageInfoCompat
import com.tencent.mmkv.MMKV

/**
 * Application 的基类
 *
 * Application 单例获取 [BaseApplication.INSTANCE]
 * Activity 单例获取 [BaseApplication.MAIN_ACTIVITY] 应用采用的 compose-UI 所以全局只有一个 Activity
 */
abstract class BaseApplication : Application() {

    override fun onCreate() {
        INSTANCE = this
        super.onCreate()

        NetworkChangeListener.init(this)
        AppForegroundListener().init()

    }

    companion object {
        @JvmStatic
        val TAG = "[TAG_LOG]"

        @JvmStatic
        lateinit var INSTANCE: BaseApplication

        @JvmStatic
        lateinit var MAIN_ACTIVITY: BaseActivity

        private const val TOKEN_KEY = "user-token-key"

        @JvmStatic
        var TOKEN: String = ""
            set(value) {
                field = value
                MMKV.defaultMMKV().encode(TOKEN_KEY, value)
            }
            get() = if (TextUtils.isEmpty(field)) {
                field = MMKV.defaultMMKV().decodeString(TOKEN_KEY) ?: ""
                field
            } else {
                field
            }

        @JvmStatic
        val DEBUG: Boolean by lazy {
            (INSTANCE.applicationInfo != null && (INSTANCE.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0)
        }

        @JvmStatic
        val TEST: Boolean by lazy {
            DEBUG || (VERSION_INFO.second.contains("test", ignoreCase = true))
        }

        @JvmStatic
        val VERSION_INFO: Triple<Long, String, String> by lazy { // versionCode, versionName, packageName
            val packageManager = INSTANCE.packageManager
            val packageName = INSTANCE.packageName
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                packageManager.getPackageInfo(packageName, 0)
            }
            Triple(
                PackageInfoCompat.getLongVersionCode(packageInfo),
                packageInfo.versionName ?: "0.0.0",
                packageName
            )
        }
    }
}