package com.abdulkadirkara.filmverse.presentation.screens.screenhome

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.abdulkadirkara.data.remote.ApiConstants
import com.abdulkadirkara.domain.model.CartState
import com.abdulkadirkara.domain.model.FilmCardUI
import com.abdulkadirkara.domain.model.FilmCategoryUI
import com.abdulkadirkara.domain.model.FilmImageUI
import com.abdulkadirkara.filmverse.R
import com.abdulkadirkara.filmverse.presentation.screens.components.CustomTopAppBar
import com.abdulkadirkara.filmverse.presentation.screens.components.ErrorComponent
import com.abdulkadirkara.filmverse.presentation.screens.components.LoadingComponent
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenHome(
    viewModel: ScreenHomeViewModel = hiltViewModel()
) {
    val imageState = viewModel.imageState.observeAsState(HomeUIState.Loading)
    val categoryState = viewModel.categoryState.observeAsState(HomeUIState.Loading)
    val selectedCategory = viewModel.selectedCategory.observeAsState().value
    val movieState = viewModel.movieState.observeAsState(HomeUIState.Loading)

    Scaffold(
        topBar = {
            CustomTopAppBar()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            //image state'ine bakar viewpager yapıyı çağırır
            HomeScreenImageState(imageState)
            //categori state'ine bakarak card yapısını çağırır
            HomeScreenCategoryState(categoryState, viewModel, selectedCategory)
            //HomeScreenMovies
            HomeScreenMoviesState(movieState)
        }
    }
}

@Composable
fun HomeScreenImageState(imageState: State<HomeUIState<List<FilmImageUI>>>) {
    when (imageState.value) {
        is HomeUIState.Empty -> {
            //boşşsa diye uygun bir lottie animasyon ile component kullanılabilir
            LoadingComponent()
        }

        is HomeUIState.Error -> {
            val errorMessage = (imageState.value as HomeUIState.Error).message
            ErrorComponent(errorMessage) {
                // Dialog kapatıldığında yapılacak işlemler
                //Burda da bir lottie animasyonu döndüreibliriz
            }
        }

        HomeUIState.Loading -> {
            LoadingComponent()
        }

        is HomeUIState.Success -> {
            val imageList = (imageState.value as HomeUIState.Success<List<FilmImageUI>>).data
            HomeScreenViewPager(imageList)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreenViewPager(imageList: List<FilmImageUI>) {
    val pagerState = rememberPagerState(pageCount = imageList.size)
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % imageList.size)
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp), // Yüksekliği artırabilirsiniz
            verticalAlignment = Alignment.CenterVertically // Sayfaları dikeyde ortala
        ) { currentPage ->
            val url = ApiConstants.IMAGE_BASE_URL + imageList[currentPage].image
            // Resim kartı
            Card(
                modifier = Modifier
                    .fillMaxSize() // Orijinal genişlik 200, iki katına çıkarıyoruz
                //.height(300.dp) // Orijinal yükseklik 300, iki katına çıkarıyoruz
                //.padding(8.dp),
                ,
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                AsyncImage(
                    model = url,
                    contentDescription = "Film Posteri",
                    contentScale = ContentScale.FillBounds, // Resmi kesmeden ölçekle
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun HomeScreenCategoryState(
    categoryState: State<HomeUIState<List<FilmCategoryUI>>>,
    viewModel: ScreenHomeViewModel,
    selectedCategory: FilmCategoryUI?
) {
    when (categoryState.value) {
        is HomeUIState.Empty -> LoadingComponent()
        is HomeUIState.Error -> {
            val errorMessage = (categoryState.value as HomeUIState.Error).message
            ErrorComponent(errorMessage) {
                //Bir şeyler yapıalcak
            }
        }

        is HomeUIState.Loading -> LoadingComponent()
        is HomeUIState.Success -> {
            val categories = (categoryState.value as HomeUIState.Success<List<FilmCategoryUI>>).data
            val selectedCategory = categories.find { it.isClicked } ?: categories.first()
            CategoryList(
                categories = categories,
                selectedCategory = selectedCategory
            ) { selected ->
                viewModel.selectCategory(selected) // Kategori seçimi
            }
        }
    }
}

@Composable
fun CategoryList(
    categories: List<FilmCategoryUI>,
    selectedCategory: FilmCategoryUI,
    onCategorySelected: (FilmCategoryUI) -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        items(categories.size) { index ->
            val category = categories[index]
            val isSelected = category == selectedCategory

            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onCategorySelected(category) },
                shape = RoundedCornerShape(16.dp),
                border = if (isSelected) BorderStroke(2.dp, Color(0xFF007BFF)) else null,
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFFE3F2FD) else Color(0xFFF5F5F5),
                    contentColor = if (isSelected) Color(0xFF0D47A1) else Color(0xFF424242)
                ),
            ) {
                Text(
                    text = category.category,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    color = if (isSelected) Color(0xFF0D47A1) else Color(0xFF424242),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                )
            }
        }
    }
}

