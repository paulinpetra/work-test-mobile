package com.paulin.work_test_mobile.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.paulin.work_test_mobile.ui.screens.HomePage
import com.paulin.work_test_mobile.ui.screens.RestaurantDetailScreen
import com.paulin.work_test_mobile.viewmodel.HomeViewModel
import com.paulin.work_test_mobile.viewmodel.RestaurantDetailViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable(route = "home") {
            val homeViewModel: HomeViewModel = viewModel()

            HomePage(
                navController = navController,
                viewModel = homeViewModel
            )
        }

        composable(
            route = "detail/{restaurantId}",
            arguments = listOf(navArgument("restaurantId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""

            val detailViewModel: RestaurantDetailViewModel = viewModel()

            RestaurantDetailScreen(
                restaurantId = restaurantId,
                viewModel = detailViewModel,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}