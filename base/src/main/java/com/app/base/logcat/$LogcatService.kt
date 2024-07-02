package com.app.base.logcat

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.app.base.NotificationChannelInfo
import com.app.base.NotificationChannelInfo.ID
import com.app.base.buildForegroundServiceConfig
import com.app.base.createNotificationChannel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.Random

/**
 * 前台服务：应用运行日志捕获
 *
 * @see [com.app.template.page._test.LogcatTestScreen]
 */
class LogcatService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        startLogcat()
        return super.onStartCommand(intent, flags, startId)
    }

    private var logcatProc: Process? = null
    private var reader: BufferedReader? = null

    @OptIn(DelicateCoroutinesApi::class)
    private fun startLogcat() {
        val command = buildLogcatCommand()

        logcatRunning = true

        kotlinx.coroutines.GlobalScope.launch {
            try {
                Runtime.getRuntime().exec("logcat -c")
                Runtime.getRuntime().exec(command)?.let {
                    logcatProc = it
                    reader = BufferedReader(InputStreamReader(it.inputStream), 1024)
                    var line: String? = null
                    while (logcatRunning && kotlin.run { line = reader!!.readLine(); line } != null) {
                        if (!logcatRunning) {
                            break
                        }
                        if (line!!.isEmpty()) {
                            continue
                        }
                        line += System.lineSeparator()
                        logcatDataFlow.value += line
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                logcatProc?.destroy()
                logcatProc = null

                try {
                    reader?.close()
                    reader = null
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startForeground() {
        createNotificationChannel(
            this,
            NotificationChannelInfo.FOREGROUND_SERVICE
        ) {
            this.buildForegroundServiceConfig()
        }

        try {
            val notification = NotificationCompat.Builder(this, NotificationChannelInfo.FOREGROUND_SERVICE.ID)
                .setOngoing(true)
                .build()
            ServiceCompat.startForeground(
                this,
                Random().nextInt(Int.MAX_VALUE),
                notification,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE
                } else {
                    0
                },
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun buildLogcatCommand(): String {
        return "logcat | grep \"(${android.os.Process.myPid().toString()})\" *:*"
    }

    companion object {
        const val COMMAND = "logcat-command"
        const val LOGCAT_OKHTTP = "okhttp.OkHttpClient:I"

        /**
         * 使用后记得清除掉
         */
        val logcatDataFlow = MutableStateFlow("")

        var logcatRunning = false
        fun stop() {
            logcatRunning = false
        }
    }
}