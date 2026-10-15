package com.example.miapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miapp.data.model.Farmacia
import com.example.miapp.data.model.NegocioResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val dao = AppDatabase.getDatabaseAuto().productoDao()
    private val repository = ProductoRepository(dao)

    private val _searchQuery = MutableStateFlow("")

    // Lee SIEMPRE de Room (y cuando escribís en el buscador, filtra con LIKE en el DAO)
    @OptIn(ExperimentalCoroutinesApi::class)
    val farmacias: StateFlow<List<Farmacia>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.farmaciasLocal
            } else {
                repository.buscarEnRoom(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Dejamos resultados siempre vacío para que tu MainActivity NUNCA oculte la lista de Farmacias de Room
    private val _resultados = MutableStateFlow<List<NegocioResult>>(emptyList())
    val resultados: StateFlow<List<NegocioResult>> = _resultados

    init {
        cargarFarmacias()
    }

    fun cargarFarmacias() {
        viewModelScope.launch {
            repository.actualizarDesdeApi()
        }
    }

    fun buscar(query: String) {
        _searchQuery.value = query
    }

    fun limpiarBusqueda() {
        _searchQuery.value = ""
        _resultados.value = emptyList()
    }
}