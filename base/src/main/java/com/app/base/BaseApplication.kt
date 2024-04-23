package com.app.base

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import androidx.core.content.pm.PackageInfoCompat
import androidx.multidex.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.PrettyFormatStrategy
import com.tencent.mmkv.MMKV

abstract class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        initMMKV()
        initLogLib()

        NetworkChangeListener.init(this)
        AppForegroundListener().init()
    }

    private fun initMMKV() {
        val rootDir = MMKV.initialize(this)
        Log.e("MMKV-dir: $rootDir")
    }


    private fun initLogLib() {
        LogX.addLogAdapter(object : AndroidLogAdapter(PrettyFormatStrategy.newBuilder().tag("e-bike").build()) {
            override fun isLoggable(priority: Int, tag: String?) = BuildConfig.DEBUG
        })
    }

    companion object {

        @JvmStatic
        lateinit var instance: BaseApplication

        @JvmStatic
        fun get(): BaseApplication = instance

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
            (instance.applicationInfo != null && (instance.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0)
        }

        @JvmStatic
        val TEST: Boolean by lazy {
            DEBUG || (VERSION_INFO.second.contains("test", ignoreCase = true))
        }

        @JvmStatic
        val VERSION_INFO: Triple<Long, String, String> by lazy { // versionCode, versionName, packageName
            val packageManager = instance.packageManager
            val packageName = instance.packageName
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                packageManager.getPackageInfo(packageName, 0)
            }
            Triple(PackageInfoCompat.getLongVersionCode(packageInfo), packageInfo.versionName, packageName)
        }
    }
}