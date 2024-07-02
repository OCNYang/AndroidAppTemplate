package com.app.base

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.blankj.utilcode.util.ScreenUtils
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar

/**
 * Activity 基类
 *
 * - 字体禁止跟随系统字体大小
 * - 强制竖屏【!!!横屏启动会有方向闪屏】所以推荐设置 android:screenOrientation="portrait"，这里的设置只是补偿设置
 * - 状态栏设置：貌似没差别就没设置
 */
abstract class BaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 强制横屏，但是在 App 打开的一瞬间是控制不了的；所以在 AndroidManifest.xml 中也进行了控制
        if (!ScreenUtils.isPortrait()) {
            ScreenUtils.setPortrait(this)
        }

        // 状态栏、导航栏的控制
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .navigationBarDarkIcon(true)
            .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
            // .navigationBarColor("#ffffffff") // todo 背景色
            .init()
    }

    override fun attachBaseContext(context: Context?) {
        // 应用状态大小的控制
        super.attachBaseContext(context?.configFontScale())
    }

}