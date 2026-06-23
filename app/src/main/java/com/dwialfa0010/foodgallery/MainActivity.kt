package com.dwialfa0010.foodgallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dwialfa0010.foodgallery.network.ApiStatus
import com.dwialfa0010.foodgallery.network.UserDataStore
import com.dwialfa0010.foodgallery.ui.screen.AddFoodScreen
import com.dwialfa0010.foodgallery.ui.screen.FoodListScreen
import com.dwialfa0010.foodgallery.ui.screen.ProfileDialog
import com.dwialfa0010.foodgallery.ui.theme.FoodGalleryTheme
import com.dwialfa0010.foodgallery.viewmodel.AuthViewModel
import com.dwialfa0010.foodgallery.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            FoodGalleryTheme {

                val vm: MainViewModel = viewModel()
                val authVm: AuthViewModel = viewModel()

                val dataStore = remember {
                    UserDataStore(this)
                }
                val user by dataStore.userFlow.collectAsState(
                    initial = com.dwialfa0010.foodgallery.model.User()
                )
                var showAddScreen by remember {
                    mutableStateOf(false)
                }
                var showProfileDialog by remember {
                    mutableStateOf(false)
                }
                LaunchedEffect(user.email) {
                    vm.retrieveData(user.email)
                }

                if (showAddScreen) {
                    AddFoodScreen(
                        onAddClick = { foodName, description, imageUri ->
                            vm.addFood(
                                context = this@MainActivity,
                                email = user.email,
                                foodName = foodName,
                                description = description,
                                imageUri = imageUri
                            )
                            showAddScreen = false
                        },
                        onBackClick = {
                            showAddScreen = false
                        }
                    )

                } else {
                    val status by vm.status.collectAsState()
                    when (status) {
                        ApiStatus.LOADING -> {

                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        ApiStatus.SUCCESS -> {
                            FoodListScreen(
                                foods = vm.data.value,

                                onDelete = { id ->
                                    vm.deleteFood(
                                        email = user.email,
                                        id = id
                                    )
                                },

                                onAddClick = {
                                    showAddScreen = true
                                },

                                onProfileClick = {
                                    if (user.email.isEmpty()) {

                                        authVm.signIn(
                                            context = this@MainActivity,
                                            dataStore = dataStore
                                        )

                                    } else {

                                        showProfileDialog = true
                                    }
                                },

                                isLoggedIn = user.email.isNotEmpty()
                            )

                            if (showProfileDialog) {

                                ProfileDialog(
                                    user = user,
                                    onDismiss = {
                                        showProfileDialog = false
                                    },
                                    onLogout = {

                                        authVm.signOut(
                                            context = this@MainActivity,
                                            dataStore = dataStore
                                        )

                                        showProfileDialog = false
                                    }
                                )
                            }
                        }

                        ApiStatus.FAILED -> {

                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Text("Tidak dapat terhubung ke server")

                                    Spacer(
                                        modifier = Modifier.height(12.dp)
                                    )

                                    Button(
                                        onClick = {

                                            if (user.email.isNotEmpty()) {
                                                vm.retrieveData(user.email)
                                            }
                                        }
                                    ) {
                                        Text("Coba Lagi")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
