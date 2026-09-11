package com.example.estructura.repository;

import com.example.estructura.callback.CategoriaCallback;
import com.example.estructura.callback.ProductoCallback;

public interface ProductoRepository {
    void obtenerProductos(ProductoCallback callback);
    void obtenerProductosPorCategoria(String categoria, ProductoCallback callback);
    void obtenerCategorias(CategoriaCallback callback);
}