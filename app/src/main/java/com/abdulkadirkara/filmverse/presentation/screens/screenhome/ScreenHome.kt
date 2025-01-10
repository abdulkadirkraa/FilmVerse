package com.abdulkadirkara.filmverse.presentation.screens.screenhome

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
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

/**
 * Composable function for rendering the home screen with movie categories, filtered movies, and an optional bottom sheet for filtering options.
 *
 * @param navController The NavController instance used for navigation within the app.
 * @param viewModel The ViewModel instance associated with this screen, responsible for managing UI-related data.
 * @param animatedVisibilityScope The scope used for controlling animated visibility transitions of UI elements.
 *
 * This composable displays the home screen of the app, which includes a custom top bar,
 * a list of movie categories, the filtered list of movies, and a bottom sheet that allows users
 * to apply filters based on categories, directors, and ratings.
 *
 * The bottom sheet can be triggered by clicking the filter icon in the top bar, and users can
 * apply, update, or clear filters. The movie list is displayed below the top bar and is updated
 * based on the selected filters.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SharedTransitionScope.ScreenHome(
    navController: NavController,
    viewModel: ScreenHomeViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    // Observing the UI state for image, category, and filtered movies
    val imageState = viewModel.imageState.observeAsState(HomeUIState.Loading)
    val categoryState = viewModel.categoryState.observeAsState(HomeUIState.Loading)
    val filteredMoviesState = viewModel.filteredMovies.observeAsState(HomeUIState.Loading)
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    // Local state for bottom sheet visibility
    var showBottomSheet by remember { mutableStateOf(false) }

    // Observing selected categories, directors, rating, and filter count
    val selectedCategories by viewModel.selectedCategories
    val selectedDirectors by viewModel.selectedDirectors
    val selectedRating by viewModel.selectedRating
    val verticalState = rememberScrollState()
    val filterCount by viewModel.filterCount
    val isFilterSelected by viewModel.isFilterSelected

    // State to detect visibility of the top bar based on scroll
    val density = LocalDensity.current
    val listState = rememberLazyGridState()
    var isVisible by remember { mutableStateOf(true) }

    // Detecting scroll position to toggle visibility of the top bar
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }
            .collect { currentOffset ->
                isVisible = currentOffset == 0
            }
    }

    // Applying filters when any selection changes
    LaunchedEffect(
        selectedCategories,
        selectedDirectors,
        selectedRating,
        filterCount
    ) {
        viewModel.applyFilters()
    }

    // Updating movies with favorites once filtered movies state changes
    LaunchedEffect(filteredMoviesState.value) {
        viewModel.updateMoviesWithFavorites()
    }

    Scaffold(
        topBar = {
            HomeScreenCustomTopBar(
                onFilterClick = {
                    showBottomSheet =
                        true // Show the bottom sheet when the filter button is clicked
                },
                filterCount = filterCount
            )
        }
    ) { paddingValues ->
        // If the screen is not scrolled (animated visibility is true), no padding is added (0.dp) to the top bar.
        // If the screen is scrolled (animated visibility is false), top padding is applied to create space between the top bar and content.
        val topPadding = if (isVisible) 0.dp else paddingValues.calculateTopPadding()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding)
        ) {
            // Bottom sheet that displays filtering options for categories, directors, and ratings
            if (showBottomSheet) {
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
                    // Filter options for categories, directors, and rating
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(verticalState),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Clear filters button when filters are applied
                        if (isFilterSelected) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = {
                                    viewModel.clearFilters()
                                    showBottomSheet = false
                                }) {
                                    Text(
                                        text = "Tümünü Kaldır",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        // Category filter selection
                        Text(text = "Kategori Seçin", style = MaterialTheme.typography.titleMedium)
                        FilterChipGroup(
                            categories = listOf("Action", "Drama", "Science Fiction", "Fantastic"),
                            selectedCategories = selectedCategories,
                            onCategorySelected = { category ->
                                viewModel.updateSelectedCategories(category)
                            },
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Director filter selection
                        Text(text = "Yönetmen Seçin", style = MaterialTheme.typography.titleMedium)
                        FilterChipGroup(
                            categories = listOf(
                                "J.J. Abrams",
                                "Baz Luhrmann",
                                "Terry Gilliam",
                                "David Fincher",
                                "Peter Jackson",
                                "Todd Phillips",
                                "Chris Columbus",
                                "Chad Stahelski",
                                "Brian De Palma",
                                "Roman Polanski",
                                "Denis Villeneuve",
                                "Christopher Nolan",
                                "Quentin Tarantino"
                            ),
                            selectedCategories = selectedDirectors,
                            onCategorySelected = { director ->
                                viewModel.updateSelectedDirectors(director)
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Rating filter selection
                        Text(
                            text = "Rating: ${selectedRating.toInt()}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Slider(
                            value = selectedRating,
                            onValueChange = { rating -> viewModel.updateSelectedRating(rating) },
                            valueRange = 0f..10f,
                            steps = 10,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Apply button to save the filters and close the bottom sheet
                        Button(onClick = {
                            viewModel.applyFilters()
                            showBottomSheet = false
                        }) {
                            Text("Uygula")
                        }
                    }
                }
            }
            // Animated visibility of home screen image based on scroll state
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically { with(density) { -40.dp.roundToPx() } } + expandVertically(
                    expandFrom = Alignment.Top
                ) + fadeIn(initialAlpha = 0.3f),
                exit = slideOutVertically { with(density) { -40.dp.roundToPx() } } + shrinkVertically() + fadeOut()
            ) {
                HomeScreenImageState(imageState = imageState)
            }
            // Display the movie categories and the filtered movies
            HomeScreenCategoryState(categoryState = categoryState, viewModel = viewModel)
            HomeScreenMoviesState(
                animatedVisibilityScope = animatedVisibilityScope,
                movieState = filteredMoviesState,
                navController = navController,
                viewModel = viewModel,
                listState = listState
            )
        }
    }
}

/**
 * A Composable function that displays different UI states (Loading, Error, Empty, Success)
 * based on the provided [imageState].
 *
 * @param imageState A [State] object containing the current UI state of type [HomeUIState<List<FilmImageEntity>>].
 *                   This state can represent one of the following states:
 *                   - [HomeUIState.Empty]: No data is available to display.
 *                   - [HomeUIState.Error]: An error occurred while fetching the data. Displays an error message.
 *                   - [HomeUIState.Loading]: Data is being loaded, displays a loading component.
 *                   - [HomeUIState.Success]: Data loaded successfully, displays a [HomeScreenViewPager]
 *                     with images from the list of [FilmImageEntity].
 */
