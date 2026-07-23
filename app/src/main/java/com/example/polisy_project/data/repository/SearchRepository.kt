package com.example.polisy_project.data.repository

import com.example.polisy_project.data.api.RetrofitInstance
import com.example.polisy_project.data.local.AppDatabase
import com.example.polisy_project.data.local.entity.DriverEntity
import com.example.polisy_project.data.local.entity.InfractionEntity
import com.example.polisy_project.data.local.entity.VehicleEntity
import com.example.polisy_project.data.model.Driver
import com.example.polisy_project.data.model.Infraction
import com.example.polisy_project.data.model.SearchResult
import com.example.polisy_project.data.model.Vehicle

class SearchRepository(private val db: AppDatabase) {

    private val api = RetrofitInstance.api

    suspend fun searchByLicense(license: String): SearchResult {
        return try {
            val drivers = api.searchDriversByLicense(license)
            val driver = drivers.firstOrNull()
            if (driver != null) {
                db.driverDao().insert(driver.toEntity())
                val infractions = api.getInfractionsByDriver(driver.id!!)
                infractions.forEach { db.infractionDao().insert(it.toEntity()) }
                val vehicle = infractions.firstOrNull()?.let { api.getVehicleById(it.vehicle_id) }
                vehicle?.let { db.vehicleDao().insert(it.toEntity()) }
                SearchResult(driver, vehicle, infractions)
            } else {
                SearchResult(null, null, emptyList())
            }
        } catch (e: Exception) {
            val localDriver = db.driverDao().findByLicense(license)
            if (localDriver != null) {
                val localInfractions = db.infractionDao().findByDriverId(localDriver.id)
                val vehicle = localInfractions.firstOrNull()?.let { db.vehicleDao().findById(it.vehicle_id) }
                SearchResult(
                    localDriver.toModel(),
                    vehicle?.toModel(),
                    localInfractions.map { it.toModel() }
                )
            } else {
                SearchResult(null, null, emptyList())
            }
        }
    }

    suspend fun searchByPlate(plate: String): SearchResult {
        return try {
            val vehicles = api.searchVehiclesByPlate(plate)
            val vehicle = vehicles.firstOrNull()
            if (vehicle != null) {
                db.vehicleDao().insert(vehicle.toEntity())
                val infractions = api.getInfractionsByVehicle(vehicle.id!!)
                infractions.forEach { db.infractionDao().insert(it.toEntity()) }
                val driver = infractions.firstOrNull()?.let { api.getDriverById(it.driver_id) }
                driver?.let { db.driverDao().insert(it.toEntity()) }
                SearchResult(driver, vehicle, infractions)
            } else {
                SearchResult(null, null, emptyList())
            }
        } catch (e: Exception) {
            val localVehicle = db.vehicleDao().findByPlate(plate)
            if (localVehicle != null) {
                val localInfractions = db.infractionDao().findByVehicleId(localVehicle.id)
                val driver = localInfractions.firstOrNull()?.let { db.driverDao().findById(it.driver_id) }
                SearchResult(
                    driver?.toModel(),
                    localVehicle.toModel(),
                    localInfractions.map { it.toModel() }
                )
            } else {
                SearchResult(null, null, emptyList())
            }
        }
    }

    private fun Driver.toEntity() = DriverEntity(id!!, name, license_number, phone)
    private fun DriverEntity.toModel() = Driver(id, name, license_number, phone)
    private fun Vehicle.toEntity() = VehicleEntity(id!!, plate_number, make, model, year)
    private fun VehicleEntity.toModel() = Vehicle(id, plate_number, make, model, year)
    private fun Infraction.toEntity() = InfractionEntity(id!!, driver_id, vehicle_id, officer_id, type, date, description, location)
    private fun InfractionEntity.toModel() = Infraction(id, driver_id, vehicle_id, officer_id, type, date, description, location)
}
