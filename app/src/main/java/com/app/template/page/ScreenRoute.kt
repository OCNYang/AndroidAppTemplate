package com.app.template.page

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Deprecated("已弃用，转为使用 Navigation 三方封装库")
sealed class ScreenRoute(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : ScreenRoute("home")
    data object Detail : ScreenRoute("detail")

    data object PlantDetail : ScreenRoute(
        route = "plantDetail/{plantId}",
        navArguments = listOf(navArgument("plantId") {
            type = NavType.StringType
        })
    ) {
        fun createRoute(plantId: String) = "plantDetail/${plantId}"
    }

    data object Gallery : ScreenRoute(
        route = "gallery/{plantName}",
        navArguments = listOf(navArgument("plantName") {
            type = NavType.StringType
        })
    ) {
        fun createRoute(plantName: String) = "gallery/${plantName}"
    }
}