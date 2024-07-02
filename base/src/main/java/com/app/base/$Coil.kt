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

private const val EMPTY: Byte = -1
private const val ERROR: Byte = -2
private const val LOADING: Byte = -3
private const val SUCCESS: Byte = 1
private const val NONE: Byte = 0

/**
 * 图片加载函数的统一封装
 */
@Composable
fun SubComposeAsyncImage(
    model: Any?,
    contentDescription: String? = model?.toString(),
    modifier: Modifier = Modifier,
    state: MutableState<Byte> = rememberSaveable {
        mutableStateOf(NONE)
    },
    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = AsyncImagePainter.DefaultTransform,
    onState: ((AsyncImagePainter.State) -> Unit)? = {
        state.value = when (it) {
            AsyncImagePainter.State.Empty -> EMPTY
            is AsyncImagePainter.State.Error -> ERROR
            is AsyncImagePainter.State.Loading -> LOADING
            is AsyncImagePainter.State.Success -> SUCCESS
        }
    },
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    animatedLoadingDurationMillis: Int = 700,
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
            EMPTY -> emptyStateContent.invoke(this@SubcomposeAsyncImage)
            ERROR -> errorStateContent.invoke(this@SubcomposeAsyncImage)
            LOADING -> loadingStateContent.invoke(this@SubcomposeAsyncImage)
            SUCCESS -> this@SubcomposeAsyncImage.SubcomposeAsyncImageContent(Modifier.align(Alignment.Center))
            else -> placeholderContent.invoke(this@SubcomposeAsyncImage)
        }
        Column(Modifier.matchParentSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedVisibility(
                visible = state.value == LOADING,
                exit = fadeOut(animationSpec = keyframes { durationMillis = animatedLoadingDurationMillis }),
                content = animatedLoadingStateContent
            )
        }
    }
}