package com.app.template.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 页面中只执行一次的逻辑
 * 多次声明无效：同一个可组合函数内，如果有多个逻辑想要执行，最好放在同一个 [LaunchedOnce] 内；
 * 多次声明不同的 key 才有效；
 */
@Composable
fun LaunchedOnce(
    key: String? = null,
    viewModel: _LaunchedOnceViewModel = androidx.lifecycle.viewmodel.compose.viewModel(key = key),
    block: suspend CoroutineScope.() -> Unit,
) = LaunchedOnce(key = key, viewModel = viewModel, blocks = arrayOf(block))

@Composable
fun LaunchedOnce(
    key: String? = null,
    viewModel: _LaunchedOnceViewModel = androidx.lifecycle.viewmodel.compose.viewModel(key = key),
    vararg blocks: suspend CoroutineScope.() -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
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
