package com.app.glance.shortcut

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.app.glance.R

/**
 * 应用图标长按的快捷菜单；在 MainActivity 中调用注册
 */
fun buildAuthorShortcut(context: Context) {
    val shortcut = ShortcutInfoCompat.Builder(context, "shortcut_author")
        .setShortLabel("作者")
        .setLongLabel("作者万岁")
        .setIcon(IconCompat.createWithResource(context, R.drawable.ic_shortcut_author))
        .setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ocnyang")))
        .build()
    ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
}