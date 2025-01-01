package com.abdulkadirkara.filmverse.presentation.navigation

sealed class Screens (val route: String){
    data object ScreenHome: Screens("screen_home")
    data object ScreenDetail:Screens("screen_detail")
    data object ScreenCard:Screens("screen_card")
}