package com.abdulkadirkara.domain.model

data class FilmEntityModel(
    val id: Int,
    val category: String,
    val description: String,
    val director: String,
    val imagePath: String,
    val name: String,
    val price: Int,
    val rating: Double,
    val year: Int
)