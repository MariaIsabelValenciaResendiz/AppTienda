package com.example.estructura.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.estructura.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class CategoriaBottomSheet {

    public interface OnCategoriaSeleccionadaListener {
        void onCategoriaSeleccionada(String categoria);
    }

    public static void mostrar(Context context, List<String> categorias, String categoriaActual,
                               OnCategoriaSeleccionadaListener listener) {

        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View vista = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_categorias, null);
        dialog.setContentView(vista);

        LinearLayout contenedor = vista.findViewById(R.id.llListaCategorias);
        TextView btnCancelar = vista.findViewById(R.id.btnCancelar);

        // Opción "Ver todos" siempre primero
        agregarOpcion(context, contenedor, "▦", "Ver todos", "todos".equals(categoriaActual), () -> {
            listener.onCategoriaSeleccionada("todos");
            dialog.dismiss();
        });

        // Resto de categorías traídas de la API
        for (String categoria : categorias) {
            String icono = obtenerIcono(categoria);
            boolean seleccionada = categoria.equals(categoriaActual);

            agregarOpcion(context, contenedor, icono, capitalizar(categoria), seleccionada, () -> {
                listener.onCategoriaSeleccionada(categoria);
                dialog.dismiss();
            });
        }

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private static void agregarOpcion(Context context, LinearLayout contenedor, String icono,
                                      String nombre, boolean seleccionada, Runnable onClick) {

        View item = LayoutInflater.from(context).inflate(R.layout.item_categoria, contenedor, false);

        TextView txtIcono = item.findViewById(R.id.txtIconoCategoria);
        TextView txtNombre = item.findViewById(R.id.txtNombreCategoria);
        TextView txtIndicador = item.findViewById(R.id.txtIndicador);
        LinearLayout fila = item.findViewById(R.id.llItemCategoria);

        txtIcono.setText(icono);
        txtNombre.setText(nombre);

        if (seleccionada) {
            fila.setBackgroundColor(0xFFDCEEE3);
            txtIndicador.setText("✓");
        } else {
            fila.setBackgroundColor(0xFFFFFFFF);
            txtIndicador.setText("›");
        }

        item.setOnClickListener(v -> onClick.run());

        contenedor.addView(item);
    }

    private static String obtenerIcono(String categoria) {
        switch (categoria) {
            case "electronics":
                return "🎧";
            case "men's clothing":
                return "👕";
            case "women's clothing":
                return "👗";
            case "jewelery":
                return "💎";
            default:
                return "▦";
        }
    }

    private static String capitalizar(String categoria) {
        switch (categoria) {
            case "electronics":
                return "Electrónica";
            case "men's clothing":
                return "Ropa de hombre";
            case "women's clothing":
                return "Ropa de mujer";
            case "jewelery":
                return "Joyería";
            default:
                return categoria;
        }
    }
}