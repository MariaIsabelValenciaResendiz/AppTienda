package com.example.estructura.repository;

import java.util.List;

import com.example.estructura.callback.ProductoCallback;
import com.example.estructura.model.Producto;
import com.example.estructura.network.ApiService;
import com.example.estructura.network.RetrofitClient;
import com.example.estructura.callback.CategoriaCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoRepositoryImpl implements ProductoRepository {

    private final ApiService apiService;

    public ProductoRepositoryImpl() {
        this.apiService = RetrofitClient.getApiService();
    }

    @Override
    public void obtenerProductos(ProductoCallback callback) {
        apiService.obtenerProductos().enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("No se pudo obtener el catálogo de productos.");
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                callback.onError("Error de conexión. Verifica tu internet e intenta de nuevo.");
            }
        });
    }

    @Override
    public void obtenerCategorias(CategoriaCallback callback) {
        apiService.obtenerCategorias().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("No se pudieron obtener las categorías.");
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                callback.onError("Error de conexión. Verifica tu internet e intenta de nuevo.");
            }
        });
    }

    @Override
    public void obtenerProductosPorCategoria(String categoria, ProductoCallback callback) {
        apiService.obtenerProductosPorCategoria(categoria).enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("No se pudieron obtener los productos de esta categoría.");
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                callback.onError("Error de conexión. Verifica tu internet e intenta de nuevo.");
            }
        });
    }
}