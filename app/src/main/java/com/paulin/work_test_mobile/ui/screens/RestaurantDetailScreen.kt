package com.paulin.work_test_mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.paulin.work_test_mobile.ui.components.ErrorMessage
import com.paulin.work_test_mobile.ui.components.LoadingIndicator
import com.paulin.work_test_mobile.ui.components.RestaurantDetailCard
import com.paulin.work_test_mobile.ui.components.TransparentStatusBarEffect
import com.paulin.work_test_mobile.viewmodel.RestaurantDetailViewModel


@Composable
fun RestaurantDetailScreen(
    restaurantId: String,
    viewModel: RestaurantDetailViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    //hide status bar
    TransparentStatusBarEffect(hideStatusBarIcons = true)

    // fetch restaurant details and open status when the screen is displayed
    LaunchedEffect(restaurantId) {
        viewModel.getRestaurantDetails(restaurantId)
        viewModel.getOpenStatus(restaurantId)
    }

    //collect StateFlow values as state
    val restaurant by viewModel.restaurantDetails.collectAsState()
    val openStatus by viewModel.openStatus.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val filters by viewModel.filters.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        //back button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .zIndex(1f)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Back",
                tint = Color(0xFF1F2B2E)
            )

        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .zIndex(0f)
        ) {
            // show different UI based on state
            when {
                isLoading -> LoadingIndicator()
                error != null -> ErrorMessage(message = error!!)
                restaurant != null -> {
                    RestaurantDetailCard(
                        restaurant = restaurant!!,
                        filters = filters,
                        isOpen = openStatus?.isCurrentlyOpen == true


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