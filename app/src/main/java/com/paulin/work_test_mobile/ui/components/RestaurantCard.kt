package com.paulin.work_test_mobile.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.paulin.work_test_mobile.R
import com.paulin.work_test_mobile.data.models.network.FilterData
import com.paulin.work_test_mobile.data.models.network.RestaurantData
import com.paulin.work_test_mobile.ui.theme.StarIconColor
import com.paulin.work_test_mobile.ui.theme.TimeColor
import com.paulin.work_test_mobile.ui.theme.TimeIconColor

private fun formatDeliveryTime(minutes: Int): String {
    return when {
        minutes < 60 -> "$minutes min"
        minutes % 60 == 0 -> "${minutes / 60} hr"
        else -> "${minutes / 60} hr ${minutes % 60} min"
    }
}

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
    val customShape = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )
    Card(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = customShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        AsyncImage(
            model = imageUrl,
            contentDescription = "Restaurant Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(132.dp)
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge
                )

                if (filters.isNotEmpty()) {
                    val filterText = filters.joinToString(" • ") { it.name }
                    Text(
                        text = filterText,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.clock_icon),
                        contentDescription = null,
                        tint = TimeIconColor,
                        modifier = Modifier.size(14.dp)
                    )

                    val formattedTime = formatDeliveryTime(deliveryTime)
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = TimeColor,
                        modifier = Modifier.semantics {
                            contentDescription = "Delivery time: $formattedTime"
                        }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = StarIconColor,
                    modifier = Modifier.size(14.dp)
                )

                Text(
                    text = rating.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.semantics {
                        contentDescription = "Rating: $rating out of 5 stars"
                    }
                )
            }
        }
    }
}
