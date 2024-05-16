package com.app.glance

import android.content.ComponentName
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.Text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
            modifier = GlanceModifier.fillMaxSize()
                .background(GlanceTheme.colors.background),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            androidx.glance.Image(
                androidx.glance.ImageProvider(androidx.glance.R.color.glance_colorError), contentDescription = "mm",
                modifier = GlanceModifier.fillMaxWidth().height(100.dp).background(Color.Gray))

            Text(text = "Where to?", modifier = GlanceModifier.padding(12.dp))
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
}