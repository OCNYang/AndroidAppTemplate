package com.app.spi.default0

import com.app.module_dev_tools.SPIDevTools
import com.app.module_main.SPIMain
import com.ramcosta.composedestinations.generated.devtools.destinations.TestScreenDestination
import com.ramcosta.composedestinations.generated.main.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

// todo 记得有个注解，可以限制只能内部访问
interface DefaultSPIDevTools : SPIDevTools {
    override fun navigationToMainScreen(navigator: DestinationsNavigator) {
        navigator.navigate(HomeScreenDestination())
    }
}