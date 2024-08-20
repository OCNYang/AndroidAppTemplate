package com.app.library_ui_uniform

import android.content.Context
import androidx.startup.Initializer
import com.ocnyang.status_box.StatusBoxGlobalConfig
import com.ocnyang.status_box.initDef


/**
 * StatusBox 初始化
 *
 * 初始化是在 `AndroidManifest.xml` 中
 */
class StatusBoxInitializer : Initializer<StatusBoxGlobalConfig> {
    override fun create(context: Context): StatusBoxGlobalConfig {
        StatusBoxGlobalConfig.initDef()
        return StatusBoxGlobalConfig
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}