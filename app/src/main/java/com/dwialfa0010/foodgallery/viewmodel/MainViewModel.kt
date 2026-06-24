package com.dwialfa0010.foodgallery.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwialfa0010.foodgallery.model.Food
import com.dwialfa0010.foodgallery.network.ApiStatus
import com.dwialfa0010.foodgallery.network.FoodApi
import com.dwialfa0010.foodgallery.util.uriToFile
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Food>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    // Menambahkan Job untuk mengelola siklus hidup request API
    private var fetchJob: Job? = null

    fun retrieveData(email: String) {
        // Membatalkan request yang sedang berjalan agar tidak terjadi tabrakan/race condition
        fetchJob?.cancel()

        Log.d("EMAIL", "retrieveData dipanggil email=$email")

        fetchJob = viewModelScope.launch {
            status.value = ApiStatus.LOADING
            try {
                data.value = FoodApi.service.getFoods(email)
                Log.d("EMAIL", "hasil email=$email jumlah=${data.value.size}")
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.e("API", e.message ?: "Unknown Error")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun addFood(
        context: Context,
        email: String,
        foodName: String,
        description: String,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            try {
                val emailBody = email.toRequestBody("text/plain".toMediaType())
                val foodNameBody = foodName.toRequestBody("text/plain".toMediaType())
                val descriptionBody = description.toRequestBody("text/plain".toMediaType())

                var imagePart: MultipartBody.Part? = null
                if (imageUri != null) {
                    val file = uriToFile(imageUri, context)
                    val requestFile = file.asRequestBody("image/*".toMediaType())
                    imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
                }

                FoodApi.service.addFood(emailBody, foodNameBody, descriptionBody, imagePart)
                retrieveData(email)
            } catch (e: Exception) {
                Log.e("POST", e.message ?: "Error Upload")
            }
        }
    }

    fun deleteFood(email: String, id: Int) {
        viewModelScope.launch {
            try {
                FoodApi.service.deleteFood(id)
                retrieveData(email)
            } catch (e: Exception) {
                Log.e("DELETE", e.message ?: "Error")
            }
        }
    }

    fun updateFood(
        context: Context,
        email: String,
        id: Int,
        foodName: String,
        description: String,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            try {
                val foodNameBody = foodName.toRequestBody("text/plain".toMediaType())
                val descriptionBody = description.toRequestBody("text/plain".toMediaType())

                var imagePart: MultipartBody.Part? = null
                if (imageUri != null) {
                    val file = uriToFile(imageUri, context)
                    val requestFile = file.asRequestBody("image/*".toMediaType())
                    imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
                }

                FoodApi.service.updateFood(id, foodNameBody, descriptionBody, imagePart)
                retrieveData(email)
            } catch (e: Exception) {
                Log.e("UPDATE", e.message ?: "Error")
            }
        }
    }

    fun clearData() {
        data.value = emptyList()
        status.value = ApiStatus.SUCCESS
    }
}