package com.abdulkadirkara.filmverse.presentation.screens.screenhome

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abdulkadirkara.domain.model.FilmCardEntity
import com.abdulkadirkara.domain.model.FilmCategoryEntity
import com.abdulkadirkara.domain.model.FilmImageEntity
import com.abdulkadirkara.filmverse.presentation.navigation.Screens
import com.abdulkadirkara.filmverse.presentation.screens.components.CustomImage
import com.abdulkadirkara.filmverse.presentation.screens.components.ErrorComponent
import com.abdulkadirkara.filmverse.presentation.screens.components.LoadingComponent
import com.abdulkadirkara.filmverse.presentation.screens.components.topappbar.HomeScreenCustomTopBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenHome(
    navController: NavController,
    viewModel: ScreenHomeViewModel = hiltViewModel()
) {
    val imageState = viewModel.imageState.observeAsState(HomeUIState.Loading)
    val categoryState = viewModel.categoryState.observeAsState(HomeUIState.Loading)
    val filteredMoviesState = viewModel.filteredMovies.observeAsState(HomeUIState.Loading)
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val selectedCategories by viewModel.selectedCategories
    val selectedDirectors by viewModel.selectedDirectors
    val selectedRating by viewModel.selectedRating
    val verticalState = rememberScrollState()
    val filterCount by viewModel.filterCount

    LaunchedEffect(
        selectedCategories,
        selectedDirectors,
        selectedRating,
        filterCount
    ) {
        viewModel.applyFilters()
    }

    LaunchedEffect(filteredMoviesState.value) {
        viewModel.updateMoviesWithFavorites()
    }

    val isFilterSelected by viewModel.isFilterSelected

    Scaffold(
        topBar = {
            HomeScreenCustomTopBar(
                onFilterClick = {
                    showBottomSheet = true
                },
                filterCount = filterCount
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (showBottomSheet){
                ModalBottomSheet(
                    sheetState = bottomSheetState,
                    onDismissRequest = { coroutineScope.launch { showBottomSheet = false } },
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 16.dp,
                    dragHandle = {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(75.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(50))
                                .background(MaterialTheme.colorScheme.primary)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp).verticalScroll(verticalState),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (isFilterSelected) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = {
                                    viewModel.clearFilters() // Filtreleri sıfırlamak için
                                    showBottomSheet = false
                                }) {
                                    Text(text = "Tümünü Kaldır", color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }

                        Text(text = "Kategori Seçin", style = MaterialTheme.typography.titleMedium)
                        FilterChipGroup(
                            categories = listOf("Action", "Drama", "Science Fiction", "Fantastic"),
                            selectedCategories = selectedCategories,
                            onCategorySelected = { category ->
                                viewModel.updateSelectedCategories(category)
                            },
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "Yönetmen Seçin", style = MaterialTheme.typography.titleMedium)
                        FilterChipGroup(
                            categories = listOf("J.J. Abrams", "Baz Luhrmann", "Terry Gilliam", "David Fincher", "Peter Jackson",
                                "Todd Phillips", "Chris Columbus", "Chad Stahelski", "Brian De Palma", "Roman Polanski",
                                "Denis Villeneuve", "Christopher Nolan", "Quentin Tarantino"
                            ),
                            selectedCategories = selectedDirectors,
                            onCategorySelected = { director ->
                                viewModel.updateSelectedDirectors(director)
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "Rating: ${selectedRating.toInt()}", style = MaterialTheme.typography.titleMedium)
                        Slider(
                            value = selectedRating,
                            onValueChange = { rating -> viewModel.updateSelectedRating(rating) },
                            valueRange = 0f..10f,
                            steps = 10,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            viewModel.applyFilters()
                            showBottomSheet = false
                        }) {
                            Text("Uygula")
                        }
                    }
                }
            }
            HomeScreenImageState(imageState)
            HomeScreenCategoryState(categoryState, viewModel)
            HomeScreenMoviesState(filteredMoviesState, navController, viewModel)
        }
    }
}

