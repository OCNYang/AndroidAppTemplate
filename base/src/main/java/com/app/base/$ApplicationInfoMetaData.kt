package com.app.base

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle

/**
 * 动态修改替换 AndroidManifest.xml 中 meta-data 的值
 *
 * 主要用来修改一些（未提供动态能力的）第三方平台的 key 配置，比如高德
 *
 * @sample
 * ```context.changeApplicationInfoMetaData(){ putString("","") }```
 */
fun Context.changeApplicationInfoMetaData(metaDataAction: Bundle.() -> Unit) {
    var applicationInfo: ApplicationInfo? = null
    try {
        applicationInfo = this.packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    applicationInfo?.let {
        val metaData = it.metaData
        metaData.metaDataAction()
        applicationInfo.metaData = metaData
    }
}