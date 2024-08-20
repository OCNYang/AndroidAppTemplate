package com.app.base.`$viewmodel`

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 页面中只执行一次的逻辑
 * 多次声明无效：同一个可组合函数内，如果有多个逻辑想要执行，最好放在同一个 [LaunchedEffectStrict] 内；
 * 多次声明不同的 viewModelKey 才有效；
 *
 * key1: 如果想主动触发的话，设置此值
 */
@Composable
fun LaunchedEffectStrict(
    key1: Any? = null,
    viewModelKey: String? = null,
    viewModel: _LaunchedOnceViewModel = viewModel(key = viewModelKey),
    block: suspend CoroutineScope.() -> Unit,
) = LaunchedEffectStrict(
    key1 = key1,
    viewModelKey = viewModelKey,
    viewModel = viewModel,
    blocks = arrayOf(block)
)

@Composable
fun LaunchedEffectStrict(
    key1: Any? = null,
    viewModelKey: String? = null,
    viewModel: _LaunchedOnceViewModel = viewModel(key = viewModelKey),
    vararg blocks: suspend CoroutineScope.() -> Unit,
) {
    LaunchedEffect(key1 = key1 ?: Unit) {
        if (viewModel.onceFlow.value) {
            viewModel.lunched()
            blocks.forEach {
                it.invoke(this)
            }
        }
    }
}

class _LaunchedOnceViewModel : ViewModel() {
    private val _onceFlow = MutableStateFlow(true)
    val onceFlow = _onceFlow.asStateFlow()
    fun lunched() {
        _onceFlow.value = false
    }
}


/**
 * 重载个更方便理解的名字
 */
@Composable
fun LaunchedOnce(
    key1: Any? = null,
    viewModelKey: String? = null,
    viewModel: _LaunchedOnceViewModel = androidx.lifecycle.viewmodel.compose.viewModel(key = viewModelKey),
    block: suspend CoroutineScope.() -> Unit,
) = LaunchedEffectStrict(
    key1 = key1,
    viewModelKey = viewModelKey,
    viewModel = viewModel,
    blocks = arrayOf(block)
)

@Composable
fun LaunchedOnce(
    key1: Any? = null,
    viewModelKey: String? = null,
    viewModel: _LaunchedOnceViewModel = androidx.lifecycle.viewmodel.compose.viewModel(key = viewModelKey),
    vararg blocks: suspend CoroutineScope.() -> Unit,
) = LaunchedEffectStrict(
    key1 = key1,
    viewModelKey = viewModelKey,
    viewModel = viewModel,
    blocks = blocks
)

