package com.abdulkadirkara.filmverse.presentation.screens.components.topappbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cast
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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

/**
 * A custom top bar for the home screen that includes a Cast icon, a centered title,
 * a Search icon, and a Filter icon with a badge showing the number of active filters.
 *
 * @param onFilterClick A lambda function that will be invoked when the filter icon is clicked.
 * @param filterCount The number of active filters, displayed in a badge next to the filter icon.
 */
@Composable
fun HomeScreenCustomTopBar(
    onFilterClick: () -> Unit,
    filterCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { // Apply alpha and shadow effects to the top bar
                alpha = 0.95f // Slight transparency for the background
                shadowElevation = 8.dp.toPx() // Add shadow under the top bar
            }
            .background(Color.Black.copy(alpha = 0.5f)) // Semi-transparent black background
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Cast Icon (on the left side of the top bar)
        Icon(
            imageVector = Icons.Rounded.Cast,
            contentDescription = "Cast",
            tint = Color.White,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(10f)
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

        // Row layout for the search and filter icons on the right side of the top bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp), // Reduce space between icons
            modifier = Modifier
                .padding(end = 4.dp)
                .weight(20f)
        ) {
            IconButton(onClick = { /*onSearchClick*/ }) {
                Icon(Icons.Rounded.Search, contentDescription = "Search", tint = Color.White)
            }
            // Column to vertically arrange the filter icon and its badge
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                BadgedBox(
                    badge = {
                        if (filterCount > 0) {
                            Badge(
                                containerColor = Color.Red,
                                contentColor = Color.Black
                            ) {
                                Text(text = "$filterCount")
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = onFilterClick
                    ) {
                        Icon(
                            Icons.Rounded.FilterList,
                            contentDescription = "Filter",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}