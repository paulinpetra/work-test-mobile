package com.paulin.work_test_mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paulin.work_test_mobile.ui.components.ErrorMessage
import com.paulin.work_test_mobile.ui.components.LoadingIndicator
import com.paulin.work_test_mobile.ui.components.RestaurantDetailCard
import com.paulin.work_test_mobile.viewmodel.RestaurantDetailViewModel


@Composable
fun RestaurantDetailScreen(
    restaurantId: String,
    viewModel: RestaurantDetailViewModel,
    modifier: Modifier = Modifier
) {
    // fetch restaurant details and open status when the screen is displayed
    LaunchedEffect(restaurantId) {
        viewModel.getRestaurantDetails(restaurantId)
        viewModel.getOpenStatus(restaurantId)
    }

    // Collect StateFlow values as state
    val restaurant by viewModel.restaurantDetails.collectAsState()
    val openStatus by viewModel.openStatus.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val filters by viewModel.filters.collectAsState()//??


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // show different UI based on state
        when {
            isLoading -> LoadingIndicator()
            error != null -> ErrorMessage(message = error!!)
            restaurant != null -> {
                RestaurantDetailCard(
                    restaurant = restaurant!!,
                    filters = filters
                )


                Spacer(modifier = Modifier.height(16.dp))


                // Open status card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (openStatus?.isCurrentlyOpen == true)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = if (openStatus?.isCurrentlyOpen == true) "Open" else "Closed",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            else -> {
                Text(text = "Restaurant information not available")
            }
        }
    }
}