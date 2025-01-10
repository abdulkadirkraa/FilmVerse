package com.abdulkadirkara.filmverse.presentation.screens.screenfavorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abdulkadirkara.domain.model.FilmEntityModel
import com.abdulkadirkara.filmverse.presentation.screens.components.CustomImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

/**
 * Data class representing a tab item with a title, unselected icon, and selected icon.
 * It is used for tab navigation.
 *
 * @param title The title of the tab.
 * @param unselectedIcon The icon displayed when the tab is not selected.
 * @param selectedIcon The icon displayed when the tab is selected.
 */
data class TabItem(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
)

/**
 * A composable screen that displays two tabs:
 * 1. "Favoriler" - A tab to view the list of favorite films.
 * 2. "Koleksiyonlar" - A tab (currently a placeholder) to view collections.
 *
 * This screen uses a pager to switch between the tabs.
 *
 * @param viewModel The ViewModel to manage the screen's data and state. It is provided by Hilt.
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScreenFavorites(viewModel: ScreenFavoritesViewModel = hiltViewModel()) {
    // List of tab items with titles and associated icons.
    val tabItems = listOf(
        TabItem(
            title = "Favoriler",
            unselectedIcon = Icons.Default.FavoriteBorder,
            selectedIcon = Icons.Default.Favorite
        ),
        TabItem(
            title = "Koleksiyonlar",
            unselectedIcon = Icons.Default.BookmarkBorder,
            selectedIcon = Icons.Default.Bookmark
        )
    )

    // State to track the currently selected tab.
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // PagerState to manage the pager for tab switching.
    val pagerState = rememberPagerState(
        pageCount = tabItems.size
    )

    // Synchronize the tab selection with the pager's current page.
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress)
            selectedTabIndex = pagerState.currentPage
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Tab row to display the tabs at the top.
        TabRow(selectedTabIndex = selectedTabIndex) {
            // Iterate over the tabItems list and create a Tab for each item.
            tabItems.forEachIndexed { index, tabItem ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = tabItem.title) },
                    icon = {
                        // Dynamically switch between selected and unselected icons.
                        Icon(
                            imageVector = if (selectedTabIndex == index) tabItem.selectedIcon else tabItem.unselectedIcon,
                            contentDescription = tabItem.title
                        )
                    }
                )
            }
        }

        // HorizontalPager to display the content corresponding to each tab.
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { index ->
            // Box that holds the content of each page in the pager.
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (index) {
                    0 -> {
                        FavoriteFilmsContent(viewModel)
                    }

                    1 -> {
                        // Koleksiyonlar Tab'ı
                        Text(text = "Koleksiyonlar içerikleri burada olacak.")
                    }
                }
            }
        }
    }
}

/**
 * A composable function that displays the list of favorite films.
 * It collects the favorite films from the ViewModel and displays them in a LazyColumn.
 * If there's an error message, it displays it at the top.
 *
 * @param viewModel The ViewModel that provides the favorite films and error messages.
 */
@Composable
fun FavoriteFilmsContent(viewModel: ScreenFavoritesViewModel) {
    // Collect favorite films and error message from the ViewModel.
    val favoriteFilms by viewModel.favoriteFilms.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Load the favorite films when the component is launched or favorite films change.
    LaunchedEffect(favoriteFilms) {
        viewModel.loadFavoriteFilms()
    }

    if (errorMessage != null) {
        Text(text = errorMessage ?: "Bir hata oluştu", color = Color.Red)
    } else {
        LazyColumn {
            items(favoriteFilms.size) { film ->
                FilmItem(
                    film = favoriteFilms[film],
                    onAddToCollectionClick = { /* Functionality to add to collection */ },
                    onRemoveFromFavoritesClick = {
                        viewModel.deleteFilm(favoriteFilms[film])
                        viewModel.loadFavoriteFilms()
                    },
                    onShareClick = {/* Functionality to share film */ }
                )
            }
        }
    }
}

/**
 * A Composable function that represents a film item in the favorites list.
 * It displays a film's image, name, category, rating, and price. It also provides options
 * to add the film to a collection, share it, or remove it from favorites.
 *
 * @param film The [FilmEntityModel] that contains information about the film.
 * @param onAddToCollectionClick A lambda function to handle the action when adding the film to a collection.
 * @param onRemoveFromFavoritesClick A lambda function to handle the action when removing the film from favorites.
 * @param onShareClick A lambda function to handle the action when sharing the film.
 */
@Composable
fun FilmItem(
    film: FilmEntityModel,
    onAddToCollectionClick: () -> Unit,
    onRemoveFromFavoritesClick: () -> Unit,
    onShareClick: () -> Unit
) {
    // Local state to track whether the options dropdown is expanded or not
    var expanded by remember { mutableStateOf(false) }

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
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomImage(
                imageUrl = film.imagePath,
                modifier = Modifier
                    .weight(0.2f)
                    .padding(end = 8.dp),
                imageSize = DpSize(80.dp, 80.dp)
            )

            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .padding(end = 8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = film.name)
                Text(text = film.category)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color.Yellow,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = film.rating.toString())
                }

                Text(text = "${film.price}₺")
            }

            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxHeight()
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = "Options Menu"
                    )
                }

                // Dropdown menu that contains actions like adding to collection, sharing, and deleting
                DropdownMenu(
                    expanded = expanded, // Dropdown is visible when 'expanded' is true
                    onDismissRequest = { expanded = false } // Close the dropdown when dismissed
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Listeye Ekle") },
                        onClick = {
                            expanded = false
                            onAddToCollectionClick()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.BookmarkAdd,
                                contentDescription = "Listeye Ekle",
                                tint = Color(0xFF0D47A1)
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Paylaş") },
                        onClick = {
                            expanded = false
                            onShareClick()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Paylaş",
                                tint = Color(0xFF0D47A1)
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Sil") },
                        onClick = {
                            expanded = false
                            onRemoveFromFavoritesClick() // Trigger the remove from favorites action
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Sil",
                                tint = Color(0xFF0D47A1)
                            )
                        }
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Handle click to add to cart */ },
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
}
