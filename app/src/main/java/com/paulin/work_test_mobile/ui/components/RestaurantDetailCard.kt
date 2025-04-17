package com.paulin.work_test_mobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.paulin.work_test_mobile.data.models.network.FilterData
import com.paulin.work_test_mobile.data.models.network.RestaurantData
import com.paulin.work_test_mobile.ui.theme.FilterTextColor
import com.paulin.work_test_mobile.ui.theme.InterFamily

@Composable
fun RestaurantDetailCard(
    restaurant: RestaurantData,
    filters: List<FilterData>,
    isOpen: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            AsyncImage(
                model = restaurant.imageUrl,
                contentDescription = restaurant.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

        }

        // card overlapping the image slightly
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 175.dp), // position to overlap image
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                //title
                Text(
                    text = restaurant.name,
                    style = MaterialTheme.typography.headlineMedium,
                )
                // filter categories
                if (filters.isNotEmpty()) {
                    val filterText = filters.joinToString(" • ") { it.name }
                    Text(
                        text = filterText,
                        color = FilterTextColor,
                        fontFamily = InterFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 16.sp
                    )
                }
                Text(
                    text = if (isOpen) "Open" else "Closed",
                    color = if (isOpen)
                        Color(0xFF2ECC71)
                    else
                        Color(0xFFC0392B),
                )

            }
        }
    }
}