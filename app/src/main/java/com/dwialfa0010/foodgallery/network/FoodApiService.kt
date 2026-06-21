package com.dwialfa0010.foodgallery.network

import com.dwialfa0010.foodgallery.model.Food
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Path

interface FoodApiService {

    @GET("foods")
    suspend fun getFoods(
        @Query("email") email: String
    ): List<Food>

    @POST("foods")
    suspend fun addFood(
        @Body food: Food
    ): Food

    @DELETE("foods/{id}")
    suspend fun deleteFood(
        @Path("id") id: Int
    )
}