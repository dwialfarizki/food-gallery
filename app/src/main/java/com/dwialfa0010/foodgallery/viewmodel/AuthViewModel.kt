package com.dwialfa0010.foodgallery.viewmodel

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwialfa0010.foodgallery.BuildConfig
import com.dwialfa0010.foodgallery.model.User
import com.dwialfa0010.foodgallery.network.UserDataStore
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    fun signIn(
        context: Context,
        dataStore: UserDataStore
    ) {
        viewModelScope.launch {

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.API_KEY)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            try {

                val credentialManager =
                    CredentialManager.create(context)

                val result =
                    credentialManager.getCredential(
                        context,
                        request
                    )

                handleSignIn(
                    result,
                    dataStore
                )

            } catch (e: GetCredentialException) {

                Log.e(
                    "SIGN-IN",
                    e.errorMessage?.toString() ?: ""
                )
            }
        }
    }

    private suspend fun handleSignIn(
        result: GetCredentialResponse,
        dataStore: UserDataStore
    ) {

        val credential = result.credential

        if (
            credential is CustomCredential &&
            credential.type ==
            GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {

            try {

                val googleId =
                    GoogleIdTokenCredential.createFrom(
                        credential.data
                    )

                dataStore.saveData(
                    User(
                        name = googleId.displayName ?: "",
                        email = googleId.id,
                        photoUrl = googleId.profilePictureUri?.toString() ?: ""
                    )
                )

            } catch (e: GoogleIdTokenParsingException) {

                Log.e(
                    "SIGN-IN",
                    e.message ?: ""
                )
            }
        }
    }

    fun signOut(
        context: Context,
        dataStore: UserDataStore
    ) {

        viewModelScope.launch {

            try {

                CredentialManager.create(context)
                    .clearCredentialState(
                        ClearCredentialStateRequest()
                    )

                dataStore.saveData(
                    User()
                )

            } catch (e: ClearCredentialException) {

                Log.e(
                    "SIGN-IN",
                    e.errorMessage?.toString() ?: ""
                )
            }
        }
    }
}