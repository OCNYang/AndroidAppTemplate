package com.app.template.page.paging

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.template.viewmodel.PagingViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Destination<RootGraph>
@Composable
fun ListScreen() {
    PullToRefreshViewModelSample()
}

/**
 * 列表：下拉刷新、上拉加载更多 使用例子
 *
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
fun PullToRefreshViewModelSample(viewModel: PagingViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val pagingItems = viewModel.pager.collectAsLazyPagingItems()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Title") },
                // Provide an accessible alternative to trigger refresh.
                actions = {
                    IconButton(
                        enabled = !viewModel.isRefreshing,
                        onClick = { pagingItems.refresh() }
                    ) {
                        Icon(Icons.Filled.Refresh, "Trigger Refresh")
                    }
                }
            )
        })
    {
        BoxWithConstraints(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            PullToRefreshBox(
                modifier = Modifier.fillMaxSize(),
                isRefreshing = viewModel.isRefreshing,
                onRefresh = { pagingItems.refresh() }
            ) {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(pagingItems.itemCount) {
                        ListItem({ Text(text = "Item ${pagingItems[it]}") })
                    }
                    pagingItems.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> { // 正在加载首页数据时的显示
                                item {
                                    LazyColumnEmptyBox(
                                        height = this@BoxWithConstraints.maxHeight,
                                        state = "正在加载中..."
                                    )
                                }
                            }

                            loadState.refresh is LoadState.Error -> { // 加载首页数据时出现错误
                                val error = loadState.refresh as LoadState.Error
                                item {
                                    LazyColumnEmptyBox(
                                        height = this@BoxWithConstraints.maxHeight,
                                        state = "error",
                                        content = {
                                            Text(
                                                text = "错误：${error.error.message}   点击重试",
                                                modifier = Modifier
                                                    .clickable {
                                                        retry()
                                                    }
                                                    .padding(10.dp),
                                                textAlign = TextAlign.Center,
                                                color = Color.Red
                                            )
                                        })
                                }
                            }

                            loadState.append is LoadState.Loading -> { // 正在加载更多数据
                                item {
                                    Text(
                                        text = "loading...",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 10.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            loadState.append is LoadState.Error -> { // 加载更多数据时，出现错误
                                val error = loadState.append as LoadState.Error
                                item {
                                    Text(
                                        text = "错误 2 ：${error.error.message}   点击重试",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                retry()
                                            }
                                            .padding(vertical = 10.dp),
                                        textAlign = TextAlign.Center,
                                        color = Color.Red
                                    )
                                }
                            }

                            loadState.refresh is LoadState.NotLoading && loadState.append is LoadState.NotLoading && itemCount == 0 -> { // 没有任何数据时显示的空布局
                                item {
                                    LazyColumnEmptyBox(
                                        height = this@BoxWithConstraints.maxHeight,
                                        state = "-这里什么都没有-"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LazyColumnEmptyBox(
    height: Dp,
    state: String,
    content: @Composable BoxScope.() -> Unit = { Text(text = state) }
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        contentAlignment = Alignment.Center,
        content = content
    )
}


@Composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
fun PullToRefreshViewModelSample() {
    val viewModel = remember {
        object : ViewModel() {
            private val refreshRequests = Channel<Unit>(1)
            var isRefreshing by mutableStateOf(false)
                private set

            var itemCount by mutableStateOf(15)
                private set

            init {
                viewModelScope.launch {
                    for (r in refreshRequests) {
                        isRefreshing = true
                        try {
                            itemCount += 5
                            delay(5000) // simulate doing real work
                        } finally {
                            isRefreshing = false
                        }
                    }
                }
            }

            fun refresh() {
                refreshRequests.trySend(Unit)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Title") },
                // Provide an accessible alternative to trigger refresh.
                actions = {
                    IconButton(
                        enabled = !viewModel.isRefreshing,
                        onClick = { viewModel.refresh() }
                    ) {
                        Icon(Icons.Filled.Refresh, "Trigger Refresh")
                    }
                }
            )
        }
    ) {
        PullToRefreshBox(
            modifier = Modifier.padding(it),
            isRefreshing = viewModel.isRefreshing,
            onRefresh = { viewModel.refresh() }
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                if (!viewModel.isRefreshing) {
                    items(viewModel.itemCount) {
                        ListItem({ Text(text = "Item ${viewModel.itemCount - it}") })
                    }
                }
            }
        }
    }
}

@Composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
fun PullToRefreshScalingSample() {
    var itemCount by remember { mutableStateOf(15) }
    var isRefreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            // fetch something
            delay(1500)
            itemCount += 5
            isRefreshing = false
        }
    }

    val scaleFraction = {
        if (isRefreshing) 1f
        else LinearOutSlowInEasing.transform(state.distanceFraction).coerceIn(0f, 1f)
    }

    Scaffold(
        modifier =
        Modifier.pullToRefresh(
            state = state,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh
        ),
        topBar = {
            TopAppBar(
                title = { Text("TopAppBar") },
                // Provide an accessible alternative to trigger refresh.
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Filled.Refresh, "Trigger Refresh")
                    }
                }
            )
        }
    ) {
        Box(Modifier.padding(it)) {
            LazyColumn(Modifier.fillMaxSize()) {
                if (!isRefreshing) {
                    items(itemCount) { ListItem({ Text(text = "Item ${itemCount - it}") }) }
                }
            }
            Box(
                Modifier
                    .align(Alignment.TopCenter)
                    .graphicsLayer {
                        scaleX = scaleFraction()
                        scaleY = scaleFraction()
                    }
            ) {
                PullToRefreshDefaults.Indicator(state = state, isRefreshing = isRefreshing)
            }
        }
    }
}


@Composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
fun PullToRefreshLinearProgressIndicatorSample() {
    var itemCount by remember { mutableIntStateOf(15) }
    var isRefreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            // fetch something
            delay(1500)
            itemCount += 5
            isRefreshing = false
        }
    }

    Scaffold(
        modifier =
        Modifier.pullToRefresh(
            state = state,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh
        ),
        topBar = {
            TopAppBar(
                title = { Text("TopAppBar") },
                // Provide an accessible alternative to trigger refresh.
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Filled.Refresh, "Trigger Refresh")
                    }
                }
            )
        }
    ) {
        Box(Modifier.padding(it)) {
            LazyColumn(Modifier.fillMaxSize()) {
                if (!isRefreshing) {
                    items(itemCount) { ListItem({ Text(text = "Item ${itemCount - it}") }) }
                }
            }
            if (isRefreshing) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            } else {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = { state.distanceFraction }
                )
            }
        }
    }
}

@Composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
fun PullToRefreshSampleCustomState() {
    var itemCount by remember { mutableIntStateOf(15) }
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            // fetch something
            delay(1500)
            itemCount += 5
            isRefreshing = false
        }
    }

    val state = remember {
        object : PullToRefreshState {
            private val anim = Animatable(0f, Float.VectorConverter)

            override val distanceFraction
                get() = anim.value

            override suspend fun animateToThreshold() {
                anim.animateTo(1f, spring(dampingRatio = Spring.DampingRatioHighBouncy))
            }

            override suspend fun animateToHidden() {
                anim.animateTo(0f)
            }

            override suspend fun snapTo(targetValue: Float) {
                anim.snapTo(targetValue)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TopAppBar") },
                // Provide an accessible alternative to trigger refresh.
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Filled.Refresh, "Trigger Refresh")
                    }
                }
            )
        }
    ) {
        PullToRefreshBox(
            modifier = Modifier.padding(it),
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = state
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                if (!isRefreshing) {
                    items(itemCount) { ListItem({ Text(text = "Item ${itemCount - it}") }) }
                }
            }
        }
    }
}

