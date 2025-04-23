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
    //call the method of RestaurantRepository which fetches the data
    private val restaurantRepository: RestaurantRepository = RestaurantRepository()

    //Private mutable state (_restaurantData) that only the ViewModel can modify
    //Public immutable state (restaurantData) that UI components observe
    private val _restaurantData = MutableStateFlow<List<RestaurantData>>(emptyList())
    //not used by the UI
    //val restaurantData: StateFlow<List<RestaurantData>> = _restaurantData.asStateFlow()

    //StateFlow to hold filter data
    private val _filters = MutableStateFlow<List<FilterData>>(emptyList())
    val filters: StateFlow<List<FilterData>> = _filters.asStateFlow()

    //data for filtered restaurant list
    private val _filteredRestaurants = MutableStateFlow<List<RestaurantData>>(emptyList())
    val filteredRestaurants: StateFlow<List<RestaurantData>> = _filteredRestaurants.asStateFlow()

    // StateFlow to track the currently active filter
    private val _activeFilterIds = MutableStateFlow<Set<String>>(emptySet())
    val activeFilterIds: StateFlow<Set<String>> = _activeFilterIds.asStateFlow()

    //StateFlow to hold loading and error states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()


    //get restaurant-list data, async so need coroutine (viewmodelscope)
    fun getRestaurantData() {
        _isLoading.value = true
//when you call launch within a coroutine scope, such as viewModelScope, it creates and starts a new coroutine
        viewModelScope.launch {      //runs on main thread (handled by Retrofit)
            println("${Thread.currentThread().name} :: ${Thread.currentThread().id}")  //checking which thread it runs on (system.out in log cat)
            try {
                val restaurantResult =
                    restaurantRepository.fetchRestaurantData()   //kotlin coroutine system automatically switches to IO thread (repo methods are suspend f that don't block main thread
                if (restaurantResult != null) {
                    _restaurantData.value = restaurantResult
                    _filteredRestaurants.value = restaurantResult  //shows all initially

                    _error.value =
                        null   //setting error to null after sucessful call indicating that any prev errors has been resolved

                    // extract unique filter IDs and fetch their details(fetchFilterDetails function below)
                    //each RestaurantData object has a property called filterIds which is a List<String>
                    val filterIds = restaurantResult.flatMap { it.filterIds }
                        .distinct()//distinct() removes duplicate elements from a collection so you only fetch details for each filter ID once, even if multiple restaurants use the same filter
                    fetchFilterDetails(filterIds)//for each unique filter ID, fetch its detailed information

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

    //fetch filter-list data
    //for each ID, calls the repository method to get its details(repo takes one id)
    private fun fetchFilterDetails(filterIds: List<String>) {
        viewModelScope.launch {
            val filterDetails = filterIds.mapNotNull { filterId ->//automatically filter out any null results
                restaurantRepository.fetchFilterDetails(filterId)
            }
            // directly set the list of FilterData objects
            _filters.value = filterDetails
        }
    }

    //function for filtering restaurants based on selected filters
    fun filterRestaurantsByFilterId(filterId: String) {
        val allRestaurants = _restaurantData.value
        val currentFilters = _activeFilterIds.value.toMutableSet()

        if (filterId in currentFilters) {
            // If filter is already selected, remove it
            currentFilters.remove(filterId)
        } else {
            // Otherwise add it
            currentFilters.add(filterId)
        }

        _activeFilterIds.value = currentFilters
        //apply filtering
        if (currentFilters.isEmpty()) {
            // No filters selected, show all restaurants
            _filteredRestaurants.value = allRestaurants
        } else {
            // Show restaurants that match ANY of the selected filters
            _filteredRestaurants.value = allRestaurants.filter { restaurant ->
                restaurant.filterIds.any { it in currentFilters }
            }
        }
    }
}
