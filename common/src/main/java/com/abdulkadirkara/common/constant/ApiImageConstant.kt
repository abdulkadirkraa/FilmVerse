package com.abdulkadirkara.common.constant

object ApiImageConstant {
    /**
     * The base URL for fetching images related to movies from the API.
     *
     * This URL is the root path used for building full image URLs. It is prepended
     * to the specific image paths provided by the API to fetch movie-related images
     * such as posters or backdrops.
     *
     * Example of how to use:
     * ```
     * val fullImageUrl = ApiImageConstant.IMAGE_BASE_URL + imagePath
     * ```
     */
    const val IMAGE_BASE_URL = "http://kasimadalan.pe.hu/movies/images/"
}