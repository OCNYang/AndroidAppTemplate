package com.app.template.page.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.app.base.AppLifecycleViewModel
import com.app.base.Log
import com.app.base.NetworkChangeViewModel
import com.app.base.appViewModel
import com.app.template.page._widget.WebViewCompose
import com.app.template.viewmodel.HomeViewModel
import com.ocnyang.status_box.StateContainer
import com.ocnyang.status_box.StatusBox
import com.ocnyang.status_box.UIState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    onPlantClick: (String) -> Unit = {},
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    appLifecycleViewModel: AppLifecycleViewModel = appViewModel()
) {
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
    ) { contentPadding ->
        StatusBox(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            stateContainer = pageStateContainer,
            contentScrollEnabled = false
        ) {
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
