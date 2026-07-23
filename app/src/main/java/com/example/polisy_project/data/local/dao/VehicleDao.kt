package com.example.polisy_project.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.polisy_project.data.local.entity.VehicleEntity

@Dao
interface VehicleDao {
    @Query("SELECT * FROM vehicles WHERE plate_number = :plate LIMIT 1")
    suspend fun findByPlate(plate: String): VehicleEntity?

    @Query("SELECT * FROM vehicles WHERE id = :id LIMIT 1")
    suspend fun findById(id: Int): VehicleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vehicle: VehicleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vehicles: List<VehicleEntity>)

    @Query("DELETE FROM vehicles")
    suspend fun deleteAll()
}
