package com.abdulkadirkara.filmverse.presentation.screens.screenfavorites

import android.util.Log
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScreenFavorites(viewModel: ScreenFavoritesViewModel = hiltViewModel()) {
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

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val pagerState = rememberPagerState(
        pageCount = tabItems.size
    )

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
        TabRow (selectedTabIndex = selectedTabIndex) {
            tabItems.forEachIndexed { index, tabItem ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = tabItem.title) },
                    icon = {
                        Icon(
                            imageVector = if (selectedTabIndex == index) tabItem.selectedIcon else tabItem.unselectedIcon,
                            contentDescription = tabItem.title
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) { index ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
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

data class TabItem(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
)

@Composable
fun FavoriteFilmsContent(viewModel: ScreenFavoritesViewModel) {
    val favoriteFilms by viewModel.favoriteFilms.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

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
                    onAddToCollectionClick = { /*viewModel.deleteFilm(favoriteFilms[film])*/ },
                    onRemoveFromFavoritesClick = { viewModel.deleteFilm(favoriteFilms[film]) },
                    onShareClick = {}
                )
            }
        }
    }
}

@Composable
fun FilmItem(
    film: FilmEntityModel,
    onAddToCollectionClick: () -> Unit,
    onRemoveFromFavoritesClick: () -> Unit,
    onShareClick: () -> Unit
) {
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

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
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
                            onRemoveFromFavoritesClick()
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
                        .clickable {
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
}
