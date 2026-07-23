package com.example.polisy_project.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.polisy_project.data.api.RetrofitInstance
import com.example.polisy_project.data.model.LoginRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class AuthRepository(private val context: Context) {

    companion object {
        private val OFFICER_ID_KEY = intPreferencesKey("officer_id")
        private val OFFICER_NAME_KEY = stringPreferencesKey("officer_name")
        private val BADGE_KEY = stringPreferencesKey("badge_number")
    }

    suspend fun login(badgeNumber: String, password: String): Result<Unit> {
        return try {
            val response = RetrofitInstance.api.login(LoginRequest(badgeNumber, password))
            context.dataStore.edit {
                it[OFFICER_ID_KEY] = response.id
                it[OFFICER_NAME_KEY] = response.name
                it[BADGE_KEY] = response.badge_number
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun restoreSession(): Boolean {
        return try {
            val id = context.dataStore.data.map { it[OFFICER_ID_KEY] }.first()
            id != null && id > 0
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getOfficerId(): Int {
        return context.dataStore.data.map { it[OFFICER_ID_KEY] ?: 0 }.first()
    }

    suspend fun getOfficerName(): String {
        return context.dataStore.data.map { it[OFFICER_NAME_KEY] ?: "" }.first()
    }

    suspend fun logout() {
        context.dataStore.edit { it.clear() }
    }
}
