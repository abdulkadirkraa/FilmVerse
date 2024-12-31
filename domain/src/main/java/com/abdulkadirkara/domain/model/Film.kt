package com.abdulkadirkara.domain.model

data class Film(
    val category: String,
    val description: String,
    val director: String,
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val rating: Double,
    val year: Int
)
