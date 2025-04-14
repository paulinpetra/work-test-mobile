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

//this is the central nav component that defines the apps navigation graph

@Composable
fun AppNavigation(
    navController: NavHostController, //A controller that manages the navigation state and back stack in a Compose application

    modifier: Modifier = Modifier
) {
    NavHost( //serves as container for the navGraph, swaps out composables based on current destination
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable(route = "home") {//unique identifier for this destination used to reference it when navigating

            // create HomeViewModel at the navigation level, ensures viewmodel survies config change but is cleared when navigating away permanently
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
            })// define the type and properties of the route arguments
//backStackEntry.arguments bundle contains all parameters for this navigation destination
            //backStackEntry.arguments?.getString("restaurantId") extracts the parameter value
        ) { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""

            // create a RestaurantDetailViewModel instance scoped to this navigation destination
            val detailViewModel: RestaurantDetailViewModel = viewModel()

            RestaurantDetailScreen(
                restaurantId = restaurantId,
                viewModel = detailViewModel
            )
        }
    }
}