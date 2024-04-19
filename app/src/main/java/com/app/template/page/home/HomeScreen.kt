package com.app.template.page.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.app.template.R
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HomeTopAppBar(
                pagerState = pagerState,
                onFilterClick = {
                    // viewModel.updateData()
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { contentPadding ->
        HomePagerScreen(
            onPlantClick = onPlantClick,
            pagerState = pagerState,
            Modifier.padding(top = contentPadding.calculateTopPadding())
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
