package com.dwialfa0010.foodgallery.network

import com.dwialfa0010.foodgallery.model.Food
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.PUT

interface FoodApiService {

    @GET("api/foods")
    suspend fun getFoods(
        @Query("email") email: String? = null
    ): List<Food>

    @Multipart
    @POST("api/foods")
    suspend fun addFood(
        @Part("user_email") userEmail: RequestBody,
        @Part("food_name") foodName: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part?
    ): Food

    @DELETE("api/foods/{id}")
    suspend fun deleteFood(
        @Path("id") id: Int
    )

    @Multipart
    @POST("api/foods/{id}?_method=PUT")
    suspend fun updateFood(
        @Path("id") id: Int,
        @Part("food_name") foodName: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part?
    ): Food
}