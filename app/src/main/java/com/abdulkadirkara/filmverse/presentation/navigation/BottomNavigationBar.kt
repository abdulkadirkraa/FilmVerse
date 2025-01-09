package com.abdulkadirkara.filmverse.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.abdulkadirkara.domain.model.FilmCardEntity
import com.abdulkadirkara.filmverse.presentation.screens.screencard.ScreenCard
import com.abdulkadirkara.filmverse.presentation.screens.screencard.ScreenCardViewModel
import com.abdulkadirkara.filmverse.presentation.screens.screendetail.ScreenDetail
import com.abdulkadirkara.filmverse.presentation.screens.screendetail.ScreenDetailViewModel
import com.abdulkadirkara.filmverse.presentation.screens.screenfavorites.ScreenFavorites
import com.abdulkadirkara.filmverse.presentation.screens.screenfavorites.ScreenFavoritesViewModel
import com.abdulkadirkara.filmverse.presentation.screens.screenhome.ScreenHome
import com.abdulkadirkara.filmverse.presentation.screens.screenhome.ScreenHomeViewModel
import com.abdulkadirkara.filmverse.presentation.screens.screenprofile.ScreenProfile
import com.abdulkadirkara.filmverse.presentation.screens.scrennsearch.ScreenSearch
import com.google.gson.Gson

@Composable
fun BottomNavigationBarContent(
    menuItems: List<BottomNavItem>,
    navController: NavController,
    choosenItem: MutableState<Int>,
    cartItemCount: Int
) {
    BottomAppBar(
        containerColor = Color.White,
        content = {
            menuItems.forEachIndexed { index, bottomNavItem ->
                val selectedColor = Color(0xFF0D47A1)
                val unselectedColor = Color.Gray

                NavigationBarItem(
                    selected = choosenItem.value == index,
                    onClick = {
                        choosenItem.value = index
                        navController.navigate(bottomNavItem.screen.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    },
                    icon = {
                        BadgeBox(
                            badgeCount = if (index == 2) cartItemCount else bottomNavItem.badgeCount,
                            isCartIcon = index == 2
                        ) {
                            Image(
                                imageVector = bottomNavItem.iconRes,
                                contentDescription = null,
                                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                                    if (choosenItem.value == index) selectedColor else unselectedColor
                                )
                            )
                        }
                    },
                    label = {
                        Text(
                            fontSize = 10.sp,
                            text = bottomNavItem.label,
                            style = TextStyle(
                                fontWeight = if (choosenItem.value == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (choosenItem.value == index) selectedColor else unselectedColor
                            )
                        )
                    }
                )
            }
        }
    )
}


@Composable
fun BadgeBox(badgeCount: Int, isCartIcon: Boolean, content: @Composable () -> Unit) {
    if (badgeCount > 0) {
        androidx.compose.material3.BadgedBox(
            badge = {
                androidx.compose.material3.Badge {
                    Text(badgeCount.toString(), fontSize = 8.sp)
                }
            }
        ) {
            content()
        }
    } else {
        content()
    }
}


@Composable
fun BottomnavigationBar() {
    val navController = rememberNavController()
    val choosenItem = remember { mutableIntStateOf(0) }
    val cartViewModel: BottomBarCartViewModel = hiltViewModel()
    val cartItemCount by cartViewModel.cartItemCount.collectAsState()

    val menuItems = listOf(
        BottomNavItem(Icons.Rounded.Home, "Anasayfa", Screens.ScreenHome),
        BottomNavItem(Icons.Rounded.Search, "Ara", Screens.ScreenSearch),
        BottomNavItem(Icons.Rounded.ShoppingCart, "Sepetim", Screens.ScreenCard, badgeCount = cartItemCount),
        BottomNavItem(Icons.Rounded.Favorite, "Favoriler", Screens.ScreenFavorites),
        BottomNavItem(Icons.Rounded.Person, "Profil", Screens.ScreenProfile)
    )


    Scaffold(
        bottomBar = {
            BottomNavigationBarContent(
                menuItems = menuItems,
                navController = navController,
                choosenItem = choosenItem,
                cartItemCount = cartItemCount
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.ScreenHome.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screens.ScreenHome.route) {
                val viewModel: ScreenHomeViewModel = hiltViewModel()
                ScreenHome(navController, viewModel)
            }
            composable(
                route = "${Screens.ScreenDetail.route}/{film}",
                arguments = listOf(navArgument("film") { type = NavType.StringType })
            ) {
                val json = it.arguments?.getString("film")
                val film = Gson().fromJson(json, FilmCardEntity::class.java)
                val viewModel: ScreenDetailViewModel = hiltViewModel()
                ScreenDetail(film, navController, viewModel)
            }
            composable(Screens.ScreenCard.route) {
                val viewModel: ScreenCardViewModel = hiltViewModel()
                ScreenCard(viewModel)
            }
            composable(Screens.ScreenFavorites.route) {
                val viewModel: ScreenFavoritesViewModel = hiltViewModel()
                ScreenFavorites(viewModel)
            }
            composable(Screens.ScreenProfile.route) {
                ScreenProfile()
            }
            composable(Screens.ScreenSearch.route) {
                ScreenSearch()
            }
        }
    }
}

data class BottomNavItem(
    val iconRes: ImageVector,
    val label: String,
    val screen: Screens,
    val badgeCount: Int = 0
)