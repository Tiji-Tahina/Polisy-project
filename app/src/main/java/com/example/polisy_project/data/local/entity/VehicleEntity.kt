package com.example.polisy_project.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey val id: Int,
    val plate_number: String,
    val make: String,
    val model: String,
    val year: Int
)
