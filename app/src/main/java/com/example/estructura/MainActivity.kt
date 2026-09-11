package com.example.historicocarritos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Puedes cambiar "Auditor" por "Cliente" para probar el bloqueo del Escenario 2
            CartHistoryScreen(userRole = "Auditor")
        }
    }
}