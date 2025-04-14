package com.paulin.work_test_mobile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paulin.work_test_mobile.data.models.network.OpenStatusResponse
import com.paulin.work_test_mobile.data.models.network.RestaurantData
import com.paulin.work_test_mobile.data.repository.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


// simple implementation using StateFlow without DI or factory pattern.

class RestaurantDetailViewModel : ViewModel() {

    // Create repository instance directly (same as HomeViewModel)
    private val restaurantRepository: RestaurantRepository = RestaurantRepository()

    // StateFlow to hold restaurant details
    private val _restaurantDetails = MutableStateFlow<RestaurantData?>(null)
    val restaurantDetails: StateFlow<RestaurantData?> = _restaurantDetails.asStateFlow()

    // StateFlow to hold the open status of the restaurant
    private val _openStatus = MutableStateFlow<OpenStatusResponse?>(null)
    val openStatus: StateFlow<OpenStatusResponse?> = _openStatus.asStateFlow()

    // StateFlow to hold loading and error states (same as HomeViewModel)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Function to fetch restaurant details by ID
    fun getRestaurantDetails(restaurantId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Get the full restaurant list
                val allRestaurants = restaurantRepository.fetchRestaurantData()

                // Find the specific restaurant by ID
                val restaurant = allRestaurants?.find { it.id == restaurantId }

                if (restaurant != null) {
                    _restaurantDetails.value = restaurant
                    _error.value = null
                } else {
                    _error.value = "Restaurant not found"
                }
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error fetching restaurant details", e)
                _error.value = "An error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Function to fetch the open status of a restaurant
    fun getOpenStatus(restaurantId: String) {
        viewModelScope.launch {
            try {
                val status = restaurantRepository.fetchOpenStatus(restaurantId)
                _openStatus.value = status
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error fetching open status", e)
                // Not setting error here to avoid overriding restaurant details error
            }
        }
    }
}