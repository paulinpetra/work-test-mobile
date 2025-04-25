package com.paulin.work_test_mobile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paulin.work_test_mobile.data.models.network.FilterData
import com.paulin.work_test_mobile.data.models.network.OpenStatusResponse
import com.paulin.work_test_mobile.data.models.network.RestaurantData
import com.paulin.work_test_mobile.data.repository.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class RestaurantDetailViewModel : ViewModel() {

    private val restaurantRepository: RestaurantRepository = RestaurantRepository()

    private val _restaurantDetails = MutableStateFlow<RestaurantData?>(null)
    val restaurantDetails: StateFlow<RestaurantData?> = _restaurantDetails.asStateFlow()

    private val _filters = MutableStateFlow<List<FilterData>>(emptyList())
    val filters: StateFlow<List<FilterData>> = _filters.asStateFlow()

    private val _openStatus = MutableStateFlow<OpenStatusResponse?>(null)
    val openStatus: StateFlow<OpenStatusResponse?> = _openStatus.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun getRestaurantDetails(restaurantId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val allRestaurants = restaurantRepository.fetchRestaurantData()
                val restaurant = allRestaurants?.find { it.id == restaurantId }

                if (restaurant != null) {
                    _restaurantDetails.value = restaurant
                    _error.value = null
                    fetchFilterDetails(restaurant.filterIds)

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

    private fun fetchFilterDetails(filterIds: List<String>) {
        viewModelScope.launch {
            val filterDetails = filterIds.mapNotNull { filterId ->
                restaurantRepository.fetchFilterDetails(filterId)
            }
            _filters.value = filterDetails
        }
    }


    fun getOpenStatus(restaurantId: String) {
        viewModelScope.launch {
            try {
                val status = restaurantRepository.fetchOpenStatus(restaurantId)
                _openStatus.value = status
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error fetching open status", e)
            }
        }
    }
}