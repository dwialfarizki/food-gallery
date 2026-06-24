package com.dwialfa0010.foodgallery.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://api-food-gallery-production.up.railway.app/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(
        MoshiConverterFactory.create(moshi)
    )
    .build()

object FoodApi {
    val service: FoodApiService by lazy {
        retrofit.create(FoodApiService::class.java)
    }
}