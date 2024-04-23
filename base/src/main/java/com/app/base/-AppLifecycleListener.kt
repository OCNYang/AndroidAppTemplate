package com.app.base

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.eventFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class AppLifecycleViewModel : ViewModel() {

    val appLifecycleEventFlow = ProcessLifecycleOwner.get().lifecycle.eventFlow
    val appLifecycleStateFlow = ProcessLifecycleOwner.get().lifecycle.currentStateFlow

    internal val _lifecycleEvent: MutableStateFlow<Lifecycle.Event> = MutableStateFlow(Lifecycle.Event.ON_ANY)

    /**
     * 这里的弃用标志，只是提醒作用
     * 精简版生命周期，部分生命周期事件可能不会被调用
     * 例如应用退到后台时，会触发 ON_STOP，但是不会触发 ON_PAUSE
     * 推荐使用 [appLifecycleEventFlow]
     */
    @Deprecated("This is the reduced life cycle and parts may not be invoked", replaceWith = ReplaceWith("appLifecycleEventFlow"))
    val appSimpleLifecycleFlow: StateFlow<Lifecycle.Event> = _lifecycleEvent

    val appForegroundFlow: Flow<Boolean> = appLifecycleEventFlow
        .filter { it == Lifecycle.Event.ON_STOP || it == Lifecycle.Event.ON_START }
        .map {
            it != Lifecycle.Event.ON_STOP
        }

    companion object {
        fun Boolean.isForeground(): Boolean = this
        fun Boolean.isBackground(): Boolean = !this
    }
}

class AppForegroundListener : DefaultLifecycleObserver {

    private val appLifecycleViewModel: AppLifecycleViewModel by lazy {
        appViewModel()
    }

    fun init() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        appLifecycleViewModel._lifecycleEvent.value = Lifecycle.Event.ON_CREATE
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        appLifecycleViewModel._lifecycleEvent.value = Lifecycle.Event.ON_START
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        appLifecycleViewModel._lifecycleEvent.value = Lifecycle.Event.ON_RESUME
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        appLifecycleViewModel._lifecycleEvent.value = Lifecycle.Event.ON_PAUSE
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        appLifecycleViewModel._lifecycleEvent.value = Lifecycle.Event.ON_STOP
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        appLifecycleViewModel._lifecycleEvent.value = Lifecycle.Event.ON_DESTROY
    }

}
