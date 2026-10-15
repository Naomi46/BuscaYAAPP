package com.example.miapp.data.model

import com.google.gson.annotations.SerializedName

data class BusquedaResponse(
    @SerializedName("results") val results: List<NegocioResultDto> = emptyList()
)

data class NegocioResultDto(
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("ubicacion") val ubicacion: String = "",
    @SerializedName("telefono") val telefono: String = "",
    @SerializedName("rating") val rating: Double = 0.0,
    @SerializedName("imagen") val imagen: String = ""
) {
    fun toUiModel(): NegocioResult {
        return NegocioResult(
            nombre = this.nombre,
            ubicacion = this.ubicacion,
            telefono = this.telefono,
            rating = this.rating,
            imagen = this.imagen
        )
    }
}

data class NegocioResult(
    val nombre: String = "",
    val ubicacion: String = "",
    val telefono: String = "",
    val rating: Double = 0.0,
    val imagen: String = ""
)
