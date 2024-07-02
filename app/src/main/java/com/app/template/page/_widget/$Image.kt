package com.app.template.page._widget

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import com.app.base.SubComposeAsyncImage
import com.ocnyang.compose_loading.InstaSpinner

/**
 * 图片显示的 Compose 组合项
 */
@Composable
fun ImageX(
    model: Any?,
    contentDescription: String? = model?.toString(),
    modifier: Modifier = Modifier,
    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = AsyncImagePainter.DefaultTransform,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
    animatedLoadingDurationMillis: Int = 700,
) {
    SubComposeAsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        transform = transform,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        filterQuality = filterQuality,
        animatedLoadingDurationMillis = animatedLoadingDurationMillis,
        emptyStateContent = { Text(text = "图片地址错误", Modifier.align(Alignment.Center)) },// todo 定制成自己想要的样子
        errorStateContent = { Text(text = "Error", Modifier.align(Alignment.Center)) },// todo
        animatedLoadingStateContent = {
            InstaSpinner(size = 25.dp) // todo
        }
    )
}