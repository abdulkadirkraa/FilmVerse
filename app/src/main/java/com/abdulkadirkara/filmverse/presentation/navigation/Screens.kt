package com.abdulkadirkara.filmverse.presentation.navigation

/**
 * A sealed class used to define screen routes for navigation in a Jetpack Compose application.
 *
 * Each screen is represented as a `data object` with a unique route string.
 * This structure ensures type-safe and maintainable navigation throughout the app.
 *
 * @property route The unique route string associated with each screen.
 */
sealed class Screens(val route: String) {

    /**
     * Represents the Home screen.
     * The navigation route for this screen is "screen_home".
     */
    data object ScreenHome : Screens("screen_home")
    data object ScreenDetail : Screens("screen_detail")
    data object ScreenCard : Screens("screen_card")
    data object ScreenFavorites : Screens("screen_favorites")
    data object ScreenProfile : Screens("screen_profile")
    data object ScreenSearch : Screens("screen_search")
}