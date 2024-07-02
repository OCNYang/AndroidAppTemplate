package com.app.base

import android.content.ContentResolver
import android.content.Context
import android.net.Uri

/**
 * 工具函数
 * 参考 -> ~/docs/android_util_doc/readme.md
 */

/**
 * Drawable to Uri
 */
fun getDrawableUri(context: Context, id: Int): Uri = Uri.parse(context.resources.let {
    "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${it.getResourcePackageName(id)}/${it.getResourceTypeName(id)}/${it.getResourceEntryName(id)}"
})

