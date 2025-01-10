package com.abdulkadirkara.filmverse.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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

/**
 * Composable function for displaying the content of the bottom navigation bar with icons, labels, and badges.
 *
 * @param menuItems A list of [BottomNavItem] representing the items in the bottom navigation.
 * @param navController The [NavController] used for navigation between different screens.
 * @param choosenItem A [MutableState] representing the currently selected item index.
 * @param cartItemCount The number of items in the cart, used to display the badge count for the cart item.
 */
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
                        // Update the selected item index and navigate to the corresponding screen
                        choosenItem.value = index
                        navController.navigate(bottomNavItem.screen.route) {
                            launchSingleTop = true // Avoid creating multiple instances of the same destination
                            restoreState = true // Restore the previous state of the destination
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true // Save the state of the previous destination
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

/**
 * Composable function to display a badge on the icon if the badge count is greater than 0.
 *
 * @param badgeCount The count of the badge to display.
 * @param isCartIcon A boolean indicating whether the icon represents the cart.
 * @param content The composable content to be displayed within the badge box.
 */
@Composable
fun BadgeBox(badgeCount: Int, isCartIcon: Boolean, content: @Composable () -> Unit) {
    if (badgeCount > 0) {
        BadgedBox(
            badge = {
                Badge {
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

/**
 * Composable function to display the bottom navigation bar and handle navigation between different screens.
 * This function manages navigation logic and displays a badge count for the cart.
 *
 * @param menuItems A list of [BottomNavItem] representing the items in the bottom navigation.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
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

    SharedTransitionLayout {
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
                    ScreenHome(navController, viewModel, animatedVisibilityScope = this)
                }
                /**
                 * Composable destination for the Detail Screen.
                 *
                 * @param film A JSON string representing the selected film.
                 */
                composable(
                    route = "${Screens.ScreenDetail.route}/{film}",
                    arguments = listOf(navArgument("film") { type = NavType.StringType })
                ) {
                    val json = it.arguments?.getString("film")
                    val film = Gson().fromJson(json, FilmCardEntity::class.java)
                    val viewModel: ScreenDetailViewModel = hiltViewModel()
                    ScreenDetail(animatedVisibilityScope = this,
                        film, navController, viewModel)
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
}

/**
 * Data class representing a bottom navigation item, including its icon, label, route, and optional badge count.
 *
 * @param iconRes The icon image resource for the navigation item.
 * @param label The label text for the navigation item.
 * @param screen The screen associated with this navigation item.
 * @param badgeCount The badge count to display for the item. Default is 0.
 */
data class BottomNavItem(
    val iconRes: ImageVector,
    val label: String,
    val screen: Screens,
    val badgeCount: Int = 0
)
