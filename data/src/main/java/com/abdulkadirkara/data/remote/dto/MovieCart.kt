package com.abdulkadirkara.data.remote.dto

data class MovieCart(
    val cartId: Int,
    val category: String,
    val description: String,
    val director: String,
    val image: String,
    val name: String,
    val orderAmount: Int,
    val price: Int,
    val rating: Int,
    val userName: String,
    val year: Int
)