package com.abdulkadirkara.filmverse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.abdulkadirkara.filmverse.presentation.navigation.Navigation
import com.abdulkadirkara.filmverse.ui.theme.FilmVerseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FilmVerseTheme {
                Navigation()
            }
        }
    }
}
