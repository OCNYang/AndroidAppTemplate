package com.app.template.page

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ShareCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.template.R
import com.app.template.page.home.HomeScreen

@Composable
fun TemplateApp() {
    val navController = rememberNavController()
    SunFlowerNavHost(
        navController = navController
    )
}

@Composable
fun SunFlowerNavHost(
    navController: NavHostController
) {
    val activity = (LocalContext.current as Activity)
    NavHost(navController = navController, startDestination = ScreenRoute.Home.route) {
        composable(route = ScreenRoute.Home.route) {
            HomeScreen(
                onPlantClick = {
                    navController.navigate(
                        ScreenRoute.PlantDetail.createRoute(
                            plantId = "id"
                        )
                    )
                }
            )
        }
//
//        composable(
//            route = Screen.PlantDetail.route,
//            arguments = Screen.PlantDetail.navArguments
//        ) {
//            PlantDetailsScreen(
//                onBackClick = { navController.navigateUp() },
//                onShareClick = {
//                    createShareIntent(activity, it)
//                },
//                onGalleryClick = {
//                    navController.navigate(
//                        Screen.Gallery.createRoute(
//                            plantName = it.name
//                        )
//                    )
//                }
//            )
//        }
//
//        composable(
//            route = Screen.Gallery.route,
//            arguments = Screen.Gallery.navArguments
//        ) {
//            GalleryScreen(
//                onPhotoClick = {
//                    val uri = Uri.parse(it.user.attributionUrl)
//                    val intent = Intent(Intent.ACTION_VIEW, uri)
//                    activity.startActivity(intent)
//                },
//                onUpClick = {
//                    navController.navigateUp()
//                })
//        }
    }
}

// Helper function for calling a share functionality.
// Should be used when user presses a share button/menu item.
private fun createShareIntent(activity: Activity, plantName: String) {
    val shareText = activity.getString(R.string.share_text_plant, plantName)
    val shareIntent = ShareCompat.IntentBuilder(activity)
        .setText(shareText)
        .setType("text/plain")
        .createChooserIntent()
        .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
    activity.startActivity(shareIntent)
}