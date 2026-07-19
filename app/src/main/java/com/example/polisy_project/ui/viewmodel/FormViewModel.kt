package com.example.polisy_project.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.polisy_project.data.model.Driver
import com.example.polisy_project.data.model.Infraction
import com.example.polisy_project.data.model.Vehicle
import com.example.polisy_project.data.repository.InfractionRepository
import kotlinx.coroutines.launch

class FormViewModel : ViewModel() {

    private val repository = InfractionRepository()

    var driverName by mutableStateOf("")
    var driverLicense by mutableStateOf("")
    var driverPhone by mutableStateOf("")

    var vehiclePlate by mutableStateOf("")
    var vehicleMake by mutableStateOf("")
    var vehicleModel by mutableStateOf("")
    var vehicleYear by mutableStateOf("")

    var infractionType by mutableStateOf("")
    var infractionDate by mutableStateOf("")
    var infractionDescription by mutableStateOf("")

    var saveSuccess by mutableStateOf<Boolean?>(null)
        private set

    fun submitInfraction() {
        viewModelScope.launch {
            try {
                val driver = repository.insertDriver(
                    Driver(
                        name = driverName,
                        license_number = driverLicense,
                        phone = driverPhone
                    )
                )

                val vehicle = repository.insertVehicle(
                    Vehicle(
                        plate_number = vehiclePlate,
                        make = vehicleMake,
                        model = vehicleModel,
                        year = vehicleYear.toIntOrNull() ?: 0
                    )
                )

                repository.insertInfraction(
                    Infraction(
                        driver_id = driver.id!!,
                        vehicle_id = vehicle.id!!,
                        type = infractionType,
                        date = infractionDate,
                        description = infractionDescription
                    )
                )

                saveSuccess = true
                clearForm()
            } catch (e: Exception) {
                saveSuccess = false
            }
        }
    }

    fun resetSaveStatus() {
        saveSuccess = null
    }

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
    }
}
