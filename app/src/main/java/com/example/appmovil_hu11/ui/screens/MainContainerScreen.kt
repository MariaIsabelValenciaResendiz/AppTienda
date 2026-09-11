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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.appmovil_hu11.ui.*
import com.example.appmovil_hu11.ui.components.CartCard
import com.example.appmovil_hu11.ui.components.UserCard
import com.example.appmovil_hu11.ui.theme.*

@Composable
fun MainContainerScreen(
    viewModel: MainViewModel = viewModel()
) {
    Scaffold(containerColor = NovaBg) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Pestañas para cambiar entre HU11 y HU12
            TabRow(
                selectedTabIndex = viewModel.selectedTab,
                containerColor = Color.Transparent,
                contentColor = NovaPillText,
                divider = {}
            ) {
                Tab(
                    selected = viewModel.selectedTab == 0,
                    onClick = { viewModel.setTab(0) },
                    text = { Text("Usuarios (HU11)", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = viewModel.selectedTab == 1,
                    onClick = { viewModel.setTab(1) },
                    text = { Text("Carritos (HU12)", fontWeight = FontWeight.Bold) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (val state = viewModel.uiState) {
                is MainUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = NovaPillText)
                    }
                }
                is MainUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error al cargar la información", color = NovaOrangeAction)
                    }
                }
                is MainUiState.Success -> {
                    if (viewModel.selectedTab == 0) {
                        // VISTA HU11 - USUARIOS
                        HeaderSection(
                            title = "Usuarios registrados",
                            subtitle = "Directorio de cuentas",
                            pillText = "Solo lectura"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "${state.users.size} cuentas",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = NovaTextDark,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(state.users) { user -> UserCard(user = user) }
                        }
                    } else {
                        // VISTA HU12 - CARRITOS
                        HeaderSection(
                            title = "Histórico de Carritos",
                            subtitle = "Monitoreo global de operaciones",
                            pillText = "Modo Auditoría"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "${state.carts.size} carritos registrados",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = NovaTextDark,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(state.carts) { cart -> CartCard(cart = cart) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderSection(title: String, subtitle: String, pillText: String) {
    Column {
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = NovaTextDark
        )
        Text(
            text = subtitle,
            fontSize = 14.sp,
            color = NovaTextSub,
            modifier = Modifier.padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .align(Alignment.End)
                .background(NovaPillBg, shape = RoundedCornerShape(50))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = pillText,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = NovaPillText
            )
        }
    }
}