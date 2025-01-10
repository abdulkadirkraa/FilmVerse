package com.abdulkadirkara.filmverse.presentation.screens.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import coil3.compose.AsyncImage
import com.abdulkadirkara.common.constant.ApiImageConstant

/**
 * A composable function to display an image from a URL using Coil's AsyncImage.
 * This function allows customization of image size, content scale, and content description.
 *
 * @param modifier A [Modifier] that can be applied to the AsyncImage. Defaults to an empty modifier.
 * @param imageUrl The URL of the image to be displayed. The image is fetched using Coil.
 * @param contentDescription A string that provides a description of the image for accessibility. Defaults to an empty string.
 * @param contentScale Defines how the content should scale to fit the bounds of the Image.
 *        Defaults to [ContentScale.Fit], which scales the image to fit within the bounds while preserving its aspect ratio.
 * @param imageSize A nullable [DpSize] that defines the width and height of the image.
 *        If provided, the image size will be adjusted according to the specified width and height.
 *        If null, the modifier will be used without size constraints.
 */
@Composable
fun CustomImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDescription: String = "",
    contentScale: ContentScale = ContentScale.Fit,
    imageSize: DpSize? = null
) {
    AsyncImage(
        // Construct the complete URL using a base URL constant and the provided image URL
        model = ApiImageConstant.IMAGE_BASE_URL + imageUrl,
        contentDescription = contentDescription,
        // Apply the modifier with optional size constraints if imageSize is provided
        modifier = (imageSize?.let {
            modifier.size(it.width, it.height) // If imageSize is provided, set the image size
        } ?: modifier), // Otherwise, use the original modifier without size constraints
        // Define how the content should scale within the available space
        contentScale = contentScale
    )
}
