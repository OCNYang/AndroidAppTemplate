package com.app.glance

import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.Text
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class MyGlanceAppWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {

        withContext(Dispatchers.IO) {// 耗时任务要切换线程

        }

        provideContent {
            GlanceTheme {
                MyContent()
            }
        }
    }

    @Composable
    private fun MyContent() {
        Column(
            modifier = GlanceModifier.fillMaxSize().background(GlanceTheme.colors.background),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val context = LocalContext.current
            val url = getImageUrl(LocalSize.current)
            val scope = rememberCoroutineScope()
            var randomImage by remember(url) { mutableStateOf<Bitmap?>(null) }

            LaunchedEffect(url) {
                randomImage = context.getRandomImage(url)
            }

            if (randomImage != null) {
                androidx.glance.Image(// 网络图片现在支持比较艰难
                    androidx.glance.ImageProvider(randomImage!!), contentDescription = "mm",
                    modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
                    contentScale = ContentScale.Crop
                )
            }

            androidx.glance.Image(// 网络图片现在支持比较艰难
                androidx.glance.ImageProvider(android.R.mipmap.sym_def_app_icon), contentDescription = "mm",
                modifier = GlanceModifier.fillMaxWidth().height(100.dp)
            )
            Text(text = "Where to?", modifier = GlanceModifier.padding(12.dp))
            Row(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    text = "Home",
                    onClick = actionStartActivity(componentName = ComponentName("com.app.template", "com.app.template.MainActivity"))
                )
                Button(
                    text = "Work",
                    onClick = actionStartActivity(componentName = ComponentName("com.app.template", "com.app.template.MainActivity"))
                )
            }
        }
    }

    private fun getImageUrl(size: DpSize) =
        "https://c.53326.com/d/file/lan2018060915/zm02z42l2pj.jpg"

    private suspend fun Context.getRandomImage(url: String, force: Boolean = false): Bitmap? {
        val request = ImageRequest.Builder(this).data(url).apply {
            if (force) {
                memoryCachePolicy(CachePolicy.DISABLED)
                diskCachePolicy(CachePolicy.DISABLED)
            }
        }.build()

        // Request the image to be loaded and throw error if it failed
        return when (val result = imageLoader.execute(request)) {
            is ErrorResult -> throw result.throwable
            is SuccessResult -> result.drawable.toBitmapOrNull()
        }
    }
}