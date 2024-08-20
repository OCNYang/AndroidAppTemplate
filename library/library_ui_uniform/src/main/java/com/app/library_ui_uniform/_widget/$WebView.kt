package com.app.library_ui_uniform._widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.app.webview_x5.X5
import com.tencent.smtt.sdk.WebView

/**
 * WebView 的 compose 组合项
 */
@Composable
fun WebViewCompose(modifier: Modifier = Modifier, url: String, config: WebView.() -> Unit) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            X5.get(context).apply {
                this.config()
                loadUrl(url)
            }
        })
}