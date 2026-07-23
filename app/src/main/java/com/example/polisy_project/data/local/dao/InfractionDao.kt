package com.example.polisy_project.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.polisy_project.data.local.entity.InfractionEntity

@Dao
interface InfractionDao {
    @Query("SELECT * FROM infractions WHERE driver_id = :driverId ORDER BY date DESC")
    suspend fun findByDriverId(driverId: Int): List<InfractionEntity>

    @Query("SELECT * FROM infractions WHERE vehicle_id = :vehicleId ORDER BY date DESC")
    suspend fun findByVehicleId(vehicleId: Int): List<InfractionEntity>

    @Query("SELECT * FROM infractions WHERE id = :id LIMIT 1")
    suspend fun findById(id: Int): InfractionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(infraction: InfractionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(infractions: List<InfractionEntity>)

    @Query("DELETE FROM infractions")
    suspend fun deleteAll()
}
