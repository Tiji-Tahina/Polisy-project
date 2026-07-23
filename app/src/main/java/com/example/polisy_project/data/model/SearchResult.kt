package com.example.polisy_project.data.model

data class SearchResult(
    val driver: Driver?,
    val vehicle: Vehicle?,
    val infractions: List<Infraction>
)
