package com.abdulkadirkara.filmverse.presentation.screens.screenhome

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.abdulkadirkara.data.remote.ApiConstants
import com.abdulkadirkara.domain.model.FilmCategoryUI
import com.abdulkadirkara.domain.model.FilmImageUI
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
){
    val imageState = viewModel.imageState.observeAsState(HomeUIState.Loading)
    val categoryState = viewModel.categoryState.observeAsState(HomeUIState.Loading)
    val selectedCategory = viewModel.selectedCategory.observeAsState().value
    val movieState = viewModel.movieState.observeAsState(HomeUIState.Loading)

    Scaffold(
        topBar = {
            CustomTopAppBar()
        }
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
        ) {
            //image state'ine bakar viewpager yapıyı çağırır
            HomeScreenImageState(imageState)
            //categori state'ine bakarak card yapısını çağırır
            HomeScreenCategoryState(categoryState, viewModel,selectedCategory)
            //HomeScreenMovies
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
fun HomeScreenViewPager(imageList: List<FilmImageUI>){
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
            Log.e("url", url)

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
            ErrorComponent(errorMessage){
                //Bir şeyler yapıalcak
            }
        }
        is HomeUIState.Loading -> LoadingComponent()
        is HomeUIState.Success -> {
            val categories = (categoryState.value as HomeUIState.Success<List<FilmCategoryUI>>).data
            val allCategories = listOf(FilmCategoryUI(category = "Tümü", isClicked = false)) + categories
            CategoryList(
                categories = allCategories,
                selectedCategory = selectedCategory ?: allCategories.first()
            ) { selected ->
                viewModel.selectCategory(selected)
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
    LazyRow {
        items(categories.size) { index ->
            val category = categories[index]
            val isSelected = category == selectedCategory

            OutlinedCard(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onCategorySelected(category) },
                colors = CardDefaults.outlinedCardColors(
                    containerColor = if (isSelected) Color.Blue.copy(alpha = 0.2f) else Color.Transparent,
                    contentColor = if (isSelected) Color.Red else Color.Green
                ),
                border = if (isSelected) BorderStroke(2.dp, Color.Blue) else BorderStroke(1.dp, Color.Gray),
                elevation = CardDefaults.cardElevation(if (isSelected) 8.dp else 2.dp) // Seçili olan kart daha belirgin olabilir
            ) {
                Text(
                    text = category.category,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                    color = if (isSelected) Color.Blue else Color.Black,
                    style = if (isSelected) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


