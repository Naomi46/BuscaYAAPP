package com.example.miapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.miapp.data.model.Farmacia

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val direccion: String,
    val fecha_raw: String,
    val fecha_iso: String,
    val dia_semana: String,
    val imagen: String
) {
    fun toFarmacia(): Farmacia {
        return Farmacia(
            fecha_raw = fecha_raw,
            fecha_iso = fecha_iso,
            dia_semana = dia_semana,
            nombre = nombre,
            direccion = direccion,
            imagen = imagen
        )
    }
}