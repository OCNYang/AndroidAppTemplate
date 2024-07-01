package com.app.template

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // TemplateApp()

                    val engine: NavHostEngine = rememberNavHostEngine()
                    val navController: NavHostController = engine.rememberNavController()
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        startRoute = NavGraphs.root.startRoute,
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