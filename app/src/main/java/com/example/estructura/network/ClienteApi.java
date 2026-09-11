package com.example.estructura.network;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * "Singleton" quiere decir que esta clase solo va a crear UNA sola
 * instancia de Retrofit para toda la aplicación, en lugar de crear una
 * nueva cada vez que se necesita hacer una petición. Esto ahorra
 * memoria y evita configurarlo repetidamente.
 *
 * IMPORTANTE PARA EL EQUIPO: si algún compañero ya creó una clase
 * similar (por ejemplo para US03 - catálogo), pónganse de acuerdo y
 * usen una sola clase de cliente Retrofit compartida en el proyecto
 * final, para no duplicar configuración.
 */

public class ClienteApi {
    private static final String URL_BASE = "https://fakestoreapi.com/";

    private static Retrofit instancia;

    // Constructor privado: nadie fuera de esta clase puede escribir
    // "new ClienteApi()". Así garantizamos que solo exista una instancia.
    private ClienteApi() {
    }

    public static synchronized Retrofit obtenerInstancia() {
        if (instancia == null) {
            // Este interceptor imprime en el Logcat el detalle de cada
            // petición y respuesta HTTP. Es solo para depurar mientras
            // desarrollan; no afecta el funcionamiento de la app.
            HttpLoggingInterceptor interceptorLogs = new HttpLoggingInterceptor();
            interceptorLogs.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient clienteHttp = new OkHttpClient.Builder()
                    .addInterceptor(interceptorLogs)
                    .build();

            instancia = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .client(clienteHttp)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instancia;
    }

    public static ApiFakeStore obtenerServicio() {
        return obtenerInstancia().create(ApiFakeStore.class);
    }
}
