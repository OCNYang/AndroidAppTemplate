package com.app.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.SubcomposeAsyncImageScope

@Composable
fun SubComposeAsyncImage(
    model: Any?,
    contentDescription: String? = model?.toString(),
    modifier: Modifier = Modifier,
    state: MutableState<Int> = rememberSaveable { // 这里变为数值类型
        mutableStateOf(0)
    },
    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = AsyncImagePainter.DefaultTransform,
    onState: ((AsyncImagePainter.State) -> Unit)? = {
        state.value = when (it) {
            AsyncImagePainter.State.Empty -> -1
            is AsyncImagePainter.State.Error -> -2
            is AsyncImagePainter.State.Loading -> -3
            is AsyncImagePainter.State.Success -> 1
        }
    },
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    emptyStateContent: @Composable SubcomposeAsyncImageScope.() -> Unit,
    errorStateContent: @Composable SubcomposeAsyncImageScope.() -> Unit,
    loadingStateContent: @Composable SubcomposeAsyncImageScope.() -> Unit = {},
    animatedLoadingStateContent: @Composable AnimatedVisibilityScope.() -> Unit,
    placeholderContent: @Composable SubcomposeAsyncImageScope.() -> Unit = {
        SubcomposeAsyncImageContent(Modifier.align(Alignment.Center))
    },
) = SubcomposeAsyncImage(
    model = model,
    contentDescription = contentDescription,
    modifier = modifier,
    transform = transform,
    onState = onState,
    alignment = alignment,
    contentScale = contentScale,
    alpha = alpha,
    colorFilter = colorFilter,
    filterQuality = filterQuality,
) {
    Box(modifier = Modifier.matchParentSize()) {
        when (state.value) {
            -1 -> emptyStateContent.invoke(this@SubcomposeAsyncImage)
            -2 -> errorStateContent.invoke(this@SubcomposeAsyncImage)
            -3 -> loadingStateContent.invoke(this@SubcomposeAsyncImage)
            1 -> this@SubcomposeAsyncImage.SubcomposeAsyncImageContent(Modifier.align(Alignment.Center))
            else -> placeholderContent.invoke(this@SubcomposeAsyncImage)
        }
        Column(Modifier.matchParentSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedVisibility(
                visible = state.value == -3,
                exit = fadeOut(animationSpec = keyframes { durationMillis = 700 }),
                content = animatedLoadingStateContent
            )
        }
    }
}