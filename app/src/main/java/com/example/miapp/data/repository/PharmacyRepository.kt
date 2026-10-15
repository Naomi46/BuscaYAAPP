package com.example.miapp.data.repository

import com.example.miapp.data.model.Farmacia
import com.example.miapp.data.model.NegocioResult
import com.example.miapp.data.remote.BuscayaApiService
import com.example.miapp.data.remote.RetrofitClient

class PharmacyRepository(
    private val api: BuscayaApiService = RetrofitClient.api
) {
    suspend fun getPharmaciesOnDuty(): Result<List<Farmacia>> = runCatching {
        api.getFarmaciasTurno()
            .farmacias
            .map { it.toUiModel() }
    }

    suspend fun buscarNegocios(query: String): Result<List<NegocioResult>> = runCatching {
        api.buscar(query)
            .results
            .map { it.toUiModel() }
    }
}
