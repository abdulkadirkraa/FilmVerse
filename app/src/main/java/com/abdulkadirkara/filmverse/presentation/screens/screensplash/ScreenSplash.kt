package com.abdulkadirkara.filmverse.presentation.screens.screensplash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.abdulkadirkara.filmverse.R
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

/**
 * Composable function that displays a splash screen with a looping Lottie animation.
 * This screen is typically shown when the application is loading and is used to
 * provide a visual welcome or branding experience to the user.
 */
@Composable
fun ScreenSplash() {
    // 'isPlaying' controls whether the animation is currently playing
    val isPlaying by remember { mutableStateOf(true) }

    // 'speed' controls the playback speed of the Lottie animation
    val speed by remember { mutableFloatStateOf(1f) }

    // 'composition' loads the Lottie animation from the raw resource file
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.filmverselottie))

    // 'progress' provides the progress of the animation over time, allowing for playback control
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever, // The animation will loop forever
        isPlaying = isPlaying,
        speed = speed,
        restartOnPlay = false // Prevents restarting the animation when 'isPlaying' changes
    )

    // A Box composable used to center the Lottie animation on the screen
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // LottieAnimation composable displays the animation with the current progress
        LottieAnimation(
            composition = composition, // The loaded Lottie animation composition
            progress = { progress }, // The current progress of the animation
            modifier = Modifier.fillMaxSize()
        )
    }
}