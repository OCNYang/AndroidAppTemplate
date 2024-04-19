package com.app.template.page.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.app.template.page._widget.WebViewCompose
import com.app.template.ui.theme.AndroidAppTemplateTheme
import com.app.template.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onPlantClick: (String) -> Unit = {},
    viewModel: HomeViewModel = HomeViewModel(),
) {
    val pagerState = rememberPagerState(pageCount = { 1 })
    Scaffold(
        modifier = modifier.background(Color.Blue),
        topBar = {
            HomeTopAppBar(
                pagerState = pagerState,
                onFilterClick = {
                    // viewModel.updateData()
                },
            )
        }
    ) { contentPadding ->
        HomePagerScreen(
            onPlantClick = onPlantClick,
            pagerState = pagerState,
            Modifier.background(Color.Yellow).padding(contentPadding).background(Color.Green)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePagerScreen(
    onPlantClick: (String) -> Unit,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            text = "Hello Compose!",
            modifier = modifier
        )
        WebViewCompose(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            url = "https://www.shedoor.net/"
        ) {
            this.settings.apply {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
            }
        }

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun HomeScreenPreview() {
    AndroidAppTemplateTheme {
        HomePagerScreen(
            onPlantClick = {},
            pagerState = rememberPagerState(pageCount = { 1 }),
        )
    }
}
