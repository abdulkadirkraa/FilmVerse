package com.abdulkadirkara.filmverse.presentation.screens.screencard

data class ScreenCardUIData(
    val cartId: List<Int>,
    val name: String,
    val image: String,
    val category: String,
    val price: Int,
    val orderAmount: Int,
    val isChecked: Boolean = true
)