package com.example.polisy_project.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.polisy_project.data.local.AppDatabase
import com.example.polisy_project.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)

    var badgeNumber = ""
    var password = ""

    var loginState by androidx.compose.runtime.mutableStateOf<LoginState>(LoginState.Idle)
        private set

    init {
        viewModelScope.launch {
            if (authRepository.restoreSession()) {
                loginState = LoginState.Restored
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            loginState = LoginState.Loading
            val result = authRepository.login(badgeNumber, password)
            result.fold(
                onSuccess = { loginState = LoginState.Success },
                onFailure = { loginState = LoginState.Error(it.message ?: "Login failed") }
            )
        }
    }

    suspend fun getOfficerId(): Int = authRepository.getOfficerId() ?: 0
    suspend fun getOfficerName(): String = authRepository.getOfficerName() ?: ""

    fun logout() {
        viewModelScope.launch { authRepository.logout() }
        loginState = LoginState.Idle
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data object Restored : LoginState()
    data class Error(val message: String) : LoginState()
}
