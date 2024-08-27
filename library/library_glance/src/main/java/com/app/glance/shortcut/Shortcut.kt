package com.app.glance.shortcut

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.app.glance.R

fun buildAuthorShortcut(context: Context) {
    val shortcut = ShortcutInfoCompat.Builder(context, "shortcut_author")
        .setShortLabel("关于作者")
        .setLongLabel("作者信息")
        .setIcon(IconCompat.createWithResource(context, R.drawable.ic_shortcut_author))
        .setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ocnyang")))
        .build()
    ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
}