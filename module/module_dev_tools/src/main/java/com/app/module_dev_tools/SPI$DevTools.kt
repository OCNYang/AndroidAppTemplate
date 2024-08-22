package com.app.module_dev_tools

import com.app.base.ServiceLoaderX
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

interface SPIDevTools {
    fun navigationToMainScreen(navigator: DestinationsNavigator)

    companion object {
        internal val INSTANCE by lazy { ServiceLoaderX.loadSafe(this::class.java) }
    }
}