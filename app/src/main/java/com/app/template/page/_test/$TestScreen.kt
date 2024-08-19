package com.app.template.page._test

import android.content.Intent
import android.view.RoundedCorner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.base.BaseApplication
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.LogcatTestScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * 应用调试页面：入口页面
 *
 * 为了方便测试
 */
@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun TestScreen(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "TEST PAGE",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Text(text = "版本信息 ${BaseApplication.VERSION_INFO}", color = Color.White, fontSize = 10.sp, modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.error), textAlign = TextAlign.Center)
            ListItem(
                headlineContent = { Text(text = "Logcat 运行日志捕获") },
                supportingContent = { Text(text = "⚠️ 使用完：记得清除日志", color = MaterialTheme.colorScheme.error, fontSize = 10.sp)},
                modifier = Modifier.clickable { navigator.navigate(LogcatTestScreenDestination()) }
            )
            ListItem(
                headlineContent = { Text(text = "网络监控（Chucker）") },
                modifier = Modifier.clickable {
                    context.startActivity(Intent().apply { setClassName(BaseApplication.VERSION_INFO.third,"com.chuckerteam.chucker.internal.ui.MainActivity") })
                }
            )
            ListItem(
                headlineContent = { Text(text = "内存泄露（Leaks）") },
                modifier = Modifier.clickable {
                    context.startActivity(Intent().apply { setClassName(BaseApplication.VERSION_INFO.third,"leakcanary.internal.activity.LeakActivity") })
                }
            )
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
                        Icon(Icons.Filled.Build, tint = MaterialTheme.colorScheme.error, contentDescription = "Test")
                    }
                )
            })
    }
}
