package com.paulin.work_test_mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paulin.work_test_mobile.data.models.network.FilterData
import com.paulin.work_test_mobile.data.models.network.RestaurantData
import com.paulin.work_test_mobile.data.repository.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {
    private val restaurantRepository: RestaurantRepository = RestaurantRepository()

    private val _restaurantData = MutableStateFlow<List<RestaurantData>>(emptyList())

    private val _filters = MutableStateFlow<List<FilterData>>(emptyList())
    val filters: StateFlow<List<FilterData>> = _filters.asStateFlow()

    private val _filteredRestaurants = MutableStateFlow<List<RestaurantData>>(emptyList())
    val filteredRestaurants: StateFlow<List<RestaurantData>> = _filteredRestaurants.asStateFlow()

    private val _activeFilterIds = MutableStateFlow<Set<String>>(emptySet())
    val activeFilterIds: StateFlow<Set<String>> = _activeFilterIds.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()


    fun getRestaurantData() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val restaurantResult =
                    restaurantRepository.fetchRestaurantData()
                if (restaurantResult != null) {
                    _restaurantData.value = restaurantResult
                    _filteredRestaurants.value = restaurantResult
                    _error.value = null

                    val filterIds = restaurantResult.flatMap { it.filterIds }
                        .distinct()
                    fetchFilterDetails(filterIds)

                } else {
                    _error.value = "Failed to fetch restaurant data"

                }
            } catch (e: Exception) {
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

    fun filterRestaurantsByFilterId(filterId: String) {
        val allRestaurants = _restaurantData.value
        val currentFilters = _activeFilterIds.value.toMutableSet()

        if (filterId in currentFilters) {
            currentFilters.remove(filterId)
        } else {
            currentFilters.add(filterId)
        }

        _activeFilterIds.value = currentFilters
        if (currentFilters.isEmpty()) {
            _filteredRestaurants.value = allRestaurants
        } else {
            _filteredRestaurants.value = allRestaurants.filter { restaurant ->
                restaurant.filterIds.any { it in currentFilters }
            }
        }
    }
}
