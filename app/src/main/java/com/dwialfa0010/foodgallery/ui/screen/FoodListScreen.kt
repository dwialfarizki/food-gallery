package com.dwialfa0010.foodgallery.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dwialfa0010.foodgallery.model.Food
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

private const val BASE_URL = "https://api-food-gallery-production.up.railway.app/"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodListScreen(
    foods: List<Food>,
    onDelete: (Int) -> Unit,
    onEdit: (Food) -> Unit,
    onAddClick: () -> Unit,
    onProfileClick: () -> Unit = {},
    isLoggedIn: Boolean
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Food Gallery",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6B8E6B)
                ),
                actions = {
                    IconButton(
                        onClick = onProfileClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Login",
                            tint = Color.White
                        )
                    }
                }
            )
        },

        floatingActionButton = {
            if (isLoggedIn) {
                FloatingActionButton(
                    onClick = onAddClick,
                    containerColor = Color(0xFF6B8E6B),
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah"
                    )
                }
            }
        }

    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(foods) { food ->
                var showDialog by remember {
                    mutableStateOf(false)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {

                    Column {

                        if (!food.image_url.isNullOrEmpty()) {
                            val imageUrl = if (food.image_url.startsWith("http")) {
                                food.image_url
                            } else {
                                "$BASE_URL/storage/${food.image_url.removePrefix("/")}"
                            }

                            AsyncImage(
                                model = imageUrl,
                                contentDescription = food.food_name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentScale = ContentScale.Crop,
                                // Tambahkan ini untuk menangkap error Coil jika terjadi
                                onError = {
                                    android.util.Log.e("CEK_URL", "Coil Error: ${it.result.throwable.message}")
                                }
                            )
                        }

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = food.food_name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = food.description ?: "-"
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {

                                if (!food.isPublicValue) {

                                    Button(
                                        onClick = { onEdit(food) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF6B8E6B)
                                        )
                                    ) {
                                        Text("Edit")
                                    }

                                    Spacer(
                                        modifier = Modifier.width(8.dp)
                                    )

                                    Button(
                                        onClick = { showDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)) // Warna merah untuk hapus
                                    ) { Text("Hapus") }
                                }

                                if (showDialog) {

                                    AlertDialog(
                                        onDismissRequest = {
                                            showDialog = false
                                        },

                                        title = {
                                            Text("Konfirmasi")
                                        },

                                        text = {
                                            Text("Yakin ingin menghapus makanan ini?")
                                        },

                                        confirmButton = {

                                            TextButton(
                                                onClick = {
                                                    onDelete(food.id)
                                                    showDialog = false
                                                }
                                            ) {
                                                Text("Ya")
                                            }
                                        },

                                        dismissButton = {

                                            TextButton(
                                                onClick = {
                                                    showDialog = false
                                                }
                                            ) {
                                                Text("Batal")
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}