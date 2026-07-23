package com.example.polisy_project.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.polisy_project.data.local.AppDatabase
import com.example.polisy_project.data.model.Driver
import com.example.polisy_project.data.model.Infraction
import com.example.polisy_project.data.model.Vehicle
import com.example.polisy_project.data.repository.PVRepository
import com.example.polisy_project.data.sync.SyncManager
import kotlinx.coroutines.launch

class PVViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = PVRepository(db)
    private val syncManager = SyncManager(application, db)

    var driverName by androidx.compose.runtime.mutableStateOf("")
    var driverLicense by androidx.compose.runtime.mutableStateOf("")
    var driverPhone by androidx.compose.runtime.mutableStateOf("")

    var vehiclePlate by androidx.compose.runtime.mutableStateOf("")
    var vehicleMake by androidx.compose.runtime.mutableStateOf("")
    var vehicleModel by androidx.compose.runtime.mutableStateOf("")
    var vehicleYear by androidx.compose.runtime.mutableStateOf("")

    var infractionType by androidx.compose.runtime.mutableStateOf("")
    var infractionDate by androidx.compose.runtime.mutableStateOf("")
    var infractionDescription by androidx.compose.runtime.mutableStateOf("")
    var infractionLocation by androidx.compose.runtime.mutableStateOf("")

    var pvState by androidx.compose.runtime.mutableStateOf<PVState>(PVState.Idle)
        private set

    fun submitPV(officerId: Int) {
        viewModelScope.launch {
            pvState = PVState.Loading
            val driver = Driver(
                name = driverName,
                license_number = driverLicense,
                phone = driverPhone
            )
            val vehicle = Vehicle(
                plate_number = vehiclePlate,
                make = vehicleMake,
                model = vehicleModel,
                year = vehicleYear.toIntOrNull() ?: 0
            )
            val result = repository.createPV(
                driver = driver,
                vehicle = vehicle,
                type = infractionType,
                date = infractionDate,
                description = infractionDescription,
                location = infractionLocation,
                officerId = officerId
            )
            result.fold(
                onSuccess = {
                    pvState = PVState.Success
                    clearForm()
                    syncManager.syncPendingItems()
                },
                onFailure = {
                    pvState = PVState.Offline
                }
            )
        }
    }

    fun resetState() { pvState = PVState.Idle }

    private fun clearForm() {
        driverName = ""
        driverLicense = ""
        driverPhone = ""
        vehiclePlate = ""
        vehicleMake = ""
        vehicleModel = ""
        vehicleYear = ""
        infractionType = ""
        infractionDate = ""
        infractionDescription = ""
        infractionLocation = ""
    }
}

sealed class PVState {
    data object Idle : PVState()
    data object Loading : PVState()
    data object Success : PVState()
    data object Offline : PVState()
}
