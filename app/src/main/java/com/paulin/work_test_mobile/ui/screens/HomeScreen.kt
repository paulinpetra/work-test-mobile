package com.paulin.work_test_mobile.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    // trigger data fetching when the composable is first displayed
    LaunchedEffect(Unit) {//Unit as the key means "run this effect once when the composable enters the composition and never again"
        viewModel.getRestaurantData()
    }

    // collect StateFlow values as state
    val restaurantData by viewModel.filteredRestaurants.collectAsState()
    val filters by viewModel.filters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AppHeader()

        // horizontal Scroll List for the filters
        if (filters.isNotEmpty()) {
            FilterList(
                filters = filters,
                activeFilterId = viewModel.activeFilterId.collectAsState().value,
                onFilterClick = { filterId ->
                    viewModel.filterRestaurantsByFilterId(filterId)
                })
        }

        // vertical Scrollable List for the Restaurant Cards
        when {
            isLoading -> LoadingIndicator()
            error != null -> ErrorMessage(message = error!!)
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
