package com.example.polisy_project.data.repository

import com.example.polisy_project.data.api.RetrofitInstance
import com.example.polisy_project.data.local.AppDatabase
import com.example.polisy_project.data.local.entity.InfractionEntity
import com.example.polisy_project.data.local.entity.SyncQueueItem
import com.example.polisy_project.data.model.Driver
import com.example.polisy_project.data.model.Infraction
import com.example.polisy_project.data.model.Vehicle
import com.google.gson.Gson

class PVRepository(private val db: AppDatabase) {

    private val api = RetrofitInstance.api
    private val gson = Gson()

    suspend fun createPV(
        driver: Driver,
        vehicle: Vehicle,
        type: String,
        date: String,
        description: String,
        location: String,
        officerId: Int
    ): Result<Unit> {
        return try {
            val savedDriver = api.insertDriver(driver)
            val savedVehicle = api.insertVehicle(vehicle)
            val infraction = Infraction(
                driver_id = savedDriver.id!!,
                vehicle_id = savedVehicle.id!!,
                officer_id = officerId,
                type = type,
                date = date,
                description = description,
                location = location
            )
            api.insertInfraction(infraction)

            db.driverDao().insert(savedDriver.toEntity())
            db.vehicleDao().insert(savedVehicle.toEntity())
            db.infractionDao().insert(infraction.toEntity())

            Result.success(Unit)
        } catch (e: Exception) {
            val infraction = Infraction(
                driver_id = driver.id ?: 0,
                vehicle_id = vehicle.id ?: 0,
                officer_id = officerId,
                type = type,
                date = date,
                description = description,
                location = location
            )
            db.infractionDao().insert(infraction.toEntity())
            db.syncQueueDao().insert(
                SyncQueueItem(
                    entityType = "infraction",
                    operation = "INSERT",
                    payload = gson.toJson(infraction)
                )
            )
            Result.failure(e)
        }
    }

    suspend fun getSyncQueueCount(): Int = db.syncQueueDao().count()

    private fun Driver.toEntity() = com.example.polisy_project.data.local.entity.DriverEntity(id!!, name, license_number, phone)
    private fun Vehicle.toEntity() = com.example.polisy_project.data.local.entity.VehicleEntity(id!!, plate_number, make, model, year)
    private fun Infraction.toEntity() = InfractionEntity(id!!, driver_id, vehicle_id, officer_id, type, date, description, location)
}
