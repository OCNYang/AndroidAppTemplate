package com.app.template.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    surfaceTint = Color.White, // todo 颜色叠加(影响弹窗底色，及所有有 Elevation 高度的控件)；参考：[LocalTonalElevationEnabled] [Modifier.surface]
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    surface = Color.White,
    background = Color.White,
    surfaceTint = Color.White, // todo 同上
    primaryContainer = Color.White, // 浮动按钮的背景
)

/**
 * 这里的主题没有放到 [ui_uniform] 模块 的原因
 * 主题应该是 UI 标准化的一部分，但这里的标准化更多的是强调的 控件 的标准化；
 * 对于颜色和主题，每个 App 应该有自己的风格，所以放到 主模块中控制
 */
@Composable
fun AndroidAppTemplateTheme(
    darkTheme: Boolean = false, // isSystemInDarkTheme(), // todo 跟随手机日夜模式变化
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // todo 动态主题颜色：根据壁纸动态变化
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {// 动态配色，根据系统壁纸动态改变
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }


// todo 根据需求来决定：状态栏如何控制，目前是通过第三方库，下面代码是 compose 的控制方法
//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.background.toArgb() // 控制状态栏背景色
//
//            // WindowCompat.setDecorFitsSystemWindows(window,false) // 留出空间，!!!无效
//            WindowCompat.getInsetsController(window, view).apply {
//                isAppearanceLightStatusBars = !darkTheme // 控制状态栏图标颜色
////                hide(WindowInsetsCompat.Type.statusBars()) // 控制显隐
////                hide(WindowInsetsCompat.Type.navigationBars())
////                show(WindowInsetsCompat.Type.captionBar())
////                show(WindowInsetsCompat.Type.ime())
////                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE // 控制隐藏状态，滑动显示
//            }
//        }
//    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}