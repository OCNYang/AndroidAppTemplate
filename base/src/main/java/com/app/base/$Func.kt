package com.app.base

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.media.AudioManager
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.tencent.mmkv.MMKV

/**
 * 复制到剪贴板
 */
fun copyText(text: String, context: Context) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText("", text))
}

/**
 *  隐藏键盘
 */
fun hideKeyboard(activity: Activity) {
    val imm: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = activity.currentFocus
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * drawable to Uri
 */
fun getDrawableUri(context: Context, id: Int): Uri = Uri.parse(context.resources.let {
    "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${it.getResourcePackageName(id)}/${it.getResourceTypeName(id)}/${it.getResourceEntryName(id)}"
})

/**
 * 设置手机音量
 */
fun setPhoneVolume(context: Context, index: Int = -1) {
    (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).apply {
        setStreamVolume(AudioManager.STREAM_MUSIC, if (index == -1) getStreamMaxVolume(AudioManager.STREAM_MUSIC) else index, 0)
    }
}

/**
 * 调用拨号键
 */
fun String.call(context: Context) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$this")
    context.startActivity(intent)
}