    package com.example.appmovil_hu11

    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.material3.Surface // Importación de Surface de Material 3
    import androidx.compose.ui.Modifier
    import com.example.appmovil_hu11.ui.screens.UsersScreen
    import com.example.appmovil_hu11.ui.theme.AppMovilHu11Theme

    class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                AppMovilHu11Theme {
                    Surface(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        UsersScreen()
                    }
                }
            }
        }
    }