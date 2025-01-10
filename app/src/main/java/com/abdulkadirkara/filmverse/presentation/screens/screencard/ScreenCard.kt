package com.abdulkadirkara.filmverse.presentation.screens.screencard

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abdulkadirkara.filmverse.presentation.screens.components.CustomImage
import com.abdulkadirkara.filmverse.presentation.screens.components.LoadingComponent

/**
 * Composable function representing the screen card UI.
 * Displays a list of movie items in the cart and provides options to manage cart items.
 *
 * @param viewModel The view model used to fetch and manage the cart data.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenCard(
    viewModel: ScreenCardViewModel = hiltViewModel()
) {
    // Observing product count and movie card state from the ViewModel
    val productCount by viewModel.productCount.observeAsState(0)
    val movieCardState by viewModel.movieCardState.observeAsState(CardUIState.Loading)

    // Remembering selected states of the items in the cart
    val selectedStates = remember { mutableStateMapOf<Int, Boolean>() }

    // Context for showing Toast messages
    val context = LocalContext.current

    // Initialize selected states for each item when the data is loaded
    LaunchedEffect(movieCardState) {
        if (movieCardState is CardUIState.Success) {
            val movieList = (movieCardState as CardUIState.Success<List<ScreenCardUIData>>).data
            movieList.forEachIndexed { index, _ ->
                selectedStates[index] = true
            }
        }
    }

    // Calculating the total price for the selected items
    val totalPrice by remember {
        derivedStateOf {
            if (movieCardState is CardUIState.Success) {
                calculateTotalPrice(
                    movieList = (movieCardState as CardUIState.Success<List<ScreenCardUIData>>).data,
                    selectedStates = selectedStates
                )
            } else {
                0
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Sepetim - $productCount ürün") }
            )
        },
        bottomBar = {
            // Bottom bar displaying the total price and an option to delete selected movies
            BottomBar(
                totalPrice = totalPrice,
                selectedStates = selectedStates,
                movieCardState = movieCardState,
                viewModel = viewModel
            )
        }
    ) { paddingValues ->
        when (movieCardState) {
            is CardUIState.Empty -> {
                Toast.makeText(context, "Sepetinizde ürün bulunmamaktadır", Toast.LENGTH_SHORT).show()
            }
            is CardUIState.Error -> {
                val errorMessage = (movieCardState as CardUIState.Error).message
                Log.e("ScreenCard", "Hata: $errorMessage")
            }
            CardUIState.Loading -> {
                LoadingComponent()
            }
            is CardUIState.Success -> {
                val movieList = (movieCardState as CardUIState.Success<List<ScreenCardUIData>>).data
                MovieStateSuccess(
                    paddingValues = paddingValues,
                    movieList = movieList,
                    viewModel = viewModel,
                    selectedStates = selectedStates
                )
            }
        }
    }
}

/**
 * Composable function representing the bottom bar UI.
 * Displays the total price and a button to delete selected items from the cart.
 *
 * @param totalPrice The calculated total price of the selected items in the cart.
 * @param selectedStates A map of selected states for each item in the cart.
 * @param movieCardState The current state of the movie card data.
 * @param viewModel The view model used to manage the cart data and handle item deletion.
 */
@Composable
fun BottomBar(
    totalPrice: Int,
    selectedStates: MutableMap<Int, Boolean>,
    movieCardState: CardUIState<List<ScreenCardUIData>>,
    viewModel: ScreenCardViewModel
) {
    if (movieCardState is CardUIState.Success) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Displaying the total price section
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(50f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Toplam",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "$totalPrice ₺",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color(0xFF0D47A1)
                )
            }
            // A card button to delete selected items from the cart
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(50f)
                    .clickable {
                        // Triggering deletion of selected items when clicked
                        viewModel.deleteSelectedMovies(selectedStates = selectedStates)
                        viewModel.updateCartItemCount(newCount = -movieCardState.data.sumOf { it.orderAmount })
                    },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0D47A1)
                )
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = "Sepete Ekle",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        }
    }
}

/**
 * Helper function to calculate the total price of the selected items in the cart.
 *
 * @param movieList A list of movies in the cart.
 * @param selectedStates A map of selected states for each movie item.
 * @return The total price of the selected items in the cart.
 */
private fun calculateTotalPrice(
    movieList: List<ScreenCardUIData>,
    selectedStates: MutableMap<Int, Boolean>
): Int {
    var totalPrice = 0
    movieList.forEachIndexed { index, movie ->
        if (selectedStates[index] == true) {
            totalPrice += movie.price * movie.orderAmount
        }
    }
    return totalPrice
}

/**
 * Displays a list of movies in the shopping cart with their details.
 * Allows users to select or deselect movies and delete them from the cart.
 *
 * @param paddingValues The padding values to be applied to the LazyColumn.
 * @param movieList The list of movies to display in the cart.
 * @param viewModel The ViewModel responsible for managing cart data and performing actions.
 * @param selectedStates A map to track which movies are selected in the cart.
 */
@Composable
fun MovieStateSuccess(
    paddingValues: PaddingValues,
    movieList: List<ScreenCardUIData>,
    viewModel: ScreenCardViewModel,
    selectedStates: MutableMap<Int, Boolean>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        itemsIndexed(movieList) { index, movie ->
            val isChecked = selectedStates[index] ?: true
            MovieCardItem(
                movie = movie,
                isChecked = isChecked,
                onCheckedChange = { isSelected ->
                    selectedStates[index] = isSelected // Update the selected state when checkbox is toggled
                },
                onDelete = {
                    viewModel.deleteMovieCard(cartIds = movie.cartId) // Delete the selected movie from the cart
                    viewModel.updateCartItemCount(newCount = -movie.orderAmount) // Update the cart item count after deletion
                }
            )
        }
    }
}

/**
 * Displays a single movie item in the cart with an option to delete it or change its selection status.
 *
 * @param movie The movie data to display.
 * @param isChecked Indicates whether the movie is selected or not.
 * @param onCheckedChange A callback to handle changes in the selection state.
 * @param onDelete A callback to delete the movie from the cart.
 */
@Composable
fun MovieCardItem(
    movie: ScreenCardUIData,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) } // State to control the display of the confirmation dialog.

    // Display an AlertDialog for confirmation before deleting a movie from the cart.
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onDelete() // Call the onDelete callback if the user confirms the deletion
                }) {
                    Text(text = "Evet")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = "Hayır")
                }
            },
            title = { Text("Silme İşlemi") },
            text = { Text("${movie.name} adlı ürünü silmek istediğinizden emin misiniz?") }
        )
    }

    // Display the movie's details inside a card view.
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Checkbox to select/deselect the movie in the cart.
            Checkbox(
                checked = isChecked,
                onCheckedChange = { onCheckedChange(it) },
                modifier = Modifier.weight(0.1f)
            )

            CustomImage(
                imageUrl = movie.image,
                modifier = Modifier.weight(0.2f),
                imageSize = DpSize(80.dp, 80.dp)
            )

            // Display movie details such as name, category, price, and order amount.
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(0.5f)
            ) {
                Text(
                    text = movie.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = movie.category,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "${movie.price}₺",
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "${movie.orderAmount} adet",
                    fontSize = 14.sp,
                    color = Color.Gray,
                )
            }

            // Display the delete button and the total price for the movie.
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(0.2f)
                    .padding(start = 8.dp)
            ) {
                // Button to trigger the confirmation dialog for deleting the movie.
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Filled.Delete, contentDescription = null)
                }
                // Display the total price for the selected quantity of the movie.
                Text(
                    text = "${(movie.price * movie.orderAmount)}₺",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}