package com.abdulkadirkara.filmverse.presentation.screens.screencard

/**
 * A data class that represents the data model for a UI card in the screen.
 * This class holds the properties that describe the content of the card, such as product details, price,
 * order amount, and whether the card is checked or not.
 * The data is derived from an API response, where:
 * - `cartId` is a list of combined IDs based on the same movie names from the API response.
 * - `orderAmount` represents the total quantity of the same movie, with values summed from the API response.
 * Other attributes are populated with the first corresponding item's details from the API response.
 *
 * @param cartId A list of combined IDs based on the same movie names, derived from the API response.
 *               These IDs correspond to the movie entries that have the same name in the API response.
 * @param name The name of the movie or item displayed in the card. This is taken from the first item
 *             in the API response for movies with the same name.
 * @param image The URL or path to the image representing the movie or product. This is taken from the
 *              first item's image data in the API response.
 * @param category The category the movie or product belongs to (e.g., Movies, TV Shows, etc.),
 *                 derived from the first item in the API response.
 * @param price The price of the product or movie. This value is taken from the first item in the API
 *              response that matches the movie's name.
 * @param orderAmount The total order quantity for the movie or product, which is the sum of the
 *                    quantities of all items with the same movie name in the API response.
 * @param isChecked A boolean flag indicating whether the card is selected/checked. This value is used
 *                  to determine if the item has been selected for removal when confirming the cart.
 *                  If `true`, the item will be included in the group of items to be deleted. The default
 *                  value is `true`.
 */
data class ScreenCardUIData(
    val cartId: List<Int>,
    val name: String,
    val image: String,
    val category: String,
    val price: Int,
    val orderAmount: Int,
    val isChecked: Boolean = true
)