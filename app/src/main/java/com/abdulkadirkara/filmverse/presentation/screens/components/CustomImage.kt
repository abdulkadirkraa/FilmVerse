package com.abdulkadirkara.filmverse.presentation.screens.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import coil3.compose.AsyncImage
import com.abdulkadirkara.common.constant.ApiImageConstant

@Composable
fun CustomImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDescription: String = "",
    contentScale: ContentScale = ContentScale.Fit,
    imageSize: DpSize? = null
) {
    AsyncImage(
        model = ApiImageConstant.IMAGE_BASE_URL + imageUrl,
        contentDescription = contentDescription,
        modifier = (imageSize?.let {
            modifier.size(it.width, it.height)
        } ?: modifier),
        contentScale = contentScale
    )
}
