package com.app.network

import android.text.TextUtils
import com.app.api.CallListener
import com.app.api.bean.BaseResult
import com.app.base.showToast
import com.app.network.error.ApiException
import com.app.network.error.ExceptionHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

fun <T> flowEmit(block: suspend () -> T): Flow<T> = flow { emit(block.invoke()) }.flowOn(Dispatchers.IO)

/**
 * @param call 请求方法
 * @param errorBlock 错误回调
 * @param showLoading 加载过程的回调
 * @param onComplete 请求完成回调
 */
fun <T> CoroutineScope.launch(
    call: suspend CoroutineScope.() -> Flow<BaseResult<T>>,
    errorBlock: ((Int?, String?) -> Unit)? = null,
    showLoading: ((Boolean) -> Unit)? = null,
    onComplete: (BaseResult<T>) -> Unit = {}
) {
    launch(
        // 5.捕获异常
        CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            run {
                val exception = ExceptionHandler.handleException(throwable)
                errorBlock?.invoke(exception.errCode, exception.errMsg)
            }
        }
    ) {
        // 1.执行请求 & 发送请求结果回调 & 指定 flow {} 的线程 [@link flowEmit]
        call.invoke(this)
            .onStart {
                // 4.请求开始，展示加载框
                showLoading?.invoke(true)
            }
            // 5.捕获异常
//            .catch { e ->
//                e.printStackTrace()
//                val exception = ExceptionHandler.handleException(e)
//                errorBlock?.invoke(exception.errCode, exception.errMsg)
//            }
            // 6.请求完成，包括成功和失败
            .onCompletion {
                showLoading?.invoke(false)
            }
            .collect {
                onComplete.invoke(it)
            }
    }
}

/**
 * 通过flow执行请求，需要在协程作用域中执行
 * @param errorBlock 错误回调
 * @param requestCall 执行的请求
 * @param showLoading 开启和关闭加载框
 * @return 请求结果
 */
suspend fun <T> requestFlow(
    requestCall: suspend () -> BaseResult<T>?,
    listener: CallListener? = null
): T? {
    var data: T? = null
    val flow = requestFlowResponse(requestCall, listener)
    //7.调用collect获取emit()回调的结果，就是请求最后的结果
    flow.collect {
        data = it?.data
    }
    return data
}

/**
 * 通过flow执行请求，需要在协程作用域中执行
 * @param errorBlock 错误回调
 * @param requestCall 执行的请求
 * @param showLoading 开启和关闭加载框
 * @return Flow<BaseResponse<T>>
 */
suspend fun <T> requestFlowResponse(
    requestCall: suspend () -> BaseResult<T>?,
    listener: CallListener?
): Flow<BaseResult<T>?> {
    // 1.执行请求
    val flow = flow {
        // 设置超时时间
        val response = withTimeout(10 * 1000) {
            requestCall()
        }

        // 这里判断返回的接口业务错误状态
        if (response?.isSuccess == false) {
            if (listener == null && !(TextUtils.isEmpty(response.msg))) {
                showToast("${response.msg}")
            }
            throw ApiException(response.code, response.msg)
        }
        //2.发送网络请求结果回调
        emit(response)
        //3.指定运行的线程，flow {}执行的线程
    }.flowOn(Dispatchers.IO)
        .onStart {
            //4.请求开始，展示加载框
            listener?.onCallStart()
        }
        //5.捕获异常
        .catch { e ->
            e.printStackTrace()
            val exception = ExceptionHandler.handleException(e)
            listener?.onError(exception.errCode, exception.errMsg)
        }
        //6.请求完成，包括成功和失败
        .onCompletion {
            listener?.onCallEnd()
        }
    return flow
}