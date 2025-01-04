package com.abdulkadirkara.data.remote.dto.filmCard

import com.google.gson.annotations.SerializedName

data class FilmCardResponse(
    @SerializedName("movie_cart")
    val filmCards: List<FilmCard>
)
