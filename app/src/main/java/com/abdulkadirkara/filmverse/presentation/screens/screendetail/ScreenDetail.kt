package com.abdulkadirkara.filmverse.presentation.screens.screendetail

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.rounded.Favorite
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
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.domain.model.FilmCardEntity
import com.abdulkadirkara.filmverse.R
import com.abdulkadirkara.filmverse.presentation.navigation.Screens
import com.abdulkadirkara.filmverse.presentation.screens.components.CustomImage
import com.abdulkadirkara.filmverse.presentation.screens.components.ErrorComponent
import com.abdulkadirkara.filmverse.presentation.screens.components.topappbar.DetailScreenCustomTopAppBar

/**
 * A composable screen displaying the detailed information of a film.
 * It allows the user to view film details such as the image, category, rating, director, and description.
 * Users can also add the film to their cart and manage the quantity and price.
 *
 * @param animatedVisibilityScope The animated visibility scope for shared element transitions.
 * @param film The film data object containing the details of the film to display.
 * @param navController The navigation controller to manage screen navigation.
 * @param viewModel The ScreenDetailViewModel responsible for managing the film details, favorites, and cart.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ScreenDetail(
    animatedVisibilityScope: AnimatedVisibilityScope,
    film: FilmCardEntity,
    navController: NavController,
    viewModel: ScreenDetailViewModel = hiltViewModel()) {

    val isFavorite by viewModel.favoriteStatus.collectAsState()

    // The quantity of the item in the cart, initially set to 1
    var quantity by remember { mutableIntStateOf(1) }
    val totalPrice = remember { mutableIntStateOf(film.price * quantity) }

    // The current context, used for displaying Toast messages
    val context = LocalContext.current

    // The scroll state for the screen to allow scrolling if the content overflows
    val verticalState = rememberScrollState()

    val insertMovieResult by viewModel.insertMovieCardResult.observeAsState()
    val cartItemCount = viewModel.cartItemCount.collectAsState()

    // Triggering side effects when cart item count changes
    LaunchedEffect(key1 = cartItemCount.value) {
        viewModel.getCartItemCount()
    }

    // Triggering side effects when favorite status changes
    LaunchedEffect(isFavorite) {
        viewModel.toggleFavorite(film)
    }

    // Triggering side effects when a movie is inserted into the cart
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
            // Custom top app bar with shared element transition for film name
            DetailScreenCustomTopAppBar(
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState("title/${film.name}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 1000)
                        }
                    ),
                title = film.name,
                cartItemCount = cartItemCount.value,
                onBackClick = { navController.popBackStack() },
                goToCardScreen = { navController.navigate(Screens.ScreenCard.route) }
            )
        },
        bottomBar = {
            // Bottom bar containing quantity controls and "Add to Cart" button
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ){
                // Row for the quantity control (decrease, display, increase)
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

                // Row for displaying total price and "Add to Cart" button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Text displaying total price with shared element transition
                    Text(
                        text = "${totalPrice.intValue} TL",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(4.dp).weight(50f)
                            .sharedElement(
                                state = rememberSharedContentState("price/${film.price}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween(durationMillis = 1000)
                                }
                            )
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
        // Column displaying film details and additional sections
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(verticalState),
        ) {
            Row (
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                // Custom image with shared element transition for film image
                CustomImage(
                    imageUrl = film.image,
                    modifier = Modifier.weight(50f)
                        .sharedElement(
                            state = rememberSharedContentState("image/${film.image}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 1000)
                            }
                        ),
                    contentScale = ContentScale.FillHeight,
                    imageSize = DpSize(150.dp, 200.dp)
                )
                // Column displaying film details like category, rating, director, and description
                Column(
                    modifier = Modifier.fillMaxWidth().padding(4.dp).weight(50f),
                ) {
                    // Favorite button (click to toggle favorite status)
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(4.dp)
                            .background(Color.LightGray, CircleShape)
                            .align(Alignment.End)
                            .clickable {
                                viewModel.toggleFavorite(film)

                            }
                    ) {
                        Icon(
                            imageVector = if (film.isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
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
                    // Row displaying rating icon and rating value
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = Color.Yellow,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        // Shared element transition for the rating text
                        Text(
                            modifier = Modifier.sharedElement(
                                state = rememberSharedContentState("rating/${film.rating}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween(durationMillis = 1000)
                                }
                            ),
                            text = film.rating.toString(),
                            fontSize = 16.sp)
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

            // Spacer for visual separation between sections
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
                            .size(48.dp)
                            .padding(4.dp)
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

/**
 * A composable function that displays an animated counter.
 * The counter value is animated digit by digit whenever the `count` value changes.
 * Each digit transitions with a slide effect when the counter value is updated.
 *
 * @param count The current count to be displayed in the counter. This value is animated.
 * @param modifier A modifier to be applied to the `Row` layout.
 * @param style The text style for the digits. Defaults to `MaterialTheme.typography.titleLarge`.
 */
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