@Composable
fun HomeScreenImageState(imageState: State<HomeUIState<List<FilmImageEntity>>>) {
    when (imageState.value) {
        is HomeUIState.Empty -> {
            LoadingComponent()
        }

        is HomeUIState.Error -> {
            val errorMessage = (imageState.value as HomeUIState.Error).message
            ErrorComponent(errorMessage = errorMessage)
        }

        HomeUIState.Loading -> {
            LoadingComponent()
        }

        is HomeUIState.Success -> {
            val imageList = (imageState.value as HomeUIState.Success<List<FilmImageEntity>>).data
            HomeScreenViewPager(imageList = imageList)
        }
    }
}

/**
 * A Composable function that displays a horizontal view pager with images.
 * It automatically cycles through the images every 2 seconds, showing one image at a time.
 *
 * @param imageList A list of [FilmImageEntity] representing the images to be displayed in the pager.
 *                  Each [FilmImageEntity] contains an image URL to load.
 *
 * This function uses [HorizontalPager] from the Pager API to display the images and animate
 * the scrolling behavior. It also includes a delay of 2 seconds between each page scroll.
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreenViewPager(imageList: List<FilmImageEntity>) {
    // State for the pager, initialized with the number of pages based on imageList size.
    val pagerState = rememberPagerState(pageCount = imageList.size)

    // Coroutine that triggers automatic scroll every 2 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % imageList.size)
        }
    }

    // Column to hold the view pager
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // HorizontalPager displays the images based on the pagerState
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            verticalAlignment = Alignment.CenterVertically
        ) { currentPage ->
            Card(
                modifier = Modifier.fillMaxSize(),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                // CustomImage composable to load and display the image for the current film using Coil.
                // It fetches the image by combining the base URL with the image path and scales it to fit the container.
                CustomImage(
                    modifier = Modifier.fillMaxSize(),
                    imageUrl = imageList[currentPage].image,
                    contentScale = ContentScale.FillBounds,
                )
            }
        }
    }
}

/**
 * A Composable function that displays different UI states (Loading, Error, Empty, Success)
 * for the category list in the home screen.
 *
 * @param categoryState A [State] object containing the current UI state of type [HomeUIState<List<FilmCategoryEntity>>].
 *                      This state can represent one of the following:
 *                      - [HomeUIState.Empty]: No category data is available to display.
 *                      - [HomeUIState.Error]: An error occurred while fetching category data. Displays an error message.
 *                      - [HomeUIState.Loading]: Data is being loaded, displays a loading component.
 *                      - [HomeUIState.Success]: Data loaded successfully, displays a list of categories
 *                        with the ability to select a category.
 * @param viewModel The [ScreenHomeViewModel] that is responsible for handling category selection actions.
 *
 * This function manages different UI states for displaying categories. On success, it shows a list of categories,
 * allowing the user to select one. The selected category is passed back to the [viewModel] through the
 * [viewModel.selectCategory] function.
 */
