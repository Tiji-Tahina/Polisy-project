package com.example.polisy_project.data.repository

import com.example.polisy_project.data.api.RetrofitInstance
import com.example.polisy_project.data.model.Driver
import com.example.polisy_project.data.model.Infraction
import com.example.polisy_project.data.model.Vehicle

class InfractionRepository {

    private val api = RetrofitInstance.api

    suspend fun getDrivers(): List<Driver> = api.getDrivers()
    suspend fun insertDriver(driver: Driver) = api.insertDriver(driver)
    suspend fun getDriverById(id: Int): Driver = api.getDriverById(id)

    suspend fun getVehicles(): List<Vehicle> = api.getVehicles()
    suspend fun insertVehicle(vehicle: Vehicle) = api.insertVehicle(vehicle)
    suspend fun getVehicleById(id: Int): Vehicle = api.getVehicleById(id)

    suspend fun getInfractions(): List<Infraction> = api.getInfractions()
    suspend fun insertInfraction(infraction: Infraction) = api.insertInfraction(infraction)
    suspend fun updateInfraction(id: Int, infraction: Infraction) = api.updateInfraction(id, infraction)
    suspend fun deleteInfraction(id: Int) = api.deleteInfraction(id)
}
