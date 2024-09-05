package com.app.network

import com.app.base.BaseApplication
import com.app.network.adapter.BigDecimalAdapter
import com.app.network.adapter.DefaultOnDataMismatchAdapter
import com.app.network.adapter.FilterNullStringFromListAdapter
import com.app.network.adapter.FilterNullValuesFromListAdapter
import com.app.api_base.PATH
import com.app.network.interceptor.CookiesInterceptor
import com.app.network.interceptor.HeaderInterceptor
import com.app.network.interceptor.logInterceptor
import com.app.network.monitor.NetworkEventListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.DefaultIfNullFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.functions.Function
import okhttp3.OkHttpClient
import retrofit2.MonitorCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object HttpManager {
    private val mRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .client(initOkHttpClient())
            .baseUrl(PATH.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(
                MonitorCallAdapterFactory(
                    null,
                    Report,
                    Report
                )
            ) // 用于监控网络请求错误，以用来上报埋点
            .build()
    }

    private val moshi by lazy {
        Moshi.Builder().add(BigDecimalAdapter)
            .addLast(KotlinJsonAdapterFactory()) // todo 基于反射解析时需要设置
            // .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .add(DefaultOnDataMismatchAdapter.newFactory(Any::class.java, null))
            .add(FilterNullValuesFromListAdapter.newFactory(Any::class.java))// 这个应该是无效的，永远走不到。具体可以参考  [FilterNullStringFromListAdapter]
            .add(FilterNullStringFromListAdapter.newFactory(Any::class.java))
            .add(DefaultIfNullFactory())
            .build()
    }

    /**
     * 获取 apiService
     */
    fun <T> create(apiService: Class<T>): T {
        return mRetrofit.create(apiService)

    }

    /**
     * 自定义 Retrofit
     * 比如，某个接口的 base_url 比较特殊
     * @sample
     * ```
     * customCreate(apiService) {
     *     baseUrl("https://github.com/ocnyang")
     *     client(customOkHttpClient {
     *         addInterceptor(HeaderInterceptor())
     *     })
     * }
     * ```
     */
    fun <T> customCreate(
        apiService: Class<T>,
        custom: Retrofit.Builder.() -> Unit
    ): T {
        val customRetrofitBuilder = mRetrofit.newBuilder()
        customRetrofitBuilder.custom()
        return customRetrofitBuilder.build().create(apiService)
    }

    /**
     * 自定义 okhttp
     * 这里是为 [customCreate] 服务的
     */
    fun customOkHttpClient(custom: OkHttpClient.Builder.() -> Unit): OkHttpClient {
        val newBuilder = initOkHttpClient().newBuilder()
        newBuilder.custom()
        return newBuilder.build()
    }

    /**
     * 初始化 OkHttp
     */
    private fun initOkHttpClient(): OkHttpClient {
        val build = OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .writeTimeout(12, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)

        build.eventListener(NetworkEventListener()) // 用来记录网络请求历史

        build.addInterceptor(ChuckerInterceptor(BaseApplication.INSTANCE)) // 用来记录网络请求便于查看和测试
        build.addInterceptor(CookiesInterceptor())
        build.addInterceptor(HeaderInterceptor())
        build.addInterceptor(logInterceptor)

        // 网络状态拦截
        // build.addInterceptor(noNetworkInterceptor)
        return build.build()
    }
}