package com.abdulkadirkara.filmverse.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abdulkadirkara.filmverse.presentation.screens.screencard.ScreenCard
import com.abdulkadirkara.filmverse.presentation.screens.screendetail.ScreenDetail
import com.abdulkadirkara.filmverse.presentation.screens.screenhome.ScreenHome
import com.abdulkadirkara.filmverse.presentation.screens.screenhome.ScreenHomeViewModel

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.ScreenHome.route){
        composable(Screens.ScreenHome.route){
            val viewModel: ScreenHomeViewModel = hiltViewModel()
            ScreenHome(viewModel)
        }
        composable(Screens.ScreenDetail.route) {
            ScreenDetail()
        }
        composable(Screens.ScreenCard.route) {
            ScreenCard()
        }
    }
}