package com.example.miapp.data.model

import com.google.gson.annotations.SerializedName

data class FarmaciasResponse(
    @SerializedName("farmacias") val farmacias: List<FarmaciaDto> = emptyList()
)

data class FarmaciaDto(
    @SerializedName("fecha_raw") val fechaRaw: String = "",
    @SerializedName("fecha_iso") val fechaIso: String = "",
    @SerializedName("dia_semana") val diaSemana: String = "",
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("direccion") val direccion: String = "",
    @SerializedName("imagen") val imagen: String = ""
) {
    fun toUiModel(): Farmacia {
        return Farmacia(
            fecha_raw = this.fechaRaw,
            fecha_iso = this.fechaIso,
            dia_semana = this.diaSemana,
            nombre = this.nombre,
            direccion = this.direccion,
            imagen = this.imagen
        )
    }
}
