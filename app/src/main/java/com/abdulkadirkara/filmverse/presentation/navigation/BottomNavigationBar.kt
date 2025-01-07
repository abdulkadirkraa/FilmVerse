package com.abdulkadirkara.filmverse.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun BottomnavigationBar(){
    val choosenItem = remember { mutableIntStateOf(0)  }

    val menuItems = listOf(
        BottomNavItem(Icons.Rounded.Home, "Anasayfa", Screens.ScreenHome),
        BottomNavItem(Icons.Rounded.Search, "Ara", Screens.ScreenSearch),
        BottomNavItem(Icons.Rounded.ShoppingCart, "Sepetim", Screens.ScreenCard),
        BottomNavItem(Icons.Rounded.Favorite, "Favoriler", Screens.ScreenFavorites),
        BottomNavItem(Icons.Rounded.Person, "Profil", Screens.ScreenProfile),
    )

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                content = {
                    menuItems.forEachIndexed { index, bottomNavItem ->
                        NavigationBarItem(
                            selected = choosenItem.intValue == index,
                            onClick = {
                                choosenItem.intValue = index
                            },
                            icon = {
                                Image(
                                    imageVector = bottomNavItem.iconRes,
                                    contentDescription = ""
                                )
                            },
                            label = {
                                Text(
                                    fontSize = 10.sp,
                                    text = bottomNavItem.label,
                                    style = if (choosenItem.intValue == index) {
                                        TextStyle(fontWeight = FontWeight.Bold, color = Color.Black)
                                    } else {
                                        TextStyle(
                                            fontWeight = FontWeight.Normal,
                                            color = Color.Gray
                                        )
                                    }
                                )
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Navigation(chosenScreen = menuItems[choosenItem.intValue].screen.route)
        }
    }
}

data class BottomNavItem(
    val iconRes: ImageVector,
    val label: String,
    val screen: Screens
)