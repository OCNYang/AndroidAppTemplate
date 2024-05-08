package com.app.base

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class NetworkChangeViewModel : ViewModel() {
    internal val _data: MutableStateFlow<Connectivity?> = MutableStateFlow(null)
    val networkFlow: StateFlow<Connectivity?> = _data
}

/**
 * 网络连接断开的提示
 */
object NetworkChangeListener {
    /**
     * need : android.permission.ACCESS_NETWORK_STATE
     */
    @SuppressLint("CheckResult")
    fun init(
        context: Context,
        onChange: (Connectivity) -> Unit = { Log.d("NetworkChangeListener", "event network: ${it.toString()}") }
    ) {
        ReactiveNetwork.observeNetworkConnectivity(context.applicationContext)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                appViewModel<NetworkChangeViewModel>()._data.value = it
                onChange(it)
            }
    }
}