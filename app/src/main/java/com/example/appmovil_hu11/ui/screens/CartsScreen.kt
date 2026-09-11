package com.example.appmovil_hu11.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

// Import del modelo de datos
import com.example.appmovil_hu11.data.CartUI

// Import de CartViewModel y CartUiState alojados en el paquete ui
import com.example.appmovil_hu11.ui.*

// Import del componente CartCard y estilos del tema
import com.example.appmovil_hu11.ui.components.CartCard
import com.example.appmovil_hu11.ui.theme.*

@Composable
fun CartsScreen(
    userRole: String = "Auditor",
    viewModel: CartViewModel = viewModel()
) {
    if (userRole == "Cliente") {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NovaBg),
            contentAlignment = Alignment.Center
        ) {
            Text("Acceso Restringido", color = NovaOrangeAction, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        return
    }

    Scaffold(containerColor = NovaBg) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Encabezado
            Text(
                text = "Histórico de Carritos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = NovaTextDark
            )
            Text(
                text = "Monitoreo global de operaciones",
                fontSize = 14.sp,
                color = NovaTextSub,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Etiqueta "Modo Auditoría"
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .background(NovaPillBg, shape = RoundedCornerShape(50))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Modo Auditoría",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NovaPillText
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (val state = viewModel.uiState) {
                is CartUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = NovaPillText)
                    }
                }
                is CartUiState.Success -> {
                    CartsListView(carts = state.carts)
                }
                is CartUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error al cargar carritos", color = NovaOrangeAction, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CartsListView(carts: List<CartUI>) {
    Column {
        Text(
            text = "${carts.size} carritos registrados",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = NovaTextDark,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(carts) { cart -> CartCard(cart = cart) }
        }
    }
}