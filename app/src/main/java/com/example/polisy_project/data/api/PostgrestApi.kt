package com.example.polisy_project.data.api

import com.example.polisy_project.data.model.Driver
import com.example.polisy_project.data.model.Infraction
import com.example.polisy_project.data.model.Vehicle
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface PostgrestApi {

    @GET("drivers")
    suspend fun getDrivers(): List<Driver>

    @POST("drivers")
    suspend fun insertDriver(
        @Body driver: Driver,
        @Header("Prefer") prefer: String = "return=representation"
    ): Driver

    @GET("drivers/{id}")
    suspend fun getDriverById(@Path("id") id: Int): Driver

    @GET("vehicles")
    suspend fun getVehicles(): List<Vehicle>

    @POST("vehicles")
    suspend fun insertVehicle(
        @Body vehicle: Vehicle,
        @Header("Prefer") prefer: String = "return=representation"
    ): Vehicle

    @GET("vehicles/{id}")
    suspend fun getVehicleById(@Path("id") id: Int): Vehicle

    @GET("infractions")
    suspend fun getInfractions(): List<Infraction>

    @POST("infractions")
    suspend fun insertInfraction(@Body infraction: Infraction)

    @PATCH("infractions/{id}")
    suspend fun updateInfraction(@Path("id") id: Int, @Body infraction: Infraction)

    @DELETE("infractions/{id}")
    suspend fun deleteInfraction(@Path("id") id: Int)
}
