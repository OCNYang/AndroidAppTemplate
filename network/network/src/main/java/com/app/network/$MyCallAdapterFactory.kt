package retrofit2

import android.util.Log
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Proxy
import java.lang.reflect.Type
import java.util.concurrent.Executor

internal class MyCallAdapterFactory(private val callbackExecutor: Executor?) : CallAdapter.Factory() {
    override fun get(
        returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }
        require(returnType is ParameterizedType) { "Call return type must be parameterized as Call<Foo> or Call<? extends Foo>" }
        val responseType = Utils.getParameterUpperBound(0, returnType)

        val executor = if (Utils.isAnnotationPresent(annotations, SkipCallbackExecutor::class.java)) null else callbackExecutor

        return object : CallAdapter<Any?, Call<*>> {
            override fun responseType(): Type {
                return responseType
            }

            override fun adapt(call: Call<Any?>): Call<Any?> {
                val proxyCall = Proxy.newProxyInstance(
                    call.javaClass.classLoader,
                    arrayOf<Class<*>>(Call::class.java)
                ) { proxy, method, args ->
                    val result: Any?
                    try {
                        result = method.invoke(call, *args)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.d("TAG", "网络请求错误: " + e.message + e.message)
                        if (call is OkHttpCall) {
                            val request = call.request()
                            Log.d("TAG", "网络adapt: "+request.body)
                            Log.d("TAG", "网络tag: "+request.tag())
                            Log.d("TAG", "网络tageee: "+request.toString())
                        }
                        // todo 错误上报，这里只能获取接口的声明信息；无法获取请求参数或请求体；
                        throw e
                    }
                    result
                } as Call<Any?>

                return if (executor == null) proxyCall else DefaultCallAdapterFactory.ExecutorCallbackCall(executor, proxyCall)
            }
        }
    }
}
