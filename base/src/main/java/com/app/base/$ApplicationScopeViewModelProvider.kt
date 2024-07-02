package com.app.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.app.base.ApplicationScopeViewModelProvider.mApplicationProvider

/**
 * 功能函数：生成具有全局生命周期的 ViewModel
 *
 * @sample `val viewModel:XViewModel = appViewModel()`
 *
 */
inline fun <reified VM : ViewModel> appViewModel(): VM {
    return mApplicationProvider[VM::class.java]
}

object ApplicationScopeViewModelProvider : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore
        get() = ViewModelStore()

    val mApplicationProvider: ViewModelProvider by lazy {
        ViewModelProvider(
            ApplicationScopeViewModelProvider,
            ViewModelProvider.AndroidViewModelFactory.getInstance(BaseApplication.INSTANCE)
        )
    }
}
