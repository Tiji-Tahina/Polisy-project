package com.example.polisy_project.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.polisy_project.data.api.RetrofitInstance
import com.example.polisy_project.data.model.LoginRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class AuthRepository(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val OFFICER_ID_KEY = stringPreferencesKey("officer_id")
        private val OFFICER_NAME_KEY = stringPreferencesKey("officer_name")
    }

    suspend fun login(badgeNumber: String, password: String): Result<String> {
        return try {
            val response = RetrofitInstance.api.login(LoginRequest(badgeNumber, password))
            RetrofitInstance.authToken = response.token
            context.dataStore.edit { it[TOKEN_KEY] = response.token }
            Result.success(response.token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun restoreSession(): Boolean {
        return try {
            val prefs = context.dataStore.data.map { it[TOKEN_KEY] }.first()
            if (prefs != null) {
                RetrofitInstance.authToken = prefs
                true
            } else false
        } catch (e: Exception) {
            false
        }
    }

    suspend fun saveOfficerInfo(id: Int, name: String) {
        context.dataStore.edit {
            it[OFFICER_ID_KEY] = id.toString()
            it[OFFICER_NAME_KEY] = name
        }
    }

    suspend fun getOfficerId(): Int? {
        return context.dataStore.data.map { it[OFFICER_ID_KEY]?.toIntOrNull() }.first()
    }

    suspend fun getOfficerName(): String? {
        return context.dataStore.data.map { it[OFFICER_NAME_KEY] }.first()
    }

    suspend fun logout() {
        context.dataStore.edit { it.clear() }
        RetrofitInstance.authToken = null
    }
}
