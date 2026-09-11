package com.example.appmovil_hu11.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Import de los modelos desde DataModels.kt
import com.example.appmovil_hu11.data.CartItemUI
import com.example.appmovil_hu11.data.CartUI

// Import de los colores del tema
import com.example.appmovil_hu11.ui.theme.*

@Composable
fun CartCard(cart: CartUI) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Encabezado de la tarjeta
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Carrito #${cart.id}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NovaTextDark
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Propietario: Usuario ID ${cart.userId}",
                        fontSize = 13.sp,
                        color = NovaTextSub
                    )
                }
                Text(
                    text = cart.date,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NovaTextDark
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón interactivo
            Text(
                text = if (expanded) "▲ Ocultar productos" else "▼ Ver desglose (${cart.items.size} artículos)",
                fontSize = 13.sp,
                color = NovaOrangeAction,
                fontWeight = FontWeight.Bold
            )

            // Contenido expandible
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = NovaDivider, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    cart.items.forEach { item ->
                        CartItemRow(item = item)
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItemUI) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.productTitle,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = NovaTextDark
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "ID Producto: ${item.productId}",
                fontSize = 11.sp,
                color = NovaTextSub
            )
        }
        Text(
            text = "Cant: ${item.quantity}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = NovaTextDark
        )
    }
}