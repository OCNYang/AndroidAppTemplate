package com.app.template

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.app.base.BaseActivity
import com.app.base.BaseApplication
import com.app.template.page.TemplateApp
import com.app.template.ui.theme.AndroidAppTemplateTheme

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            false // 这里可以设置一个变量，等待首页加载完成再显示
        }
        super.onCreate(savedInstanceState)
        BaseApplication.MAIN_ACTIVITY = this

        setContent {
            AndroidAppTemplateTheme(dynamicColor = false) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TemplateApp()
                }
            }
        }
    }

}