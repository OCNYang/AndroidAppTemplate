package com.app.template

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import com.app.base.BaseActivity
import com.app.base.BaseApplication
import com.app.glance.shortcut.buildAuthorShortcut
import com.app.template.page._test.TestFAB
import com.app.template.ui.theme.AndroidAppTemplateTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.ActivityDestination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.TestScreenDestination
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.spec.NavHostEngine

/**
 * 主页面：也是唯一的 Activity
 */
@ActivityDestination<RootGraph>
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            false // 这里可以设置一个变量，等待首页加载完成再显示
        }
        super.onCreate(savedInstanceState)
        BaseApplication.MAIN_ACTIVITY = this
        buildAuthorShortcut(this)

        setContent {
            AndroidAppTemplateTheme(dynamicColor = false) {
                // 解决 compose 1.7 和 lifecycle 2.8 版本兼容问题（可能并不局限这两个版本）
                // 详见：https://issuetracker.google.com/issues/336842920#comment8
                CompositionLocalProvider(value = androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // TemplateApp()

                        val engine: NavHostEngine = rememberNavHostEngine()
                        val navController: NavHostController = engine.rememberNavController()
                        DestinationsNavHost(
                            navGraph = NavGraphs.root,
                            engine = engine,
                            navController = navController
                        )

                        if (BaseApplication.TEST) {
                            TestFAB(onClick = {
                                val testScreenRoute = TestScreenDestination().route
                                if (testScreenRoute != navController.currentDestination?.route) {
                                    navController.navigate(testScreenRoute)
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}