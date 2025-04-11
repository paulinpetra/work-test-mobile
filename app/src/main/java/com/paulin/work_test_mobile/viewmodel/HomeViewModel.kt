package com.paulin.work_test_mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paulin.work_test_mobile.data.models.network.FiltersResponse
import com.paulin.work_test_mobile.data.models.network.RestaurantData
import com.paulin.work_test_mobile.data.repository.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


//extends ViewModel, which is part of Android's architecture components
class HomeViewModel : ViewModel() {
    //call the method of RestaurantRepository which fetches the data
    private val restaurantRepository: RestaurantRepository = RestaurantRepository()

    //live data version:
    // private val _restaurantData = MutableLiveData<List<RestaurantData>>()
    //val restaurantData: LiveData<List<RestaurantData>> = _restaurantData

    //StateFlow to hold mutable and immutable restaurant data
    private val _restaurantData = MutableStateFlow<List<RestaurantData>>(emptyList())
    val restaurantData: StateFlow<List<RestaurantData>> = _restaurantData.asStateFlow()

    //StateFlow to hold filter data
    private val _filters = MutableStateFlow<List<FiltersResponse>>(emptyList())
    val filters: StateFlow<List<FiltersResponse>> = _filters.asStateFlow()

    //data for filtered restaurant list
    private val _filteredRestaurants = MutableStateFlow<List<RestaurantData>>(emptyList())
    val filteredRestaurants: StateFlow<List<RestaurantData>> = _filteredRestaurants.asStateFlow()

    //StateFlow to hold loading and error states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()


    //get restaurant-list data, async so need coroutine (viewmodelscope)
    fun getRestaurantData() {
        _isLoading.value = true
//When you call launch within a coroutine scope, such as viewModelScope, it creates and starts a new coroutine
        viewModelScope.launch {      //runs on main thread (handled by Retrofit)
            println("${Thread.currentThread().name} :: ${Thread.currentThread().id}")  //checking which thread it runs on (system.out in log cat)
            try {
                val restaurantResult =
                    restaurantRepository.fetchRestaurantData()   //kotlin coroutine system automatically switches to IO thread (repo methods are suspend f that don't block main thread
                println("${Thread.currentThread().name} :: ${Thread.currentThread().id}")  //checking which thread it runs on (system.out in log cat)

                if (restaurantResult != null) {
                    _restaurantData.value = restaurantResult
                    _filteredRestaurants.value = restaurantResult  //shows all initially

                    _error.value = null   //after network call is complete, back to main thread   (why post value??)

                    // Extract unique filter IDs and fetch their details(fetchFilterDetails function below)
                    val filterIds = restaurantResult.flatMap { it.filterIds }.distinct()
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

    //Fetch filter-list data
    private fun fetchFilterDetails(filterIds: List<String>) {
        viewModelScope.launch {
            val filterDetails = filterIds.mapNotNull { filterId ->
                restaurantRepository.fetchFilterDetails(filterId)
            }
            _filters.value = filterDetails
        }
    }

    //function for filtering restaurants based on selected filter
    fun filterRestaurantsByFilterId(filterId: String) {
        val allRestaurants = _restaurantData.value
        if (allRestaurants.isEmpty()) return

        val filtered = allRestaurants.filter { it.filterIds.contains(filterId) }
        _filteredRestaurants.value = filtered
    }

}
