package com.dwialfa0010.foodgallery.network

import com.dwialfa0010.foodgallery.model.Food
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodApiService {

    @GET("foods")
    suspend fun getFoods(
        @Query("email") email: String
    ): List<Food>

    @Multipart
    @POST("foods")
    suspend fun addFood(
        @Part("user_email") userEmail: RequestBody,
        @Part("food_name") foodName: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part?
    ): Food

    @DELETE("foods/{id}")
    suspend fun deleteFood(
        @Path("id") id: Int
    )
}