package com.example.polisy_project.data.model

data class Infraction(
    val id: Int? = null,
    val driver_id: Int,
    val vehicle_id: Int,
    val officer_id: Int,
    val type: String,
    val date: String,
    val description: String,
    val location: String
)