@Composable
fun HomeScreenMoviesState(movieState: State<HomeUIState<List<FilmCardUI>>>) {
    when (movieState.value) {
        is HomeUIState.Empty -> LoadingComponent()
        is HomeUIState.Error -> {
            val errorMessage = (movieState.value as HomeUIState.Error).message
            ErrorComponent(errorMessage) {
                //Bir şeyler yapıalcak
            }
        }

        is HomeUIState.Loading -> LoadingComponent()
        is HomeUIState.Success -> {
            val movies = (movieState.value as HomeUIState.Success<List<FilmCardUI>>).data
            HomeScreenMovies(movies, {
                //onFavoriteClick
            }, {
                //addToCartClick
            })
        }
    }
}

@Composable
fun HomeScreenMovies(
    films: List<FilmCardUI>,
    onFavoriteClick: (FilmCardUI) -> Unit,
    onAddToCartClick: (FilmCardUI) -> Unit
) {
    val filmState by remember { mutableStateOf(films) } // State ile filmler listesi

    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 sütunlu grid
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(filmState.size) { index ->
            val film = filmState[index]
            FilmCardItem(
                film = film,
                onFavoriteClick = { onFavoriteClick(it) },
                onAddToCartClick = { onAddToCartClick(it) }
            )
        }
    }
}

@Composable
fun FilmCardItem(
    film: FilmCardUI,
    onFavoriteClick: (FilmCardUI) -> Unit,
    onAddToCartClick: (FilmCardUI) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(400.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val imageUrl = ApiConstants.IMAGE_BASE_URL + film.image
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Film Görseli",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxSize()
                )

                // Favori butonu
                Box(
                    modifier = Modifier
                        .size(48.dp) // Daha büyük bir arka plan
                        .padding(8.dp) // Kartın köşelerinden uzaklaştırmak için padding ekledik
                        .background(Color.LightGray, CircleShape)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Rounded.FavoriteBorder,
                        contentDescription = "Favori",
                        tint = if (film.isFavorite) Color.Red else Color.White,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { onFavoriteClick(film) }
                    )
                }
            }

            if (film.id % 3 == 0){
                // Kampanya alanı
                if (film.campaign == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Magenta)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "5TL Kampanya",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Film bilgileri
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(
                    text = film.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
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
                    Text(text = film.rating.toString(), fontSize = 14.sp)
                }
                Text(
                    text = "${film.price} ₺",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                when (film.cartState) {
                    CartState.NOT_IN_CART -> {
                        CartStateNotInCartButton(){
                            //burda sepet ekleme api isteği ve loading ile cart durum değişimi
                        }
                    }
                    CartState.IN_CART_ONE -> {
                        CardStateInCartOneButton(){
                            //burda sepettekini silme veya arttırma api isteği ve loading ile cart durum değişimi
                        }
                    }
                    CartState.IN_CART_MULTIPLE -> {
                        CardStateInCartMultipleButton(){
                            //burda sepet ekleme veya silme api isteği ve loading ile cart durum değişimi
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartStateNotInCartButton(onCliked: () -> Unit){
    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0D47A1)
        )
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            text = "Sepete Ekle",
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
    }
}

@Composable
fun CardStateInCartOneButton(onCliked: () -> Unit){
    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0D47A1)
        )
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = Color.White)
            }
            Text(
                text = "1",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
            IconButton(onClick = {}) {
                Icon(Icons.Rounded.Add, contentDescription = "Add", tint = Color.White)
            }
        }
    }
}

@Composable
fun CardStateInCartMultipleButton(onCliked: () -> Unit){
    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0D47A1)
        )
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(painter = painterResource(R.drawable.ic_minus_48), contentDescription = "Delete", tint = Color.White)
            }
            Text(
                text = "2",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
            IconButton(onClick = {}) {
                Icon(Icons.Rounded.Add, contentDescription = "Add", tint = Color.White)
            }
        }
    }
}


