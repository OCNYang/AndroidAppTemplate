package com.app.template.page._test

import android.content.Intent
import android.text.TextUtils
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.app.base.logcat.LogcatService
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun LogcatTestScreen(filterTag: String = "") {

    val logsFlow = LogcatService.logcatDataFlow.collectAsState()

    val context = LocalContext.current
    val switchState = remember {
        mutableStateOf(LogcatService.logcatRunning)
    }

    val justFilterText = rememberSaveable {
        mutableStateOf(TextUtils.isEmpty(filterTag))
    }

    val filterState = rememberSaveable {
        mutableStateOf(filterTag)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "运行日志") }, actions = {
                Button(
                    onClick = {
                        val intent = Intent(context, LogcatService::class.java)
                        if (switchState.value) {
                            LogcatService.stop()
                            context.stopService(intent)
                            switchState.value = false
                        } else {
                            intent.putExtra(LogcatService.COMMAND, LogcatService.LOGCAT_OKHTTP)
                            context.startForegroundService(intent)
                            switchState.value = true
                        }
                    },
                    content = {
                        Text(text = if (!switchState.value) "开始" else "结束")
                    },
                )

                Button(
                    onClick = {
                        LogcatService.logcatDataFlow.value = ""
                    },
                    content = {
                        Text(text = "清空日志")
                    },
                    enabled = !TextUtils.isEmpty(logsFlow.value)
                )
            })
        }
    ) {
        SelectionContainer(Modifier.padding(it)) {
            Column(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    text = logsFlow.value,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.wrapContentWidth().align(Alignment.Start).wrapContentHeight().horizontalScroll(rememberScrollState())
                )
            }
        }
    }
}
