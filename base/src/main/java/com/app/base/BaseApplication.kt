package com.app.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.text.TextUtils
import androidx.core.content.pm.PackageInfoCompat
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.PrettyFormatStrategy
import com.tencent.mmkv.MMKV
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        initMMKV()
        initLogLib()
        initToastUtils()
        initReactiveNetwork(this)
        ForegroundCheck.init(this)
        // logcatHelper.start()
    }

    // 断网提示
    @SuppressLint("MissingPermission")
    private fun initReactiveNetwork(app: BaseApplication) {
        val subscribe = ReactiveNetwork.observeNetworkConnectivity(app)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                it?.let {
                    if (it.type() == -1) {
//                        if (ToastUtils.isInit()) {
//                            ToastUtils.showLong("Network is ${it.state()}")
//                        }
                    }
                }
            }
    }

    private fun initToastUtils() {
//        ToastUtils.init(
//            this, LocationToastStyle(
//                CustomToastStyle(), Gravity.BOTTOM, 0,
//                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 85f, resources.displayMetrics).toInt(), 0f, 0f
//            )
//        )
    }

    private fun initMMKV() {
        val rootDir = MMKV.initialize(this)
    }


    private fun initLogLib() {
        LoG.addLogAdapter(object : AndroidLogAdapter(PrettyFormatStrategy.newBuilder().tag("e-bike").build()) {
            override fun isLoggable(priority: Int, tag: String?) = true//BuildConfig.DEBUG // 这里保持一直开启，这样才能保证日志器能正常记录
        })
    }

    override fun getResources(): Resources {
        // 禁止 app 字体大小跟随系统字体大小调节
        val resoure: Resources = super.getResources()
        if (resoure != null && resoure.configuration.fontScale != 1.0f) {
            val configuration: Configuration = resoure.configuration
            configuration.fontScale = 1.0f
            resoure.updateConfiguration(configuration, resoure.displayMetrics)
        }
        return resoure
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
        val VERSION_INFO: Pair<Long, String> by lazy {
            val packageManager = instance.packageManager
            val packageName = instance.packageName
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                packageManager.getPackageInfo(packageName, 0)
            }
            Pair(PackageInfoCompat.getLongVersionCode(packageInfo), packageInfo.versionName)
        }
    }
}