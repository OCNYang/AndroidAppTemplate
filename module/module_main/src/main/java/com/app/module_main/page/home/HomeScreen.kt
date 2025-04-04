package com.app.module_main.page.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.multidex.BuildConfig
import com.app.base.AppLifecycleViewModel
import com.app.library_ui_uniform._widget.ImageX
import com.app.base.Log
import com.app.base.LogX
import com.app.base.NetworkChangeViewModel
import com.app.base.appViewModel
import com.app.library_ui_uniform._widget.Scaffold
import com.app.library_ui_uniform._widget.SnackBarAction
import com.app.library_ui_uniform._widget.WebViewCompose
import com.app.module_main.viewmodel.HomeViewModel
import com.app.base.`$viewmodel`.LaunchedOnce
import com.app.module_main.SPIMain
import com.app.module_main.page.MainGraph
import com.ocnyang.status_box.StateContainer
import com.ocnyang.status_box.StatusBox
import com.ocnyang.status_box.UIState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.main.destinations.DetailScreenDestination
import com.ramcosta.composedestinations.generated.main.destinations.ListScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@SuppressLint("SetJavaScriptEnabled")
@Destination<MainGraph>(start = true)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    navigator: DestinationsNavigator,
    // onPlantClick: (String) -> Unit = {},
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    appLifecycleViewModel: AppLifecycleViewModel = appViewModel()
) {
    val context = LocalContext.current

    val pagerState = rememberPagerState(pageCount = { 1 })
    val pageStateContainer = StateContainer(state = UIState.Success(""))

    val appLifeState = appLifecycleViewModel.appSimpleLifecycleFlow.collectAsState()
    val networkState = appViewModel<NetworkChangeViewModel>().networkFlow.collectAsState()

    LaunchedEffect(key1 = appLifeState.value) {
        Log.d("应用:${appLifeState.value}")
    }

    LaunchedEffect(key1 = networkState.value) {
        Log.e("网络：${networkState.value.toString()}")
    }

    LaunchedOnce {
        LogX.e("only run once >>>>>>>>>>>>")
        BuildConfig.DEBUG
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            HomeTopAppBar(
                pagerState = pagerState,
                onFilterClick = {
                    // viewModel.updateData()
                },
            )
        }
    ) { contentPadding, showSnackBar ->
        StatusBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            stateContainer = pageStateContainer,
            contentScrollEnabled = true
        ) {

            val imgAddress = "https://pic-go-bed.oss-cn-beijing.aliyuncs.com/img/20220316151929.png"

            ImageX(
                model = imgAddress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
            )

            Row(Modifier.horizontalScroll(rememberScrollState())) {
                Button(onClick = {
                    viewModel.requestData()
                }) {
                    Text(text = "发起网络请求")
                }
                Button(onClick = {
                    navigator.navigate(DetailScreenDestination())
                }) {
                    Text(text = "跳转详情")
                }
                Button(onClick = {
                    navigator.navigate(ListScreenDestination())
                }) {
                    Text(text = "跳转列表")
                }
                Button(onClick = {
                    showSnackBar(
                        SnackBarAction(
                            "snackbar",
                            true,
                            actionLabel = "wow"
                        )
                    ) { snackbarResult ->
                        Log.e("----000-----$snackbarResult")
                    }
                }) {
                    Text(text = "SnackBar")
                }
                Button(onClick = {
                    SPIMain.INSTANCE?.navigationToTestScreen(navigator)
                }) {
                    Text(text = "跳转 Test")
                }
            }

            WebViewCompose(
                modifier = Modifier.fillMaxSize(),
                url = "https://github.com/ocnyang"
            ) {
                this.settings.apply {
                    javaScriptEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                }
            }
        }
    }

}

