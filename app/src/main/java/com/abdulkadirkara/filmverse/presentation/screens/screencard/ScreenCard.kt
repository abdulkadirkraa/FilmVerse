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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.abdulkadirkara.common.constant.ApiImageConstant
import com.abdulkadirkara.data.remote.ApiConstants
import com.abdulkadirkara.filmverse.presentation.screens.components.LoadingComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenCard(
    viewModel: ScreenCardViewModel = hiltViewModel()
) {
    val productCount by viewModel.productCount.observeAsState(0)
    val movieCardState by viewModel.movieCardState.observeAsState(CardUIState.Loading)
    val selectedStates = remember { mutableStateMapOf<Int, Boolean>() }
    val context = LocalContext.current

    LaunchedEffect(movieCardState) {
        if (movieCardState is CardUIState.Success) {
            val movieList = (movieCardState as CardUIState.Success<List<ScreenCardUIData>>).data
            movieList.forEachIndexed { index, _ ->
                selectedStates[index] = true
            }
        }
    }

    val totalPrice by remember {
        derivedStateOf {
            if (movieCardState is CardUIState.Success) {
                calculateTotalPrice(
                    (movieCardState as CardUIState.Success<List<ScreenCardUIData>>).data,
                    selectedStates
                )
            } else {
                0
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sepetim - $productCount ürün") }
            )
        },
        bottomBar = {
            BottomBar(totalPrice, selectedStates, movieCardState, viewModel)
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
                MovieStateSuccess(paddingValues, movieList, viewModel, selectedStates)
            }
        }
    }
}

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
            Column (
                modifier = Modifier.fillMaxWidth().weight(50f).padding(8.dp),
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(50f)
                    .clickable {
                        viewModel.deleteSelectedMovies(selectedStates)
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
        item {
            // Sepete özel indirim placeholder
        }
        itemsIndexed(movieList) { index, movie ->
            val isChecked = selectedStates[index] ?: true
            MovieCardItem(
                movie = movie,
                isChecked = isChecked,
                onCheckedChange = { isSelected ->
                    selectedStates[index] = isSelected
                },
                onDelete = { viewModel.deleteMovieCard(movie.cartId, ApiConstants.USER_NAME) }
            )
        }
    }
}

@Composable
fun MovieCardItem(
    movie: ScreenCardUIData,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onDelete()
                }) {
                    Text("Evet")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Hayır")
                }
            },
            title = { Text("Silme İşlemi") },
            text = { Text("${movie.name} adlı ürünü silmek istediğinizden emin misiniz?") }
        )
    }

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
            Checkbox(
                checked = isChecked,
                onCheckedChange = { onCheckedChange(it) },
                modifier = Modifier.weight(0.1f)
            )

            // Image
            AsyncImage(
                model = ApiImageConstant.IMAGE_BASE_URL + movie.image,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .weight(0.2f)
            )

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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(0.2f)
                    .padding(start = 8.dp)
            ) {
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Filled.Delete, contentDescription = null)
                }
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