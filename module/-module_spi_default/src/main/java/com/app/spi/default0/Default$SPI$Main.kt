package com.app.spi.default0

import com.app.module_main.SPIMain
import com.ramcosta.composedestinations.generated.devtools.destinations.TestScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

interface DefaultSPIMain : SPIMain {
    override fun navigationToTestScreen(navigator: DestinationsNavigator) {
        navigator.navigate(TestScreenDestination())
    }
}