@Composable
fun HomeScreenCategoryState(
    categoryState: State<HomeUIState<List<FilmCategoryEntity>>>,
    viewModel: ScreenHomeViewModel,
) {
    when (categoryState.value) {
        is HomeUIState.Empty -> LoadingComponent()
        is HomeUIState.Error -> {
            val errorMessage = (categoryState.value as HomeUIState.Error).message
            ErrorComponent(errorMessage = errorMessage)
        }

        is HomeUIState.Loading -> LoadingComponent()
        is HomeUIState.Success -> {
            val categories =
                (categoryState.value as HomeUIState.Success<List<FilmCategoryEntity>>).data
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

/**
 * A Composable function that displays a horizontal list of categories using a [LazyRow].
 * Each category is displayed in a clickable card, and the selected category is highlighted.
 *
 * @param categories A list of [FilmCategoryEntity] objects representing the categories to display.
 * @param selectedCategory The currently selected category.
 * @param onCategorySelected A lambda function that will be called when a category is clicked, passing the selected [FilmCategoryEntity].
 *
 * This function uses [LazyRow] to display categories in a horizontal scrollable row. Each category is displayed
 * within a [Card] that highlights the selected category with different colors and borders. When a category is clicked,
 * it invokes the [onCategorySelected] function to update the selected category.
 */
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

/**
 * A Composable function that displays the current state of the home screen's movie list.
 * It handles various UI states like loading, error, and success.
 *
 * @param animatedVisibilityScope A scope used for shared element transitions.
 * @param movieState A [State] object representing the current UI state of type [HomeUIState<List<FilmCardEntity>>].
 *                   This can represent different states:
 *                   - [HomeUIState.Empty]: No movie data available.
 *                   - [HomeUIState.Error]: An error occurred while fetching movie data. Displays an error message.
 *                   - [HomeUIState.Loading]: Data is being loaded, shows a loading indicator.
 *                   - [HomeUIState.Success]: Movie data loaded successfully, displays the movie list.
 * @param navController The [NavController] used for navigating to different screens.
 * @param viewModel The [ScreenHomeViewModel] that manages movie data and favorite movie toggle.
 * @param listState The [LazyGridState] used to manage the scroll state of the LazyVerticalGrid.
 *
 * This function checks the state of the movie data and displays the appropriate UI. If the data is successfully
 * loaded, it displays a list of movies. When a movie is clicked, the navigation to the detail screen is handled
 * through the [navController]. Additionally, it allows toggling a movie's favorite status.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreenMoviesState(
    animatedVisibilityScope: AnimatedVisibilityScope,
    movieState: State<HomeUIState<List<FilmCardEntity>>>,
    navController: NavController,
    viewModel: ScreenHomeViewModel,
    listState: LazyGridState,
) {
    when (movieState.value) {
        is HomeUIState.Empty -> LoadingComponent()
        is HomeUIState.Error -> {
            val errorMessage = (movieState.value as HomeUIState.Error).message
            ErrorComponent(errorMessage = errorMessage)
        }

        is HomeUIState.Loading -> LoadingComponent()
        is HomeUIState.Success -> {
            val movies = (movieState.value as HomeUIState.Success<List<FilmCardEntity>>).data
            HomeScreenMovies(
                animatedVisibilityScope = animatedVisibilityScope,
                films = movies,
                listState = listState,
                onFavoriteClick = {
                    //onFavoriteClick
                    viewModel.toggleFavorite(it)
                },
                onItemClicked = {
                    //onItemClicked
                    val json = Gson().toJson(it)
                    navController.navigate("${Screens.ScreenDetail.route}/$json")
                }
            )
        }
    }
}

/**
 * A Composable function that displays a list of movies in a grid format.
 * The movies are presented in a [LazyVerticalGrid] with 2 columns, and shared element transitions are supported.
 *
 * @param animatedVisibilityScope A scope used for shared element transitions.
 * @param films A list of [FilmCardEntity] representing the movies to display.
 * @param listState The [LazyGridState] used to manage the scroll state of the grid.
 * @param onFavoriteClick A lambda function that is called when a movie's favorite icon is clicked.
 * @param onItemClicked A lambda function that is called when a movie item is clicked.
 *
 * This function renders a grid of movie cards with shared element transitions. When a movie's favorite icon is clicked,
 * the [onFavoriteClick] function is invoked. When a movie card is clicked, the [onItemClicked] function is triggered,
 * navigating the user to the movie's detail screen.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreenMovies(
    animatedVisibilityScope: AnimatedVisibilityScope,
    films: List<FilmCardEntity>,
    listState: LazyGridState,
    onFavoriteClick: (FilmCardEntity) -> Unit,
    onItemClicked: (FilmCardEntity) -> Unit
) {
    val filmState by remember { mutableStateOf(films) }

    LazyVerticalGrid(
        state = listState,
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(filmState.size) { index ->
            val film = filmState[index]
            FilmCardItem(
                animatedVisibilityScope = animatedVisibilityScope,
                film = film,
                onFavoriteClick = { onFavoriteClick(it) },
                onItemClicked = { onItemClicked(it) } // Calls item click handler for navigation
            )
        }
    }
}

/**
 * A Composable function that displays an individual movie card.
 * Each card contains a movie's image, name, rating, price, and a favorite icon.
 *
 * @param animatedVisibilityScope A scope used for shared element transitions.
 * @param film A [FilmCardEntity] representing the movie to be displayed in the card.
 * @param onFavoriteClick A lambda function that is called when the favorite icon is clicked.
 * @param onItemClicked A lambda function that is called when the movie card is clicked.
 *
 * This function creates a card displaying the movie details with shared element transitions. When the movie's
 * favorite icon is clicked, the [onFavoriteClick] function is called. When the card is clicked, the [onItemClicked]
 * function is triggered to navigate to the movie's detail screen.
 *
 * This function creates a card displaying the movie details, including a shared element transition for the image, title,
 * rating, and price. When the user clicks the movie card or the favorite icon, the corresponding callback functions
 * are triggered to handle navigation and update the movie's favorite status.
 *
 * The shared element transition uses [sharedElement] modifier with `rememberSharedContentState` to create seamless animations
 * between the movie card and the detailed movie view. The elements such as the image, title, rating, and price are all part of
 * this transition and are associated with unique content keys (e.g., "image/${film.image}", "title/${film.name}", etc.).
 * These keys ensure that each individual element is properly mapped and animated during navigation.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.FilmCardItem(
    animatedVisibilityScope: AnimatedVisibilityScope,
    film: FilmCardEntity,
    onFavoriteClick: (FilmCardEntity) -> Unit,
    onItemClicked: (FilmCardEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(300.dp)
            .clickable { onItemClicked(film) }, // Triggers item click event for navigation
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
                    modifier = Modifier
                        .fillMaxSize()
                        .sharedElement(
                            state = rememberSharedContentState("image/${film.image}"), // Shared element for the movie image
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 1000)
                            }
                        ),
                    imageUrl = film.image,
                    contentScale = ContentScale.FillWidth
                )

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

            // Randomized campaign display condition to enhance UI design, based on film id
            if (film.id % 3 == 0) {
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
                    modifier = Modifier
                        .padding(bottom = 2.dp)
                        .sharedElement(
                            state = rememberSharedContentState("title/${film.name}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 1000)
                            }
                        )
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color.Yellow,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        modifier = Modifier.sharedElement(
                            state = rememberSharedContentState("rating/${film.rating}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 1000)
                            }
                        ),
                        text = film.rating.toString(),
                        fontSize = 14.sp)
                }
                Text(
                    text = "${film.price} ₺",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .sharedElement(
                            state = rememberSharedContentState("price/${film.price}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 1000)
                            }
                        )
                )
            }
        }
    }
}

/**
 * A Composable function that displays a group of [FilterChip] components for filtering items by categories.
 * The chips allow users to select or deselect categories, and the selected categories are visually indicated.
 *
 * @param categories A list of strings representing the categories available for selection. Each category will be displayed as a [FilterChip].
 * @param selectedCategories A set of strings representing the categories that are currently selected. The chips for these categories will be highlighted as selected.
 * @param onCategorySelected A lambda function that is called when the selection of categories changes. It takes a [Set<String>] of the currently selected categories.
 *
 * The function uses a [FlowRow] to display the filter chips in a flexible, multi-line layout. Each [FilterChip] has a label and may optionally display an icon if the category is selected.
 * The icon used when a category is selected is a checkmark icon from [Icons.Filled.Done].
 *
 * When a [FilterChip] is clicked, the function updates the selection state by adding or removing the category from the [selectedCategories] set. The updated selection is passed to the [onCategorySelected] callback.
 */
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
