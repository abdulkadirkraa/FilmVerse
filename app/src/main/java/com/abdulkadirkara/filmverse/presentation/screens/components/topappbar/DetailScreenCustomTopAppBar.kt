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
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }

        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = babasNeue,
            modifier = modifier.weight(1f).padding(start = 16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            BadgedBox(
                badge = {
                    if (cartItemCount > 0){
                        Badge(
                            containerColor = Color.Red,
                            contentColor = Color.Black
                        ){
                            Text(text = "$cartItemCount")
                        }
                    }
                }
            ) {
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