package com.paulin.work_test_mobile.data.repository

import android.util.Log
import com.paulin.work_test_mobile.data.api.RetrofitInstance
import com.paulin.work_test_mobile.data.models.network.FiltersResponse
import com.paulin.work_test_mobile.data.models.network.OpenStatusResponse
import com.paulin.work_test_mobile.data.models.network.RestaurantData
import com.paulin.work_test_mobile.data.models.network.RestaurantsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

//A repository is a design pattern that acts as an abstraction layer between your data sources (like APIs, databases, or local storage) and the rest of your application.
// It provides a clean API for data access regardless of where that data comes from.
//it provides data for viewmodel from it's cache or api call and view model does not need to know how or where it got the data .
// It also isolated the data operations from business logic(separation of concerns)
//data abstraction
//single source of truth (centralizes data access logic and make data handling consistent in app

//I use a single repository with all the API endpoint calls for simplicity (reduced boilerplate code and easy to understand) since it's a small number of related (single purpose) API calls
//Use the Retrofit Instance here in the Repository
//To do? Add a sealed class to wrap the response from network calls, do not return null (   suspend fun fetchRestaurantData(): Result<List<RestaurantData>> )

class RestaurantRepository {
    private val restaurantApi =
        RetrofitInstance.restaurantApi //creates an instance of the api service which gives access to the API endpoints

    //the fetch for restaurant list, a suspend function so it can be called from a coroutine and dispatcher to run on IO thread
    //This ensures that all network operations run on the IO thread pool, which is optimized for network and disk operations
    //withContext -suspending f to change dispatcher
    suspend fun fetchRestaurantData(): List<RestaurantData>? = withContext(Dispatchers.IO) {
        try {
            val response: Response<RestaurantsResponse> = restaurantApi.getRestaurants()
            if (response.isSuccessful) {
                response.body()?.restaurants
            } else {
                Log.e("RestaurantRepository", "Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("RestaurantRepository", "Exception: ${e.message}")
            null
        }

    }

    //the fetch for the filter list by using id from restaurants
    suspend fun fetchFilterDetails(filterId: String): FiltersResponse? = withContext(Dispatchers.IO) {
        try {
            val response = restaurantApi.getFilterDetails(filterId)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("RestaurantRepository", "Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {//here a custom exception could be thrown instead for more specific info connected to the apps context
            Log.e("RestaurantRepository", "Exception: ${e.message}")
            null
        }
    }

    //fetch the open status of restaurants
    suspend fun fetchOpenStatus(restaurantId: String): OpenStatusResponse? = withContext(Dispatchers.IO) {
        try {
            val response = restaurantApi.getOpenStatus(restaurantId)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("RestaurantRepository", "Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("RestaurantRepository", "Exception: ${e.message}")
            null
        }
    }

}