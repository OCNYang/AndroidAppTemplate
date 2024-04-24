package com.app.base

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import com.blankj.utilcode.util.ScreenUtils
import com.gyf.immersionbar.ImmersionBar

/**
 * - 字体禁止跟随系统字体大小
 * - 强制竖屏【!!!横屏启动会有方向闪屏】所以推荐设置 android:screenOrientation="portrait"，这里的设置只是补偿设置
 * - 状态栏设置：貌似没差别就没设置
 */
abstract class BaseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!ScreenUtils.isPortrait()) {
            ScreenUtils.setPortrait(this)
        }

        ImmersionBar.with(this)
            // .navigationBarColor("#ffffffff") // todo 背景色
            .init()
    }

    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(context?.configFontScale())
    }

}