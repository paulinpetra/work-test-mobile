package com.paulin.work_test_mobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.paulin.work_test_mobile.data.models.network.FilterData
import com.paulin.work_test_mobile.data.models.network.RestaurantData
import com.paulin.work_test_mobile.ui.theme.FilterTextColor
import com.paulin.work_test_mobile.ui.theme.RestaurantClosedColor
import com.paulin.work_test_mobile.ui.theme.RestaurantOpenColor

@Composable
fun RestaurantDetailCard(
    restaurant: RestaurantData, filters: List<FilterData>, isOpen: Boolean, modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            AsyncImage(
                model = restaurant.imageUrl,
                contentDescription = "Photo of ${restaurant.name} restaurant",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 175.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = restaurant.name,
                    style = MaterialTheme.typography.headlineMedium,
                )
                if (filters.isNotEmpty()) {
                    val filterText = filters.joinToString(" • ") { it.name }
                    Text(
                        text = filterText,
                        style = MaterialTheme.typography.labelMedium,
                        color = FilterTextColor,

                        )
                }
                Text(
                    text = if (isOpen) "Open" else "Closed",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = if (isOpen) RestaurantOpenColor else RestaurantClosedColor
                    ),
                    modifier = Modifier.semantics {
                        contentDescription = "Restaurant is currently ${if (isOpen) "open" else "closed"}"
                    }
                )

            }
        }
    }
}