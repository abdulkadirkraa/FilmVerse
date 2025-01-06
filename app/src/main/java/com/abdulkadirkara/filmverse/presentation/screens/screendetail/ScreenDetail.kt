package com.abdulkadirkara.filmverse.presentation.screens.screendetail

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.abdulkadirkara.common.constant.ApiImageConstant
import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.data.remote.ApiConstants
import com.abdulkadirkara.domain.model.FilmCardUI
import com.abdulkadirkara.filmverse.R
import com.abdulkadirkara.filmverse.presentation.navigation.Screens
import com.abdulkadirkara.filmverse.presentation.screens.components.topappbar.DetailScreenCustomTopAppBar

@Composable
fun ScreenDetail(film: FilmCardUI, navController: NavController, viewModel: ScreenDetailViewModel = hiltViewModel()) {
    var quantity by remember { mutableIntStateOf(1) }
    val totalPrice = remember { mutableIntStateOf(film.price * quantity) }
    val context = LocalContext.current

    val insertMovieResult by viewModel.insertMovieCardResult.observeAsState()
    val cartItemCount = viewModel.cartItemCount.collectAsState()

    LaunchedEffect(key1 = cartItemCount.value) {
        viewModel.getCartItemCount()
    }

    LaunchedEffect(insertMovieResult) {
        insertMovieResult?.let { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    val newCount = cartItemCount.value + quantity
                    viewModel.updateCartItemCount(newCount)
                }
                is NetworkResponse.Error -> {
                    Log.e("ScreenDetail", "Hata: response")
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            DetailScreenCustomTopAppBar(
                title = film.name,
                cartItemCount = cartItemCount.value,
                onBackClick = { navController.popBackStack() },
                goToCardScreen = { navController.navigate(Screens.ScreenCard.route) }
            )
        },
        bottomBar = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                if (quantity > 1) {
                                    quantity--
                                    totalPrice.intValue = film.price * quantity
                                }else {
                                    Toast.makeText(context, "En az bir adet seçili olmalı", Toast.LENGTH_SHORT).show()
                                }
                            },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF0D47A1),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_minus_48),
                            contentDescription = "Eksilt",
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    AnimatedCounter(count = quantity)
                    Spacer(modifier = Modifier.width(16.dp))
                    Card(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                quantity++
                                totalPrice.intValue = film.price * quantity
                            },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF0D47A1),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Arttır",
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                // Fiyat Bilgisi ve Sepete Ekle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${totalPrice.intValue} TL",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(4.dp).weight(50f)
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(50f)
                            .clickable {
                                viewModel.insertMovieCard(
                                    name = film.name,
                                    image = film.image,
                                    price = film.price,
                                    category = film.category,
                                    rating = film.rating,
                                    year = film.year,
                                    director = film.director,
                                    description = film.description,
                                    orderAmount = quantity
                                )
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
    ) { paddingValues ->
        val verticalState = rememberScrollState()
        Column(
            modifier = Modifier
                .verticalScroll(verticalState)
                .fillMaxWidth()
                .padding(top = paddingValues.calculateTopPadding(), bottom = 16.dp),
        ) {
            Row (
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                val imageUrl = ApiImageConstant.IMAGE_BASE_URL + film.image
                AsyncImage(
                    model = imageUrl,
                    modifier = Modifier
                        .width(150.dp)
                        .height(200.dp)
                        .weight(50f),
                    contentDescription = "Film Görseli",
                    contentScale = ContentScale.FillHeight,
                )
                Column(
                    modifier = Modifier.fillMaxWidth().padding(4.dp).weight(50f),
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp) // Daha büyük bir arka plan
                            .padding(4.dp) // Kartın köşelerinden uzaklaştırmak için padding ekledik
                            .background(Color.LightGray, CircleShape)
                            .align(Alignment.End)
                    ) {
                        Icon(
                            Icons.Rounded.FavoriteBorder,
                            contentDescription = "Favori",
                            tint = if (film.isFavorite) Color.Red else Color.White,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                    Text(
                        text = film.category,
                        fontSize = 20.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = Color.Yellow,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = film.rating.toString(), fontSize = 16.sp)
                    }
                    Text(
                        text = film.director,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = film.description,
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )
            Text(
                text = "Ürünün Kampanyaları",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(bottom = 8.dp, top = 8.dp, start = 12.dp)
                    .background(Color.White)
            )
            HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp) // Daha büyük bir arka plan
                            .padding(4.dp) // Kartın köşelerinden uzaklaştırmak için padding ekledik
                            .background(Color(0xFF0D47A1), RoundedCornerShape(10.dp))
                    ) {
                        Icon(
                            Icons.Rounded.Campaign,
                            contentDescription = "Kampanya",
                            tint = Color.White,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                    Text(
                        text = "5 TL Kapmanya",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(4.dp)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                        contentDescription = "İleri",
                        tint = Color.Black
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedCard(
                    modifier = Modifier
                        .padding(4.dp),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = Color(0xFF0D47A1),
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
                Text(
                    text = "Listeye Ekle",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )
        }

    }
}

@Composable
fun AnimatedCounter(
    count: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.titleLarge
){
    var oldCOunt by remember {
        mutableIntStateOf(count)
    }
    SideEffect {
        oldCOunt = count
    }
    Row(modifier = modifier) {
        val countString = count.toString()
        val oldCountString = oldCOunt.toString()
        for (i in countString.indices){
            val oldChar = oldCountString.getOrNull(i)
            val newChar = countString[i]
            val char = if (oldChar == newChar){
                oldCountString[i]
            }else{
                countString[i]
            }
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInVertically { it } togetherWith slideOutVertically { -it }
                }, label = ""
            ) { c ->
                Text(
                    text = c.toString(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    style = style,
                    softWrap = false
                )
            }
        }
    }
}