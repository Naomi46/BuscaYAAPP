package com.example.miapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

class DetalleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val nombre = intent.getStringExtra("NOMBRE") ?: ""
        val direccion = intent.getStringExtra("DIRECCION") ?: ""
        val fecha = intent.getStringExtra("FECHA") ?: ""
        val telefono = intent.getStringExtra("TELEFONO") ?: ""
        val imagen = intent.getStringExtra("IMAGEN") ?: ""

        setContent {
            DetalleScreen(
                nombre = nombre,
                direccion = direccion,
                fecha = fecha,
                telefono = telefono,
                imagen = imagen,
                onBackClick = { finish() }
            )
        }
    }
}

@Composable
fun DetalleScreen(
    nombre: String,
    direccion: String,
    fecha: String,
    telefono: String,
    imagen: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val iniciales = nombre.split(" ")
        .take(2).mapNotNull { it.firstOrNull()?.toString() }.joinToString("")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1B5E20), Color(0xFF4CAF50))
                )
            )
    ) {
        // Top bar con flecha mejorada
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.White.copy(alpha = 0.15f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Detalle del Local",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Sección del avatar/imagen central
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (imagen.isNotEmpty()) {
                AsyncImage(
                    model = imagen,
                    contentDescription = nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape)
                        .shadow(8.dp, CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        .border(3.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iniciales,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 38.sp
                    )
                }
            }
        }

        // Tarjeta blanca de contenido
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .shadow(6.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título del negocio/farmacia
                Text(
                    text = nombre,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1B5E20),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                // Etiqueta distintiva
                val badgeText = if (fecha.isNotEmpty()) "🏥 Farmacia de Turno" else "💼 Local Comercial"
                val badgeBg = if (fecha.isNotEmpty()) Color(0xFFE8F5E9) else Color(0xFFE3F2FD)
                val badgeTxtColor = if (fecha.isNotEmpty()) Color(0xFF2E7D32) else Color(0xFF1565C0)

                Box(
                    modifier = Modifier
                        .background(badgeBg, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeTxtColor
                    )
                }

                Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                // Dirección
                if (direccion.isNotEmpty()) {
                    val cleanAddress = direccion.split("–", "-")[0].trim()
                    InfoItem(
                        icon = Icons.Default.Place,
                        title = "Dirección",
                        value = direccion,
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$cleanAddress, Villa del Rosario, Córdoba"))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "No se pudo abrir el mapa", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }

                // Fecha de Turno (si aplica)
                if (fecha.isNotEmpty()) {
                    InfoItem(
                        icon = Icons.Default.DateRange,
                        title = "Fecha de Turno",
                        value = fecha
                    )
                }

                // Teléfono
                val telefonoMostrado = if (telefono.isNotEmpty()) {
                    telefono
                } else {
                    val partes = direccion.split("–", "-")
                    if (partes.size > 1) partes[1].trim() else ""
                }

                if (telefonoMostrado.isNotEmpty()) {
                    InfoItem(
                        icon = Icons.Default.Phone,
                        title = "Teléfono",
                        value = telefonoMostrado,
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telefonoMostrado"))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "No se pudo iniciar la llamada", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InfoItem(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: (() -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0)),
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFE8F5E9), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    fontWeight = FontWeight.Medium
                )
            }
            if (onClick != null) {
                Text(
                    text = "›",
                    fontSize = 20.sp,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}