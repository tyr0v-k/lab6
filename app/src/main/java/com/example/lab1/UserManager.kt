package com.example.lab1

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserManager (
    private val context: Context
) {
    companion object {
        val USER_NAME_KEY = stringPreferencesKey("USER_NAME")
    }

    suspend fun storeUser(name: String) {
        context.myDataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
            Log.d("MANAGER_LOGGER","Logged as " + preferences[USER_NAME_KEY])
        }
    }
    suspend fun logOut() {
        context.myDataStore.edit{
            preferences -> preferences.remove(USER_NAME_KEY)
        }
    }

    suspend fun getUser(): String{
        Log.d("MANAGER_LOGGER","Getting " + context.myDataStore.data.first()[USER_NAME_KEY])
        return context.myDataStore.data.first()[USER_NAME_KEY] ?: ""}
    }
