package com.paulin.work_test_mobile.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.paulin.work_test_mobile.ui.components.AppHeader
import com.paulin.work_test_mobile.ui.components.ErrorMessage
import com.paulin.work_test_mobile.ui.components.FilterList
import com.paulin.work_test_mobile.ui.components.LoadingIndicator
import com.paulin.work_test_mobile.ui.components.RestaurantList
import com.paulin.work_test_mobile.viewmodel.HomeViewModel

@Composable
fun HomePage(modifier: Modifier = Modifier, viewModel: HomeViewModel, navController: NavController) {

    val restaurantData by viewModel.filteredRestaurants.collectAsState()
    val filters by viewModel.filters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val activeFilterIds by viewModel.activeFilterIds.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AppHeader()

        if (filters.isNotEmpty()) {
            FilterList(filters = filters, activeFilterIds = activeFilterIds, onFilterClick = { filterId ->
                viewModel.filterRestaurantsByFilterId(filterId)
            })
        }

        when {
            isLoading -> LoadingIndicator()
            error != null -> {
                val currentError = error as String
                ErrorMessage(message = currentError)
            }

            restaurantData.isEmpty() -> EmptyStateMessage()
            else -> RestaurantList(
                restaurantData = restaurantData,
                filters = filters,
                onRestaurantClick = { restaurantId ->
                    navController.navigate("detail/$restaurantId")
                })
        }
    }
}

@Composable
private fun EmptyStateMessage() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Text(text = "No restaurants found")
    }
}
