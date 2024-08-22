package com.app.template

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import com.app.base.BaseActivity
import com.app.base.BaseApplication
import com.app.glance.shortcut.buildAuthorShortcut
import com.app.template.ui.theme.AndroidAppTemplateTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.ActivityDestination
import com.ramcosta.composedestinations.generated.devtools.destinations.TestScreenDestination
import com.ramcosta.composedestinations.generated.root.navgraphs.AppNavGraph
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.spec.NavHostEngine

/**
 * 主页面：也是唯一的 Activity
 */
@ActivityDestination<AppGraph>
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
                            navGraph = AppNavGraph,
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


@Composable
fun TestFAB(onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        // todo 当打印日志时，可以显示一个指示
        SmallFloatingActionButton(
            modifier = Modifier.padding(end = 15.dp, bottom = 45.dp),
            shape = RoundedCornerShape(5.dp),
            onClick = onClick,
            content = {
                BadgedBox(
                    badge = {
                    },
                    content = {
                        Icon(
                            Icons.Filled.Build,
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = "Test"
                        )
                    }
                )
            })
    }
}
