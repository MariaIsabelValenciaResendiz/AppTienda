package com.example.estructura.callback;

import java.util.List;

public interface CategoriaCallback {
    void onSuccess(List<String> categorias);
    void onError(String mensaje);
}