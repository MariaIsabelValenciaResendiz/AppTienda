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

import com.example.appmovil_hu11.data.UserUI
import com.example.appmovil_hu11.ui.*
import com.example.appmovil_hu11.ui.components.UserCard
import com.example.appmovil_hu11.ui.theme.*

@Composable
fun UsersScreen(
    viewModel: UserViewModel = viewModel()
) {
    Scaffold(containerColor = NovaBg) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Título principal
            Text(
                text = "Usuarios registrados",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = NovaTextDark
            )
            Text(
                text = "Directorio de cuentas",
                fontSize = 14.sp,
                color = NovaTextSub,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Etiqueta
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .background(NovaPillBg, shape = RoundedCornerShape(50))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Solo lectura",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NovaPillText
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (val state = viewModel.uiState) {
                is UserUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = NovaPillText)
                    }
                }
                is UserUiState.Success -> {
                    UsersListView(users = state.users)
                }
                is UserUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error al cargar usuarios", color = NovaOrangeAction, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun UsersListView(users: List<UserUI>) {
    Column {
        Text(
            text = "${users.size} cuentas",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = NovaTextDark,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(users) { user -> UserCard(user = user) }
        }
    }
}