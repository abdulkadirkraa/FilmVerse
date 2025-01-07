package com.abdulkadirkara.filmverse.presentation.navigation

import androidx.compose.runtime.Composable
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
import com.abdulkadirkara.filmverse.presentation.screens.screenhome.ScreenHome
import com.abdulkadirkara.filmverse.presentation.screens.screenhome.ScreenHomeViewModel
import com.abdulkadirkara.filmverse.presentation.screens.screenprofile.ScreenProfile
import com.abdulkadirkara.filmverse.presentation.screens.scrennsearch.ScreenSearch
import com.google.gson.Gson

@Composable
fun Navigation( chosenScreen: String){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = chosenScreen){
        composable(Screens.ScreenHome.route){
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
        composable(route = Screens.ScreenCard.route ) {
            val viewModel: ScreenCardViewModel = hiltViewModel()
            ScreenCard(viewModel)
        }
        composable(route = Screens.ScreenFavorites.route) {
            ScreenFavorites()
        }
        composable(route = Screens.ScreenProfile.route) {
            ScreenProfile()
        }
        composable(route = Screens.ScreenSearch.route) {
            ScreenSearch()
        }
    }
}