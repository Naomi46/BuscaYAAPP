package com.example.miapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.miapp.data.model.Farmacia
import com.example.miapp.data.model.NegocioResult

val VerdePrincipal = Color(0xFF2E7D32)
val VerdeClaro = Color(0xFF4CAF50)
val VerdeFondo = Color(0xFFF1F8E9)
val VerdeOscuro = Color(0xFF1B5E20)
val VerdeAccent = Color(0xFF81C784)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PantallaPrincipal() }
    }
}

@Composable
fun PantallaPrincipal(viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val farmacias by viewModel.farmacias.collectAsState()
    val resultados by viewModel.resultados.collectAsState()
    var textoBusqueda by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(VerdeFondo)) {

        // Header con gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(VerdeOscuro, VerdeClaro)
                    )
                )
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()) {
                Text(
                    "🔍 Busca Ya",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    "Villa del Rosario",
                    fontSize = 14.sp,
                    color = VerdeAccent
                )
                Spacer(modifier = Modifier.height(20.dp))
// Buscador
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = {
                        textoBusqueda = it
                        viewModel.buscar(it) // Filtra en tiempo real en Room con LIKE
                    },
                    placeholder = { Text("¿Qué estás buscando?", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(30.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true,
                    trailingIcon = {
                        if (textoBusqueda.isNotEmpty() || resultados.isNotEmpty()) {
                            IconButton(onClick = {
                                textoBusqueda = ""
                                viewModel.limpiarBusqueda()
                            }) {
                                Text("✕", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { viewModel.buscar(textoBusqueda) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeOscuro),
                    shape = RoundedCornerShape(30.dp),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Text("🔍 Buscar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (resultados.isNotEmpty()) {
                item {
                    SectionTitle("Resultados de búsqueda")
                }
                items(resultados) { negocio ->
                    CardNegocio(negocio = negocio, onClick = {
                        val intent = Intent(context, DetalleActivity::class.java)
                        intent.putExtra("NOMBRE", negocio.nombre)
                        intent.putExtra("DIRECCION", negocio.ubicacion)
                        intent.putExtra("TELEFONO", negocio.telefono)
                        intent.putExtra("IMAGEN", negocio.imagen)
                        context.startActivity(intent)
                    })
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            textoBusqueda = ""
                            viewModel.limpiarBusqueda()
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeOscuro),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Text("← Volver al listado principal", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                item { SectionTitle("🏥 Farmacias de turno") }
                items(farmacias) { farmacia ->
                    CardFarmacia(farmacia = farmacia, onClick = {
                        val intent = Intent(context, DetalleActivity::class.java)
                        intent.putExtra("NOMBRE", farmacia.nombre)
                        intent.putExtra("DIRECCION", farmacia.direccion)
                        intent.putExtra("FECHA", farmacia.fecha_raw)
                        intent.putExtra("IMAGEN", farmacia.imagen)
                        context.startActivity(intent)
                    })
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = VerdeOscuro,
        modifier = Modifier.padding(vertical = 6.dp)
    )
}

@Composable
fun CardFarmacia(farmacia: Farmacia, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            val iniciales = farmacia.nombre.split(" ")
                .take(2).mapNotNull { it.firstOrNull()?.toString() }.joinToString("")
            if (farmacia.imagen.isNotEmpty()) {
                AsyncImage(
                    model = farmacia.imagen,
                    contentDescription = farmacia.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(56.dp).clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier.size(56.dp).background(VerdeClaro, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(iniciales, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(farmacia.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1A1A1A))
                Spacer(modifier = Modifier.height(2.dp))
                Text(farmacia.direccion, fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .background(VerdeFondo, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(farmacia.fecha_raw, fontSize = 11.sp, color = VerdePrincipal, fontWeight = FontWeight.SemiBold)
                }
            }
            Text("›", fontSize = 24.sp, color = VerdeAccent)
        }
    }
}

@Composable
fun CardNegocio(negocio: NegocioResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            val iniciales = negocio.nombre.split(" ")
                .take(2).mapNotNull { it.firstOrNull()?.toString() }.joinToString("")
            if (negocio.imagen.isNotEmpty()) {
                AsyncImage(
                    model = negocio.imagen,
                    contentDescription = negocio.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(56.dp).clip(RoundedCornerShape(12.dp))
                )
            } else {
                Box(
                    modifier = Modifier.size(56.dp).background(VerdeOscuro, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(iniciales, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(negocio.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1A1A1A))
                Spacer(modifier = Modifier.height(2.dp))
                Text(negocio.ubicacion, fontSize = 13.sp, color = Color.Gray)
                if (negocio.rating > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment =









































































































                        Alignment.CenterVertically) {
                        Text("⭐", fontSize = 12.sp)
                        Text(" ${negocio.rating}", fontSize = 12.sp, color = Color(0xFFFF8F00), fontWeight = FontWeight.Bold)
                    }
                }
            }
            Text("›", fontSize = 24.sp, color = VerdeAccent)
        }
    }
}