package com.paulin.work_test_mobile.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.paulin.work_test_mobile.data.models.network.RestaurantData

@Composable
fun RestaurantList(
    restaurantData: List<RestaurantData>,
    onRestaurantClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(restaurantData) { restaurant ->
            RestaurantCard(
                name = restaurant.name,
                rating = restaurant.rating,
                deliveryTime = restaurant.deliveryTimeMinutes,
                imageUrl = restaurant.imageUrl,
                restaurantId = restaurant.id,
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
    restaurantId: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    // Parameter to control whether to show all details or just basic info
    showDetails: Boolean = true
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
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                contentScale = ContentScale.FillWidth,
                model = imageUrl,
                contentDescription = "Restaurant Image",
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Always show the name
                TitleValueText(title = "Name: ", value = name)

                // Conditionally show additional details
                if (showDetails) {
                    TitleValueText(title = "Rating: ", value = rating.toString())
                    TitleValueText(title = "Delivery Time: ", value = "$deliveryTime min")
                }
            }
        }
    }
}


//helper function to display a title-value pair with different styling.

@Composable
private fun TitleValueText(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            ) {
                append(title)
            }
            append(value)
        },
        style = MaterialTheme.typography.bodyLarge
    )
}
