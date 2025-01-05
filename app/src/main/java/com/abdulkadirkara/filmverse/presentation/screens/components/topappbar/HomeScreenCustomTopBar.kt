package com.abdulkadirkara.filmverse.presentation.screens.components.topappbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cast
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
fun HomeScreenCustomTopBar(
    onFilterClick: () -> Unit,
    filterCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                alpha = 0.95f
                shadowElevation = 8.dp.toPx()
            }
            .background(Color.Black.copy(alpha = 0.5f))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Cast Icon
        Icon(
            imageVector = Icons.Rounded.Cast,
            contentDescription = "Cast",
            tint = Color.White,
            modifier = Modifier.padding(start = 8.dp).weight(10f)
        )

        // Spacer to push the text to the center
        Spacer(modifier = Modifier.weight(1f))

        // Filmverse Text
        Text(
            text = stringResource(R.string.app_name),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = babasNeue,
            modifier = Modifier.weight(70f)
        )

        // Spacer to balance the space after the text
        Spacer(modifier = Modifier.weight(1f))

        // Search and Filter Icons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp), // Reduce space between icons
            modifier = Modifier.padding(end = 4.dp).weight(20f)
        ) {
            IconButton(onClick = { /*onSearchClick*/ }) {
                Icon(Icons.Rounded.Search, contentDescription = "Search", tint = Color.White)
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BadgedBox(
                    badge = {
                        if (filterCount > 0) {
                            Badge(
                                containerColor = Color.Red,
                                contentColor = Color.Black
                            ) {
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = onFilterClick
                    ) {
                        Icon(Icons.Rounded.FilterList, contentDescription = "Filter", tint = Color.White)
                    }
                }
            }
        }
    }
}