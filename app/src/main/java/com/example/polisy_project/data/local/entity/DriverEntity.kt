package com.example.polisy_project.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drivers")
data class DriverEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val license_number: String,
    val phone: String
)
