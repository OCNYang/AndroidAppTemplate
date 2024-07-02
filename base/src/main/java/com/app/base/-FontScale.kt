package com.app.base

import android.content.Context
import android.content.res.Resources
import android.os.Build

/**
 * 对应用的字体控制
 *
 * 用来禁止跟随系统字体的变更
 */
fun Context.configFontScale(fontScale: Float = 1.0f, fontWeight: Int = 0): Context {
    val configuration = resources.configuration
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (configuration.fontWeightAdjustment != fontWeight) {
            configuration.fontWeightAdjustment = fontWeight
        }
    }

    // 上面字体粗细控制并不一定有效
    // 小米手机 sdk 33：configuration.extraConfig:android.content.res.MiuiConfiguration(val extraData(key_var_font_scale)) 内部的这个字段来控制 字体粗细的，fontWeightAdjustment 一直为 0
    // configuration.toString() -> {1.4 ?mcc?mnc [zh_CN] ldltr sw392dp w392dp h802dp 440dpi nrml long hdr widecg port uimode=11 finger -keyb/v/h -nav/h winConfig={ mBounds=Rect(0, 0 - 1080, 2340) mAppBounds=Rect(0, 90 - 1080, 2296) mMaxBounds=Rect(0, 0 - 1080, 2340) mDisplayRotation=ROTATION_0 mWindowingMode=fullscreen mDisplayWindowingMode=fullscreen mActivityType=standard mAlwaysOnTop=undefined mRotation=ROTATION_0 mInSplitScreen=false}                                                                               s.1 fontWeightAdjustment=0 themeChanged=5 themeChangedFlags=536870912 extraData = Bundle[{key_var_font_scale=50}]/d}

    if (configuration.fontScale != fontScale) {
        configuration.fontScale = fontScale
    }
    return createConfigurationContext(configuration)
}

fun Resources.configFontScale(context: Context, fontScale: Float = 1.0f, fontWeight: Int = 0): Resources {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (configuration.fontWeightAdjustment != fontWeight) {
            configuration.fontWeightAdjustment = fontWeight
        }
    }

    if (configuration.fontScale != fontScale) {
        configuration.fontScale = fontScale
    }
    return context.createConfigurationContext(configuration).resources
}