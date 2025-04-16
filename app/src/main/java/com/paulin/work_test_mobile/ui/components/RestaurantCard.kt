package com.paulin.work_test_mobile.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.paulin.work_test_mobile.data.models.network.FilterData
import com.paulin.work_test_mobile.data.models.network.RestaurantData
import com.paulin.work_test_mobile.ui.theme.InterFamily
import com.paulin.work_test_mobile.ui.theme.RestaurantTitleColor

@Composable
fun RestaurantList(
    restaurantData: List<RestaurantData>,
    filters: List<FilterData>,
    onRestaurantClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(restaurantData) { restaurant ->
            // get filters for this restaurant
            val restaurantFilters = filters.filter { filter ->
                restaurant.filterIds.contains(filter.id)
            }

            RestaurantCard(
                name = restaurant.name,
                rating = restaurant.rating,
                deliveryTime = restaurant.deliveryTimeMinutes,
                imageUrl = restaurant.imageUrl,
                filters = restaurantFilters,
                onClick = { onRestaurantClick(restaurant.id) }
            )
        }
    }
}

@Composable
fun RestaurantCard(
    name: String,
    rating: Double,
    deliveryTime: Int,
    imageUrl: String,
    filters: List<FilterData>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = CardDefaults.shape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
        ),
    ) {

        AsyncImage(
            contentScale = ContentScale.FillWidth,
            model = imageUrl,
            contentDescription = "Restaurant Image",
            modifier = Modifier.fillMaxWidth()
        )

        // Text content area
        Box(modifier = Modifier.fillMaxWidth()) {
            // Main content column
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Restaurant name
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    color = RestaurantTitleColor,
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Normal
                )

                // Filter tags
                if (filters.isNotEmpty()) {
                    // Join filter names with commas
                    val filterText = filters.joinToString(", ") { it.name }
                    Text(
                        text = filterText,
                        color = RestaurantTitleColor,
                        fontFamily = InterFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 16.sp
                    )
                }

                // Delivery time
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // You can add a clock icon here if needed
                    Text(
                        text = "$deliveryTime min",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Rating in top-right corner of text area
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Star icon
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFF9CA24),
                    modifier = Modifier.size(14.dp)
                )

                // Rating text
                Text(
                    text = rating.toString(),
                    color = Color(0xFF50555C),
                    fontFamily = InterFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 10.sp
                )
            }
        }
    }
}
