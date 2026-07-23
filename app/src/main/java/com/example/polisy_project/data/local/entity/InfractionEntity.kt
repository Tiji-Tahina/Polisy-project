package com.example.polisy_project.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "infractions")
data class InfractionEntity(
    @PrimaryKey val id: Int,
    val driver_id: Int,
    val vehicle_id: Int,
    val officer_id: Int,
    val type: String,
    val date: String,
    val description: String,
    val location: String
)
