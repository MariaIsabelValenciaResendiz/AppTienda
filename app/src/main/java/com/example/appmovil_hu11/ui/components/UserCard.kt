package com.example.appmovil_hu11.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmovil_hu11.data.UserUI
import com.example.appmovil_hu11.ui.theme.*

@Composable
fun UserCard(user: UserUI) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circle Avatar con Iniciales
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(NovaPillBg, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.initials,
                    color = NovaPillText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información del usuario
            Column {
                Text(
                    text = user.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = NovaTextDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Usuario #${user.id}  @${user.username}",
                    fontSize = 13.sp,
                    color = NovaTextSub
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = user.email,
                    fontSize = 13.sp,
                    color = NovaTextSub
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = user.phone,
                    fontSize = 13.sp,
                    color = NovaTextSub
                )
            }
        }
    }
}