package com.abdulkadirkara.filmverse.presentation.screens.components.topappbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdulkadirkara.filmverse.ui.theme.babasNeue

/**
 * A custom top app bar for the detail screen. It consists of a back button, a title, a cart icon with a badge showing the
 * number of items in the cart, and a share button.
 *
 * @param modifier An optional [Modifier] to customize the layout or appearance of the top app bar.
 * @param title The title text to display in the center of the top app bar.
 * @param cartItemCount The number of items currently in the cart. A badge will appear next to the cart icon when there are items.
 * @param onBackClick A lambda function that will be called when the back button is clicked.
 * @param goToCardScreen A lambda function that will be called when the shopping cart icon is clicked.
 */
@Composable
fun DetailScreenCustomTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    cartItemCount: Int,
    onBackClick: () -> Unit,
    goToCardScreen: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Auto-mirrored back arrow icon for RTL support
                contentDescription = "Back"
            )
        }

        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = babasNeue,
            modifier = modifier
                .weight(1f)
                .padding(start = 16.dp)
        )

        // Row for cart and share icons aligned to the right
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            // Badge showing the number of items in the cart
            BadgedBox(
                badge = {
                    if (cartItemCount > 0) {
                        Badge(
                            containerColor = Color.Red,
                            contentColor = Color.Black
                        ) {
                            Text(text = "$cartItemCount")
                        }
                    }
                }
            ) {
                // Cart icon that opens the cart screen when clicked
                IconButton(onClick = goToCardScreen) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "cart")
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { /**/ }) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "Share"
                )
            }
        }
    }
}