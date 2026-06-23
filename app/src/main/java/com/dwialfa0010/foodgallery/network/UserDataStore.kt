package com.dwialfa0010.foodgallery.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dwialfa0010.foodgallery.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preference"
)

class UserDataStore(
    private val context: Context
) {

    companion object {
        private val USER_NAME = stringPreferencesKey("name")
        private val USER_EMAIL = stringPreferencesKey("email")
        private val USER_PHOTO = stringPreferencesKey("photoUrl")
    }

    val userFlow: Flow<User> = context.dataStore.data.map { pref ->
        User(
            name = pref[USER_NAME] ?: "",
            email = pref[USER_EMAIL] ?: "",
            photoUrl = pref[USER_PHOTO] ?: ""
        )
    }

    suspend fun saveData(user: User) {
        context.dataStore.edit { pref ->
            pref[USER_NAME] = user.name
            pref[USER_EMAIL] = user.email
            pref[USER_PHOTO] = user.photoUrl
        }
    }
}