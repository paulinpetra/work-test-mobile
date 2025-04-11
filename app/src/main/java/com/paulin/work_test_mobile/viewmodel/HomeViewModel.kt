package com.paulin.work_test_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paulin.work_test_mobile.data.models.network.FiltersResponse
import com.paulin.work_test_mobile.data.models.network.RestaurantData
import com.paulin.work_test_mobile.data.repository.RestaurantRepository
import kotlinx.coroutines.launch

//extends ViewModel, which is part of Android's architecture components
class HomeViewModel : ViewModel() {
    //call the method of RestaurantRepository which fetches the data
    private val restaurantRepository: RestaurantRepository = RestaurantRepository()

    //live data to hold mutable an immutable restaurant data
    //live data is simple and has minimal boilerplate, could be changed to StateFlow
    private val _restaurantData = MutableLiveData<List<RestaurantData>>()
    val restaurantData: LiveData<List<RestaurantData>> = _restaurantData

    //live data to hold filter data
    private val _filters = MutableLiveData<List<FiltersResponse>>()
    val filters: LiveData<List<FiltersResponse>> = _filters

    //data for filtered restaurant list
    private val _filteredRestaurants = MutableLiveData<List<RestaurantData>>()
    val filteredRestaurants: LiveData<List<RestaurantData>> = _filteredRestaurants


    //live data to hold loading and error states
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    //get restaurant-list data, async so need coroutine (viewmodelscope)
    fun getRestaurantData() {
        _isLoading.postValue(true)
//When you call launch within a coroutine scope, such as viewModelScope, it creates and starts a new coroutine
        viewModelScope.launch {
            try {
                val restaurantResult = restaurantRepository.fetchRestaurantData()
                if (restaurantResult != null) {
                    _restaurantData.postValue(restaurantResult)
                    _filteredRestaurants.postValue(restaurantResult) // Initially show all

                    _error.postValue(null)

                    // Extract unique filter IDs and fetch their details(fetchFilterDetails function below)
                    val filterIds = restaurantResult.flatMap { it.filterIds }.distinct()
                    fetchFilterDetails(filterIds)

                } else {
                    _error.postValue("Failed to fetch restaurant data")

                }
            } catch (e: Exception) {
                _error.postValue("An error occurred: ${e.message}")

            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    //Fetch filter-list data
    private fun fetchFilterDetails(filterIds: List<String>) {
        viewModelScope.launch {
            val filterDetails = filterIds.mapNotNull { filterId ->
                restaurantRepository.fetchFilterDetails(filterId)
            }
            _filters.postValue(filterDetails)
        }
    }

    //function for filtering restaurants based on selected filter
    fun filterRestaurantsByFilterId(filterId: String) {
        val allRestaurants = _restaurantData.value ?: return
        val filtered = allRestaurants.filter { it.filterIds.contains(filterId) }
        _filteredRestaurants.postValue(filtered)
    }


}
