package com.app.module_main

import com.app.base.ServiceLoaderX
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

interface SPIMain {
    fun navigationToTestScreen(navigator: DestinationsNavigator)

    companion object {
        internal val INSTANCE by lazy { ServiceLoaderX.loadSafe(SPIMain::class.java) }
    }
}

