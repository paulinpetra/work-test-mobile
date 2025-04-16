package com.paulin.work_test_mobile.data.api

import com.paulin.work_test_mobile.data.models.network.FilterData
import com.paulin.work_test_mobile.data.models.network.OpenStatusResponse
import com.paulin.work_test_mobile.data.models.network.RestaurantsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

//The interface defines what API calls are available without specifying how they're implemented (separation of concerns)
//Retrofit dynamically creates a class that implements your interface

interface RestaurantApi {
    //endpoint for all restaurants
    @GET("restaurants")
    suspend fun getRestaurants(): Response<RestaurantsResponse> //Retrofit executes the request on a background thread (because of suspend)
    //it then converts JSON and wraps the result in a response object the rest of the code can process

    //endpoint for filters
    @GET("filter/{id}")
    suspend fun getFilterDetails(@Path("id") filterId: String): Response<FilterData>

    //endpoint for open status of restaurant
    @GET("open/{restaurantId}")
    suspend fun getOpenStatus(@Path("restaurantId") restaurantId: String): Response<OpenStatusResponse>
}