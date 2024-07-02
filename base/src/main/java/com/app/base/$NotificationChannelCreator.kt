package com.app.base

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.net.Uri
import android.os.Build
import com.app.base.NotificationChannelInfo.Desc
import com.app.base.NotificationChannelInfo.ID
import com.app.base.NotificationChannelInfo.Name

object NotificationChannelInfo {
    val FOREGROUND_SERVICE = Triple("前台服务", "用于服务在运行时的前台提醒", "foreground_service")

    val Triple<String, String, String>.Name: String
        get() = first

    val Triple<String, String, String>.Desc: String
        get() = second

    val Triple<String, String, String>.ID: String
        get() = third
}

/**
 * 功能函数：创建 Notification 的信道
 *
 * @param notificationChannelConfig 对信道进行的配置；!每个信道一旦创建后配置不再可变
 */
fun createNotificationChannel(
    context: Context,
    notificationChannelInfo: Triple<String, String, String>,
    groupID: String? = null,
    notificationChannelConfig: NotificationChannel.() -> Unit = {}
) {
    createNotificationChannel(
        context = context,
        channelName = notificationChannelInfo.Name, channelDesc = notificationChannelInfo.Desc, channelID = notificationChannelInfo.ID,
        groupID = groupID,
        notificationChannelConfig = notificationChannelConfig
    )
}

@SuppressLint("ObsoleteSdkInt")
fun createNotificationChannel(
    context: Context,
    channelName: String, channelDesc: String, channelID: String,
    groupID: String? = null,
    notificationChannelConfig: NotificationChannel.() -> Unit = {}
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        var notificationChannel = notificationManager.getNotificationChannel(channelID)
        if (notificationChannel != null) {
            return
        }
        notificationChannel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.description = channelDesc
        notificationChannel.notificationChannelConfig()

        groupID?.let {
            notificationChannel.group = it
        }

        notificationManager.createNotificationChannel(notificationChannel)
    }
}

/**
 * 信道配置快捷方法
 */
fun NotificationChannel.buildForegroundServiceConfig() {
    importance = NotificationManager.IMPORTANCE_HIGH
}

/**
 * 信道配置快捷方法：推送通知，并自定义铃声
 */
fun NotificationChannel.buildPushMsgConfig(context: Context, soundRawRes: Int) {
    val uri = Uri.parse("android.resource://" + context.packageName + "/raw/" + soundRawRes)
    setSound(uri, Notification.AUDIO_ATTRIBUTES_DEFAULT)
    setShowBadge(true)
    enableVibration(true)
    importance = NotificationManager.IMPORTANCE_HIGH
}