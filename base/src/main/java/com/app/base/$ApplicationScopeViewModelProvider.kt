package com.app.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.app.base.ApplicationScopeViewModelProvider.mApplicationProvider

object ApplicationScopeViewModelProvider : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore
        get() = ViewModelStore()

    val mApplicationProvider: ViewModelProvider by lazy {
        ViewModelProvider(
            ApplicationScopeViewModelProvider,
            ViewModelProvider.AndroidViewModelFactory.getInstance(BaseApplication.instance)
        )
    }
}


inline fun <reified VM : ViewModel> appViewModel(): VM {
    return mApplicationProvider[VM::class.java]
}