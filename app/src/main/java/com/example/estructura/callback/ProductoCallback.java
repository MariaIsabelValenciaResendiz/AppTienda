package com.example.estructura.callback;

import java.util.List;

import com.example.estructura.model.Producto;

public interface ProductoCallback {
    void onSuccess(List<Producto> productos);
    void onError(String mensaje);
}