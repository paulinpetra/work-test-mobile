package com.paulin.work_test_mobile.ui.components


import android.R.attr.rating
import androidx.compose.foundation.layout.*
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
fun RestaurantDetailCard(
    restaurant: RestaurantData, filters: List<FilterData>, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CardDefaults.shape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
        ),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                contentScale = ContentScale.FillWidth,
                model = restaurant.imageUrl,
                contentDescription = "Restaurant Image",
                modifier = Modifier.fillMaxWidth()
            )

            // text content area
            Box(modifier = Modifier.fillMaxWidth()) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = restaurant.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = RestaurantTitleColor,
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.Normal
                    )

                    if (filters.isNotEmpty()) {
                        val filterText = filters.joinToString(" \\u2022 ") { it.name } //• not working
                        Text(
                            text = filterText,
                            color = RestaurantTitleColor,
                            fontFamily = InterFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 16.sp
                        )
                    }

                    // delivery time
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // add a clock icon
                        Text(
                            text = "${restaurant.deliveryTimeMinutes} min", style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // rating
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFF9CA24),
                        modifier = Modifier.size(14.dp)
                    )
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
}
