package com.abdulkadirkara.filmverse.presentation.screens.screenhome

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.abdulkadirkara.data.remote.ApiConstants
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
            //HomeScreenViewPager
            HomeScreenViewPager(imageState)
            //HomeScreenCategories
            //HomeScreenMovies
        }
    }
}

@Composable
fun HomeScreenViewPager(imageState: State<HomeUIState<List<FilmImageUI>>>) {
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
            HomeViewPagerContent(imageList)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeViewPagerContent(imageList: List<FilmImageUI>){
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
