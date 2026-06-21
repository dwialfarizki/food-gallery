package com.dwialfa0010.foodgallery.model

data class Food(
    val id: Int = 0,
    val user_email: String,
    val food_name: String,
    val description: String?,
    val image_url: String?
)