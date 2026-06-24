package com.dwialfa0010.foodgallery.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dwialfa0010.foodgallery.model.Food
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import java.util.Objects
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

private const val BASE_URL = "https://api-food-gallery-production.up.railway.app/"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(
    food: Food? = null,
    onAddClick: (String, String, Uri?) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var foodName by remember { mutableStateOf(food?.food_name ?: "") }
    var description by remember { mutableStateOf(food?.description ?: "") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    fun getTempUri(): Uri {
        val tempFile = File.createTempFile("food_photo", ".jpg", context.externalCacheDir)
        return FileProvider.getUriForFile(Objects.requireNonNull(context), "${context.packageName}.provider", tempFile)
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) imageUri = tempPhotoUri
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) imageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (food == null) "Tambah Makanan" else "Edit Makanan",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6B8E6B)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            OutlinedTextField(
                value = foodName,
                onValueChange = { foodName = it },
                label = { Text("Nama Makanan") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val uri = getTempUri()
                    tempPhotoUri = uri
                    cameraLauncher.launch(uri)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6B8E6B)
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Ambil Foto Kamera")
            }

            Button(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6B8E6B)
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Pilih dari Galeri")
            }

            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )
            } else if (!food?.image_url.isNullOrEmpty()) {
                val imageUrl = if (food?.image_url?.startsWith("http") == true) {
                    food.image_url
                } else {
                    "$BASE_URL/storage/${food?.image_url?.removePrefix("/")}"
                }

                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Food Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Button(
                onClick = {
                    onAddClick(foodName, description, imageUri)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6B8E6B)
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = if (food == null) "Tambah" else "Update",
                    color = Color.White
                )
            }
        }
    }
}