package com.example.miapp.data.remote

import com.example.miapp.data.model.BusquedaResponse
import com.example.miapp.data.model.FarmaciasResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BuscayaApiService {
    @GET("farmacias_turno_android")
    suspend fun getFarmaciasTurno(): FarmaciasResponse

    @GET("hybrid-search-android")
    suspend fun buscar(
        @Query("q") query: String,
        @Query("min_results") minResults: Int = 3,
        @Query("lat") lat: Double = -31.5486,
        @Query("lng") lng: Double = -63.5533,
        @Query("city") city: String = "Villa del Rosario",
        @Query("state") state: String = "Córdoba",
        @Query("country") country: String = "Argentina"
    ): BusquedaResponse
}
