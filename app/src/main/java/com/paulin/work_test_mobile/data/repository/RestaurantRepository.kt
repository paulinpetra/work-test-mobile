package com.paulin.work_test_mobile.data.repository

import android.util.Log
import com.paulin.work_test_mobile.data.api.RetrofitInstance
import com.paulin.work_test_mobile.data.models.network.FilterData
import com.paulin.work_test_mobile.data.models.network.OpenStatusResponse
import com.paulin.work_test_mobile.data.models.network.RestaurantData
import com.paulin.work_test_mobile.data.models.network.RestaurantsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class RestaurantRepository {
    private val restaurantApi =
        RetrofitInstance.restaurantApi

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

    suspend fun fetchFilterDetails(filterId: String): FilterData? = withContext(Dispatchers.IO) {
        try {
            val response = restaurantApi.getFilterDetails(filterId)
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