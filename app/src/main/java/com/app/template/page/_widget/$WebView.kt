package com.app.template.page._widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.app.webview_x5.X5
import com.tencent.smtt.sdk.WebView

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