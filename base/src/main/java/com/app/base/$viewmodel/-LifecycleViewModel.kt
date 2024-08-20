package com.app.base.`$viewmodel`

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 对可组合项生命周期的监听
 * 借助 viewModel 完成监听器的解绑
 * 在 onDispose 解绑的话，将影响 destroy 的回调；不解绑的话，将会重复添加监听器。
 */
@Composable
fun LifecycleEffect(
    onCreate: (() -> Unit)? = null,
    onStart: (() -> Unit)? = null,
    onResume: (() -> Unit)? = null,
    onPause: (() -> Unit)? = null,
    onStop: (() -> Unit)? = null,
    onDestroy: (() -> Unit)? = null,
    onDispose: (() -> Unit)? = null,
    viewModel: _LifecycleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val destroyState = viewModel.clearFlow.collectAsState()

    LaunchedEffect(key1 = destroyState.value) {
        if (destroyState.value) {
            viewModel.observer?.let {
                lifecycleOwner.lifecycle.removeObserver(it)
                viewModel.observer = null
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        if (viewModel.observer == null) {
            viewModel.observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> onCreate?.invoke()
                    Lifecycle.Event.ON_START -> onStart?.invoke()
                    Lifecycle.Event.ON_RESUME -> onResume?.invoke()
                    Lifecycle.Event.ON_PAUSE -> onPause?.invoke()
                    Lifecycle.Event.ON_STOP -> onStop?.invoke()
                    Lifecycle.Event.ON_DESTROY -> { // 只有退出可组合项时才会调用，晚于 onDispose
                        onDestroy?.invoke()
                    }

                    else -> {}
                }
            }

            lifecycleOwner.lifecycle.addObserver(viewModel.observer!!)
        }

        onDispose { // 可组合项不显示时就会调用：比如打开下一页面后
            onDispose?.invoke()
        }
    }
}


class _LifecycleViewModel : ViewModel() {
    private val _clearFlow = MutableStateFlow(false)
    val clearFlow = _clearFlow.asStateFlow()

    var observer: LifecycleObserver? = null

    override fun onCleared() {
        _clearFlow.value = true
        super.onCleared()
    }
}