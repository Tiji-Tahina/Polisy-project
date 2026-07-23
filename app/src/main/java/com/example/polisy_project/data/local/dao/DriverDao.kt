package com.example.polisy_project.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.polisy_project.data.local.entity.DriverEntity

@Dao
interface DriverDao {
    @Query("SELECT * FROM drivers WHERE license_number = :license LIMIT 1")
    suspend fun findByLicense(license: String): DriverEntity?

    @Query("SELECT * FROM drivers WHERE id = :id LIMIT 1")
    suspend fun findById(id: Int): DriverEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(driver: DriverEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(drivers: List<DriverEntity>)

    @Query("DELETE FROM drivers")
    suspend fun deleteAll()
}
