package com.abdulkadirkara.filmverse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.abdulkadirkara.filmverse.presentation.navigation.BottomnavigationBar
import com.abdulkadirkara.filmverse.presentation.screens.screensplash.ScreenSplash
import com.abdulkadirkara.filmverse.ui.theme.FilmVerseTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

/**
 * Main entry point of the app where the app's UI is set up and displayed.
 *
 * This activity is responsible for initializing the app's content view
 * and managing the splash screen before navigating to the main screen with the
 * bottom navigation bar.
 *
 * The activity is annotated with [@AndroidEntryPoint] to allow Hilt to perform
 * dependency injection, making sure all dependencies are available in this activity.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FilmVerseTheme {
                // Remembering the state to manage whether the splash screen should be shown
                val showSplash = remember { mutableStateOf(true) }

                // Handling the splash screen delay asynchronously
                LaunchedEffect(true) {
                    delay(3000)
                    // After the delay, set the splash screen to false, transitioning to the main screen
                    showSplash.value = false
                }

                // Show the splash screen if `showSplash.value` is true, otherwise show the main screen
                if (showSplash.value) {
                    // Splash screen UI
                    ScreenSplash()
                } else {
                    // Main screen with the bottom navigation bar
                    BottomnavigationBar()
                }
            }
        }
    }
}