@Composable
fun HomeScreenImageState(imageState: State<HomeUIState<List<FilmImageEntity>>>) {
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
            val imageList = (imageState.value as HomeUIState.Success<List<FilmImageEntity>>).data
            HomeScreenViewPager(imageList)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreenViewPager(imageList: List<FilmImageEntity>) {
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
                .height(300.dp),
            verticalAlignment = Alignment.CenterVertically
        ) { currentPage ->
            Card(
                modifier = Modifier
                    .fillMaxSize()
                ,
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                CustomImage(
                    modifier = Modifier.fillMaxSize(),
                    imageUrl = imageList[currentPage].image,
                    contentScale = ContentScale.FillBounds,
                )
            }
        }
    }
}

@Composable
fun HomeScreenCategoryState(
    categoryState: State<HomeUIState<List<FilmCategoryEntity>>>,
    viewModel: ScreenHomeViewModel,
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
            val categories = (categoryState.value as HomeUIState.Success<List<FilmCategoryEntity>>).data
            val selectedCategory = categories.find { it.isClicked } ?: categories.first()
            CategoryList(
                categories = categories,
                selectedCategory = selectedCategory
            ) { selected ->
                viewModel.selectCategory(selected)
            }
        }
    }
}

@Composable
fun CategoryList(
    categories: List<FilmCategoryEntity>,
    selectedCategory: FilmCategoryEntity,
    onCategorySelected: (FilmCategoryEntity) -> Unit
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
fun HomeScreenMoviesState(
    movieState: State<HomeUIState<List<FilmCardEntity>>>,
    navController: NavController,
    viewModel: ScreenHomeViewModel
) {
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
            val movies = (movieState.value as HomeUIState.Success<List<FilmCardEntity>>).data
            HomeScreenMovies(movies, {
                //onFavoriteClick
                viewModel.toggleFavorite(it)
            },{
                //onItemClicked
                val json = Gson().toJson(it)
                navController.navigate("${Screens.ScreenDetail.route}/$json")
            })
        }
    }
}

@Composable
fun HomeScreenMovies(
    films: List<FilmCardEntity>,
    onFavoriteClick: (FilmCardEntity) -> Unit,
    onItemClicked: (FilmCardEntity) -> Unit
) {
    val filmState by remember { mutableStateOf(films) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(filmState.size) { index ->
            val film = filmState[index]
            FilmCardItem(
                film = film,
                onFavoriteClick = { onFavoriteClick(it) },
                onItemClicked = { onItemClicked(it) }
            )
        }
    }
}

@Composable
fun FilmCardItem(
    film: FilmCardEntity,
    onFavoriteClick: (FilmCardEntity) -> Unit,
    onItemClicked: (FilmCardEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(400.dp)
            .clickable { onItemClicked(film) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                CustomImage(
                    modifier = Modifier.fillMaxSize(),
                    imageUrl = film.image,
                    contentScale = ContentScale.FillHeight
                )

                // Favori butonu
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(8.dp)
                        .background(Color.LightGray, CircleShape)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (film.isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = "Favori",
                        tint = if (film.isFavorite) Color.Red else Color.White,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { onFavoriteClick(film) }
                    )
                }
            }

            if (film.id % 3 == 0){
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
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterChipGroup(
    categories: List<String>,
    selectedCategories: Set<String>,
    onCategorySelected: (Set<String>) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        categories.forEach { category ->
            FilterChip(
                selected = selectedCategories.contains(category),
                onClick = {
                    val updatedSelection = if (selectedCategories.contains(category)) {
                        selectedCategories - category
                    } else {
                        selectedCategories + category
                    }
                    onCategorySelected(updatedSelection)
                },
                label = { Text(text = category) },
                leadingIcon = if (selectedCategories.contains(category)) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Selected",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else null,
                modifier = Modifier.padding(2.dp)
            )
        }
    }
}