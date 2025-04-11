package com.paulin.work_test_mobile.data.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// @Serializable annotation tells the Kotlin Serialization library that this class
// can be converted to and from JSON format
//When the API is called, the JSON response will be automatically deserialized (converted) into these Kotlin objects

//represents an individual restaurant from the list
@Serializable
data class RestaurantData(
    // @SerialName annotation is used when the JSON (snake_case) field name differs from the Kotlin (camelCase) property name

    @SerialName("delivery_time_minutes")
    val deliveryTimeMinutes: Int,
    val filterIds: List<String>,
    val id: String,
    @SerialName("image_url")
    val imageUrl: String,
    val name: String,
    val rating: Double
)

// wrapper class that contains a list of restaurants (entire api res)
@Serializable
data class RestaurantsResponse(
    val restaurants: List<RestaurantData>
)