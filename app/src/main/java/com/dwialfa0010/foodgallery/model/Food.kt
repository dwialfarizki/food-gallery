package com.dwialfa0010.foodgallery.model

import com.squareup.moshi.Json

data class Food(
    val id: Int = 0,
    @Json(name = "user_email") val user_email: String?,
    @Json(name = "food_name") val food_name: String,
    val description: String?,
    @Json(name = "image_url") val image_url: String?,
    @Json(name = "is_public") val is_public: Any? = 0
) {
    // Properti ini yang Anda gunakan di UI, jangan pakai is_public langsung
    val isPublicValue: Boolean
        get() {
            return when (val value = is_public) {
                is Boolean -> value
                is Number -> value.toInt() == 1
                is String -> value == "1" || value.equals("true", ignoreCase = true)
                else -> false
            }
        }
}