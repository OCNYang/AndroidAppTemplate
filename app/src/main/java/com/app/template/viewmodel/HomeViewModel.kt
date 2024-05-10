package com.app.template.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.app.api.CallListener
import com.app.api.TemplateApi
import com.app.base.LogX
import com.app.base.showToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.util.ServiceLoader

class HomeViewModel : ViewModel() {
    // 存储数据
    val savedStateHandle: SavedStateHandle = SavedStateHandle()

    // 可以考虑依赖注入
    // private val repository: HomeRepository by lazy { HomeRepository() }
    private val service = ServiceLoader.load(TemplateApi::class.java).first()

    private val _data = MutableStateFlow<PagingData<String>?>(null)
    val data: Flow<PagingData<String>> get() = _data.filterNotNull()

    init {
        requestData()
    }

    fun requestData() {
        viewModelScope.launch {
            val data = service.getDetailTemplate(listener = object : CallListener {
                override fun onError(code: Int?, msg: String?) {
                    showToast("请求错误[$code]：$msg")
                }

                override fun onCallStart() {
                    super.onCallStart()
                }

                override fun onCallEnd() {
                    super.onCallEnd()
                }
            })
            LogX.e("接口返回：$data")
        }
    }
}