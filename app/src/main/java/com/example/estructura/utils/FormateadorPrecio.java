package com.example.estructura.utils;
import java.util.Locale;

/**
 * Regla de negocio de US10: "los precios mostrados en la interfaz
 * deben estar redondeados a dos decimales". Centralizamos el formato
 * aquí para que TODAS las pantallas muestren el precio exactamente
 * igual (mismo símbolo, mismos decimales).
 */

public class FormateadorPrecio {
    private FormateadorPrecio() {
        // Clase de utilidades: no se debe instanciar.
    }

    public static String formatear(double valor) {
        // Locale.US asegura que el separador decimal sea el punto
        // (ej. $899.00) sin importar la configuración regional del
        // dispositivo donde corra la app.
        return String.format(Locale.US, "$%.2f", valor);
    }
}
