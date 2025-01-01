package com.abdulkadirkara.filmverse.presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cast
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdulkadirkara.filmverse.R
import com.abdulkadirkara.filmverse.ui.theme.babasNeue

@Composable
fun CustomTopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                alpha = 0.95f // Hafif saydamlık
                shadowElevation = 8.dp.toPx() // Gölge efekti
            }
            .background(Color.Black.copy(alpha = 0.5f)), // Hafif siyah arka plan
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Icon(
            Icons.Rounded.Cast, contentDescription = "Cast",
            tint = Color.White,
        )
        Text(
            text = stringResource(R.string.app_name),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = babasNeue,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.padding(end = 4.dp)
        ) {

            IconButton(onClick = { /* Handle search icon click */ }) {
                Icon(Icons.Rounded.Search, contentDescription = "Search", tint = Color.White)
            }
            IconButton(onClick = { /* Handle filter icon click */ }) {
                Icon(Icons.Rounded.FilterList, contentDescription = "Filter", tint = Color.White)
            }
        }
    }
}
