package com.example.estructura.utils;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Acceso de SOLO LECTURA a los datos de sesión que la pantalla de
 * Login (US01) debió guardar al autenticar al usuario.
 *
 * MUY IMPORTANTE PARA EL EQUIPO: los valores "preferencias_sesion" y
 * "perfil_usuario" son nombres de ejemplo. Debes reunirte con quien
 * programó US01 y usar EXACTAMENTE el mismo nombre de archivo de
 * preferencias y la misma clave con la que se guardó el rol del
 * usuario (Cliente / Administrador / Auditor); si los nombres no
 * coinciden, esta clase no podrá leer el perfil correcto.
 */

public class SesionUsuario {
    private static final String ARCHIVO_PREFERENCIAS_SESION = "preferencias_sesion";
    private static final String CLAVE_PERFIL = "perfil_usuario";

    public static final String PERFIL_CLIENTE = "Cliente";
    public static final String PERFIL_ADMINISTRADOR = "Administrador";
    public static final String PERFIL_AUDITOR = "Auditor";

    private SesionUsuario() {
    }

    public static String obtenerPerfil(Context contexto) {
        SharedPreferences preferencias = contexto.getSharedPreferences(
                ARCHIVO_PREFERENCIAS_SESION, Context.MODE_PRIVATE);
        return preferencias.getString(CLAVE_PERFIL, PERFIL_CLIENTE);
    }

    /** US09 - Escenario 3: el Auditor navega en modo solo lectura. */
    public static boolean esAuditor(Context contexto) {
        return PERFIL_AUDITOR.equals(obtenerPerfil(contexto));
    }
}
