package com.example.polisy_project.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.polisy_project.data.local.AppDatabase
import com.example.polisy_project.data.model.Driver
import com.example.polisy_project.data.model.Infraction
import com.example.polisy_project.data.model.SearchResult
import com.example.polisy_project.data.model.Vehicle
import com.example.polisy_project.data.repository.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = SearchRepository(db)

    var searchQuery by androidx.compose.runtime.mutableStateOf("")
    var searchMode by androidx.compose.runtime.mutableStateOf(SearchMode.LICENSE)

    var searchResult by androidx.compose.runtime.mutableStateOf<SearchResult?>(null)
        private set

    var searchState by androidx.compose.runtime.mutableStateOf<SearchState>(SearchState.Idle)
        private set

    fun search() {
        if (searchQuery.isBlank()) return
        viewModelScope.launch {
            searchState = SearchState.Loading
            searchResult = try {
                val result = when (searchMode) {
                    SearchMode.LICENSE -> repository.searchByLicense(searchQuery.trim())
                    SearchMode.PLATE -> repository.searchByPlate(searchQuery.trim())
                }
                searchState = SearchState.Result
                result
            } catch (e: Exception) {
                searchState = SearchState.Error(e.message ?: "Search failed")
                null
            }
        }
    }
}

enum class SearchMode { LICENSE, PLATE }

sealed class SearchState {
    data object Idle : SearchState()
    data object Loading : SearchState()
    data object Result : SearchState()
    data class Error(val message: String) : SearchState()
}
