package com.paulin.work_test_mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.paulin.work_test_mobile.data.models.network.RestaurantData
import com.paulin.work_test_mobile.ui.components.ErrorMessage
import com.paulin.work_test_mobile.ui.components.LoadingIndicator
import com.paulin.work_test_mobile.ui.components.RestaurantDetailCard
import com.paulin.work_test_mobile.viewmodel.RestaurantDetailViewModel


@Composable
fun RestaurantDetailScreen(
    restaurantId: String, viewModel: RestaurantDetailViewModel, onBackClick: () -> Unit, modifier: Modifier = Modifier
) {

    LaunchedEffect(restaurantId) {
        viewModel.getRestaurantDetails(restaurantId)
        viewModel.getOpenStatus(restaurantId)
    }

    val restaurant by viewModel.restaurantDetails.collectAsState()
    val openStatus by viewModel.openStatus.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val filters by viewModel.filters.collectAsState()

    Box(
        modifier = modifier.fillMaxSize()

    ) {

        IconButton(
            onClick = onBackClick, modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .zIndex(1f)
                .shadow(
                    elevation = 4.dp, spotColor = Color.White.copy(alpha = 0.25f)
                )

        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Back to restaurant list",
                tint = Color(0xFF1F2B2E),
                modifier = Modifier
                    .size(24.dp)
                    .semantics {
                        role = Role.Button
                        contentDescription = "Back to restaurant list"
                    })

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(0f)
        ) {
            when {
                isLoading -> LoadingIndicator()
                error != null -> {
                    val currentError = error as String
                    ErrorMessage(message = currentError)
                }

                restaurant != null -> {
                    val currentRestaurant = restaurant as RestaurantData

                    RestaurantDetailCard(
                        restaurant = currentRestaurant, filters = filters, isOpen = openStatus?.isCurrentlyOpen == true
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                else -> {
                    Text(text = "Restaurant information not available")
                }
            }
        }
    }
}