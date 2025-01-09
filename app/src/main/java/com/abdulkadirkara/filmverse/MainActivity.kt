package com.abdulkadirkara.filmverse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.abdulkadirkara.filmverse.presentation.navigation.BottomnavigationBar
import com.abdulkadirkara.filmverse.presentation.screens.screensplash.ScreenSplash
import com.abdulkadirkara.filmverse.ui.theme.FilmVerseTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FilmVerseTheme {
                val showSplash = remember { mutableStateOf(true) }

                LaunchedEffect(true) {
                    delay(3000)
                    showSplash.value = false
                }

                if (showSplash.value) {
                    ScreenSplash()
                } else {
                    BottomnavigationBar()
                }
            }
        }
    }
}
