package com.abdulkadirkara.domain.model

data class FilmCardEntity(
    val id: Int,
    val image: String,
    val name: String,
    val category: String,
    val rating: Double,
    val price: Int,
    val description: String,
    val year: Int,
    val director: String,
    val isFavorite: Boolean,
    val campaign: Campaign? = null,
    val cartState: CartState // Sepetteki durum
)

data class Campaign(
    val description: String,
    val backgroundColor: String
)

enum class CartState {
    NOT_IN_CART, // Henüz sepete eklenmedi
    IN_CART_ONE, // Sepette 1 adet
    IN_CART_MULTIPLE // Sepette birden fazla
}
