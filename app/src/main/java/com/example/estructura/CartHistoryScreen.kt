package com.example.historicocarritos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CartHistoryScreen(
    userRole: String, // "Auditor", "Admin" o "Cliente"
    viewModel: CartHistoryViewModel = viewModel()
) {
    // Escenario 2: Restricción de acceso para perfiles no autorizados
    if (userRole.lowercase() != "auditor" && userRole.lowercase() != "admin") {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Acceso Denegado: Esta vista es solo para Auditores.",
                color = Color.Red,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        return
    }

    val state by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFAF8F5)
    ) {
        when (val s = state) {
            is CartUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF2D4B3E))
                }
            }
            is CartUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = s.message, color = Color.Red)
                }
            }
            is CartUiState.Success -> {
                CartHistoryContent(
                    carts = s.carts,
                    onToggleExpand = { cartId -> viewModel.toggleExpand(cartId) }
                )
            }
        }
    }
}

@Composable
fun CartHistoryContent(
    carts: List<CartUI>,
    onToggleExpand: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Encabezado
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Histórico de Carritos",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "Monitoreo global de operaciones",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Box(
                    modifier = Modifier
                        .background(Color(0xFFE2F0D9), shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Modo Auditoría",
                        fontSize = 12.sp,
                        color = Color(0xFF2E6B38),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${carts.size} carritos registrados",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF374151)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Tarjetas
        items(carts) { cart ->
            CartItemCard(cart = cart, onToggleExpand = { onToggleExpand(cart.id) })
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun CartItemCard(
    cart: CartUI,
    onToggleExpand: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Carrito #${cart.id}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    Text(
                        text = "Propietario: Usuario ID ${cart.userId}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
                Text(
                    text = cart.date,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D5A43)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Botón Desplegable
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onToggleExpand() }
                    .padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = if (cart.isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFFB34A38),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (cart.isExpanded) "▲ Ocultar productos" else "▼ Ver desglose (${cart.items.size} artículos)",
                    color = Color(0xFFB34A38),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Desglose
            AnimatedVisibility(visible = cart.isExpanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    cart.items.forEach { product ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = product.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1F2937)
                                )
                                Text(
                                    text = "ID Producto: ${product.id}",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                            Text(
                                text = "Cant: ${product.quantity}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E3A2B)
                            )
                        }
                    }
                }
            }
        }
    }
}