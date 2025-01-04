package com.abdulkadirkara.filmverse.presentation.screens.screencard

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.abdulkadirkara.data.remote.ApiConstants
import com.abdulkadirkara.domain.model.FilmCardItem
import com.abdulkadirkara.filmverse.R
import com.abdulkadirkara.filmverse.presentation.screens.components.LoadingComponent
import com.abdulkadirkara.filmverse.ui.theme.babasNeue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenCard(
    viewModel: ScreenCardViewModel = hiltViewModel()
){
    val productCount by viewModel.productCount.observeAsState(0)
    val movieCardState by viewModel.movieCardState.observeAsState(CardUIState.Loading)
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sepetim - $productCount ürün") }
            )
        }
    ) { paddingValues ->

        when(movieCardState){
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
                val movieList = (movieCardState as CardUIState.Success<List<FilmCardItem>>).data
                MovieStateSuccess(paddingValues, movieList)
            }
        }
    }
}

@Composable
fun MovieStateSuccess(paddingValues: PaddingValues, movieList: List<FilmCardItem>){
    val selectedStates = remember { mutableStateMapOf<Int, Boolean>() }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        item {
            //placeholder expandable sepete özel indirimi ekle
        }
        itemsIndexed(movieList) { index, movie ->
            // Varsayılan olarak seçili olmayan bir durum belirliyoruz.
            val isChecked = selectedStates[index] ?: false

            MovieCardItem(
                movie = movie,
                isChecked = isChecked,
                onCheckedChange = { isSelected ->
                    selectedStates[index] = isSelected
                }
            )
        }

    }
}

@Composable
fun MovieCardItem(
    movie: FilmCardItem,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
){
    OutlinedCard (
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(10.dp),
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { onCheckedChange(it) }
            )
            val imageUrl = ApiConstants.BASE_URL + movie.image
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
            Column (
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = movie.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = movie.category,
                    fontSize = 20.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "${movie.price}₺",
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "${movie.orderAmount} adet",
                    fontSize = 20.sp,
                    color = Color.Gray,
                )
            }
            Column (
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //del icon ve totalprice
                OutlinedCard(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        contentColor = Color(0xFF0D47A1),
                    )
                ) {
                    IconButton(onClick = {  }) {
                        Icon(Icons.Filled.Delete, contentDescription = "")
                    }
                }

                Text(
                    text = "${(movie.price * movie.orderAmount)}₺",
                    fontSize = 24.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = babasNeue
                )
            }
        }
    }
}