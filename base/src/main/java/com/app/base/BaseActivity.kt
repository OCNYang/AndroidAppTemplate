package com.app.base

import android.content.Context
import androidx.activity.ComponentActivity

abstract class BaseActivity : ComponentActivity() {

    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(context?.configFontScale())
    }

}