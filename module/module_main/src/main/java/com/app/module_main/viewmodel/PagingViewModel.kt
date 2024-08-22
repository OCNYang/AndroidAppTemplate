package com.app.module_main.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.api.CallListener
import com.blankj.utilcode.util.ToastUtils
import com.orhanobut.logger.Logger
import kotlinx.coroutines.delay

/**
 * Pager 使用的参考事例
 */
class PagingViewModel : ViewModel() {
    private val service = TestPagingApi()

    var isRefreshing by mutableStateOf(false)
        private set

    val pager = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 5,
            initialLoadSize = 20,
            enablePlaceholders = false
        )
    ) {
        getAllData()
    }.flow

    private fun getAllData(): PagingSource<Int, String> =
        object : IntKeyPagingSource<String>() {
            override fun initKey() = 1
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
                val key = params.key
                Logger.e("当前key:$key")
                val currentPage = key ?: initKey()
                var isError = false
                val detailData = service.requestList(currentPage, object : CallListener {
                    override fun onError(code: Int?, msg: String?) {
                        ToastUtils.showShort(msg ?: "")
                        isError = true
                    }

                    override fun onCallStart() {
                        if (currentPage == initKey()) {
                            isRefreshing = true
                        }
                    }

                    override fun onCallEnd() {
                        isRefreshing = false
                    }
                })

                if (isError) {
                    return LoadResult.Error(Throwable(message = "have a error"))
                }

                return LoadResult.Page(
                    data = detailData,
                    prevKey = getPrevKey(currentPage),
                    nextKey = if (currentPage >= 5) null else currentPage + 1
                )
            }
        }
}

/**
 * 模仿网络请求
 */
class TestPagingApi {
    suspend fun requestList(page: Int, callListener: CallListener): List<String> {
        Logger.e("TestPagingApi page:$page")
        callListener.onCallStart()
        delay(1000)
        callListener.onCallEnd()
        return if (page % 6 == 0) {
            callListener.onError(500, "service is error")
            listOf()
        } else {
            if (page >= 5) {
                listOf()
            } else {
                (0 until 20).map {
                    "data $page--$it"
                }
            }
        }
    }
}

/**
 * 以 Int 为页码
 */
abstract class IntKeyPagingSource<Value : Any> : PagingSource<Int, Value>() {
    open fun initKey() = 0
    protected fun getPrevKey(currentKey: Int) = if (currentKey > initKey()) currentKey - 1 else null

    override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
        return initKey()
    }
}