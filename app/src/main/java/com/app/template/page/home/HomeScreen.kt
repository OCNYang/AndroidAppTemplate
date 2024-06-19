package com.app.template.page.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.app.base.AppLifecycleViewModel
import com.app.base.ImageX
import com.app.base.Log
import com.app.base.LogX
import com.app.base.NetworkChangeViewModel
import com.app.base.appViewModel
import com.app.base.invoke
import com.app.template.page.ScreenRoute
import com.app.template.page._widget.WebViewCompose
import com.app.template.viewmodel.HomeViewModel
import com.app.template.viewmodel.LaunchedOnce
import com.blankj.utilcode.util.ToastUtils
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
        ToastUtils.showShort("测试一下：只执行一次")
        LogX.e("kkkdkdk11111")
    }

    LaunchedOnce("eee") {
        LogX.e("kkkdkdk22222")
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
            contentScrollEnabled = true
        ) {

            val imgAddress = "https://pic-go-bed.oss-cn-beijing.aliyuncs.com/img/20220316151929.png"

            ImageX(
                model = imgAddress,
                modifier = Modifier.fillMaxWidth().height(120.dp),
            )

            Button(onClick = {
                viewModel.requestData()
            }) {
                Text(text = "发起网络请求")
            }
            Button(onClick = {
                onPlantClick.invoke(ScreenRoute.Detail.route)
            }) {
                Text(text = "跳转")
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
