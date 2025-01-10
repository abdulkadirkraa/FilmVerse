package com.abdulkadirkara.filmverse.presentation.screens.components

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdulkadirkara.filmverse.R
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

/**
 * A composable function that displays a customizable error screen with an animated Lottie
 * animation, an error message, and a button to close the application.
 *
 * @param errorMessage The error message to display on the screen. This will be shown below
 *        the "Hata!" text.
 */
@Composable
fun ErrorComponent(
    errorMessage: String,
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)), // Dark translucent background
        contentAlignment = Alignment.Center
    ) {
        // Box for the error card containing Lottie animation, error text, and button
        Box(
            modifier = Modifier
                .width(320.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Load the Lottie composition for the error animation (using raw resource)
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.filmverseerrorlottie))

                // Display the Lottie animation with infinite iteration
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .size(140.dp)
                        .padding(bottom = 16.dp)
                )

                Text(text = "Hata!", fontSize = 22.sp, color = Color.Red)
                Text(
                    text = errorMessage,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Button(
                    onClick = {
                        // Close the app by calling finish() on the current activity
                        (context as? Activity)?.finish()
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Uygulamayı Kapat")
                }
            }
        }
    }
}
