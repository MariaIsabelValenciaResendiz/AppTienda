
package com.example.estructura.network;

import com.example.estructura.model.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("products")
    Call<List<Producto>> obtenerProductos();

    @GET("products/categories")
    Call<List<String>> obtenerCategorias();

    @GET("products/category/{category}")
    Call<List<Producto>> obtenerProductosPorCategoria(@Path("category") String categoria);
}
