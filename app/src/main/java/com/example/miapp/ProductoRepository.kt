package com.example.miapp

import com.example.miapp.data.model.Farmacia
import com.example.miapp.data.repository.PharmacyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ProductoRepository(private val dao: ProductoDao) {
    private val remoteRepository = PharmacyRepository()

    // Única fuente de verdad: la UI siempre lee de Room
    val farmaciasLocal: Flow<List<Farmacia>> = dao.getAll().map { lista ->
        lista.map { it.toFarmacia() }
    }

    // Buscador directo en Room con LIKE
    fun buscarEnRoom(query: String): Flow<List<Farmacia>> {
        return dao.buscar(query).map { lista ->
            lista.map { it.toFarmacia() }
        }
    }

    // Baja de la API y guarda en Room
    suspend fun actualizarDesdeApi() {
        remoteRepository.getPharmaciesOnDuty()
            .onSuccess { listaApi ->
                if (listaApi.isNotEmpty()) {
                    val entidades = listaApi.map { farmacia ->
                        ProductoEntity(
                            id = "${farmacia.nombre}_${farmacia.fecha_raw}",
                            nombre = farmacia.nombre,
                            direccion = farmacia.direccion,
                            fecha_raw = farmacia.fecha_raw,
                            fecha_iso = farmacia.fecha_iso,
                            dia_semana = farmacia.dia_semana,
                            imagen = farmacia.imagen
                        )
                    }
                    dao.insertAll(entidades)
                } else {
                    insertarRespaldoSiEstaVacio()
                }
            }
            .onFailure {
                // Si arranca en modo avión y Room todavía está vacío, carga datos locales en Room
                insertarRespaldoSiEstaVacio()
            }
    }

    private suspend fun insertarRespaldoSiEstaVacio() {
        if (dao.getAll().first().isEmpty()) {
            dao.insertAll(
                listOf(
                    ProductoEntity(
                        id = "1",
                        nombre = "Farmacia Sosa",
                        direccion = "25 de Mayo 850 – 3573-422111",
                        fecha_raw = "15/10/2026",
                        fecha_iso = "2026-10-15",
                        dia_semana = "Jueves",
                        imagen = ""
                    ),
                    ProductoEntity(
                        id = "2",
                        nombre = "Farmacia San José",
                        direccion = "San Martín 520 – 3573-423333",
                        fecha_raw = "16/10/2026",
                        fecha_iso = "2026-10-16",
                        dia_semana = "Viernes",
                        imagen = ""
                    ),
                    ProductoEntity(
                        id = "3",
                        nombre = "Farmacia Del Rosario",
                        direccion = "Belgrano 310 – 3573-424444",
                        fecha_raw = "17/10/2026",
                        fecha_iso = "2026-10-17",
                        dia_semana = "Sábado",
                        imagen = ""
                    )
                )
            )
        }
    }
}