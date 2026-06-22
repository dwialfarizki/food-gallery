package com.dwialfa0010.foodgallery.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dwialfa0010.foodgallery.model.User

@Composable
fun ProfileDialog(
    user: User,
    onDismiss: () -> Unit,
    onLogout: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Profil")
        },

        text = {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                if (user.photoUrl.isNotEmpty()) {

                    AsyncImage(
                        model = user.photoUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = user.name
                )

                Text(
                    text = user.email
                )
            }
        },

        confirmButton = {

            TextButton(
                onClick = onLogout
            ) {
                Text("Logout")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("Tutup")
            }
        }
    )
}