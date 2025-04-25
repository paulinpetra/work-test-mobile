package com.paulin.work_test_mobile.data.api

import com.paulin.work_test_mobile.data.models.network.FilterData
import com.paulin.work_test_mobile.data.models.network.OpenStatusResponse
import com.paulin.work_test_mobile.data.models.network.RestaurantsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RestaurantApi {
    @GET("restaurants")
    suspend fun getRestaurants(): Response<RestaurantsResponse>

    @GET("filter/{id}")
    suspend fun getFilterDetails(@Path("id") filterId: String): Response<FilterData>

    @GET("open/{restaurantId}")
    suspend fun getOpenStatus(@Path("restaurantId") restaurantId: String): Response<OpenStatusResponse>
}