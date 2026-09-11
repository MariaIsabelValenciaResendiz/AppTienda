package com.example.estructura.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.estructura.R;
import com.example.estructura.model.ArticuloCarrito;
import com.example.estructura.utils.FormateadorPrecio;

import java.util.List;

/**
 * Un RecyclerView no puede mostrar una lista por sí solo: necesita un
 * "Adapter" que le explique, renglón por renglón, qué layout usar
 * (item_articulo_carrito.xml) y qué datos poner en cada vista.
 *
 * El adaptador NO decide qué pasa cuando se toca un botón: eso se lo
 * delega a quien lo creó (la Activity) a través de la interfaz
 * EscuchadorCarrito. Así el adaptador solo se preocupa de "pintar" la
 * lista, y la Activity decide la lógica de negocio (actualizar el
 * gestor de carrito, llamar a la API, etc.).
 */

public class AdaptadorCarrito extends RecyclerView.Adapter<AdaptadorCarrito.ArticuloViewHolder> {
    public interface EscuchadorCarrito {
        void alCambiarCantidad(ArticuloCarrito articulo, int nuevaCantidad);

        void alEliminarArticulo(ArticuloCarrito articulo);
    }

    private final List<ArticuloCarrito> articulos;
    private final EscuchadorCarrito escuchador;

    public AdaptadorCarrito(List<ArticuloCarrito> articulos, EscuchadorCarrito escuchador) {
        this.articulos = articulos;
        this.escuchador = escuchador;
    }

    @NonNull
    @Override
    public ArticuloViewHolder onCreateViewHolder(@NonNull ViewGroup padre, int tipoDeVista) {
        View vista = LayoutInflater.from(padre.getContext())
                .inflate(R.layout.item_articulo_carrito, padre, false);
        return new ArticuloViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticuloViewHolder holder, int posicion) {
        ArticuloCarrito articulo = articulos.get(posicion);

        holder.textoTitulo.setText(articulo.getTitulo());
        holder.textoPrecioUnitario.setText(
                FormateadorPrecio.formatear(articulo.getPrecioUnitario()) + " / unidad");
        holder.textoSubtotal.setText(FormateadorPrecio.formatear(articulo.calcularSubtotal()));
        holder.textoCantidad.setText(String.valueOf(articulo.getCantidad()));

        // Glide descarga y muestra la imagen del producto a partir de
        // su URL, y se encarga solo de todo el manejo de memoria/caché.
        Glide.with(holder.itemView.getContext())
                .load(articulo.getImagenUrl())
                .placeholder(R.drawable.fondo_tarjeta_redondeada)
                .into(holder.imagenProducto);

        holder.botonSumar.setOnClickListener(v ->
                escuchador.alCambiarCantidad(articulo, articulo.getCantidad() + 1));

        holder.botonRestar.setOnClickListener(v ->
                escuchador.alCambiarCantidad(articulo, articulo.getCantidad() - 1));

        holder.botonEliminar.setOnClickListener(v -> escuchador.alEliminarArticulo(articulo));
    }

    @Override
    public int getItemCount() {
        return articulos.size();
    }

    /**
     * El ViewHolder guarda las referencias a las vistas de UN renglón
     * de la lista para no tener que buscarlas otra vez (findViewById)
     * cada vez que la lista se vuelve a dibujar. Esto hace que la
     * lista se sienta fluida al hacer scroll.
     */
    static class ArticuloViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenProducto;
        TextView textoTitulo;
        TextView textoPrecioUnitario;
        TextView textoSubtotal;
        TextView textoCantidad;
        ImageButton botonSumar;
        ImageButton botonRestar;
        ImageButton botonEliminar;

        ArticuloViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenProducto = itemView.findViewById(R.id.imagenProductoItem);
            textoTitulo = itemView.findViewById(R.id.textoTituloItem);
            textoPrecioUnitario = itemView.findViewById(R.id.textoPrecioUnitarioItem);
            textoSubtotal = itemView.findViewById(R.id.textoSubtotalItem);
            textoCantidad = itemView.findViewById(R.id.textoCantidadItem);
            botonSumar = itemView.findViewById(R.id.botonSumarItem);
            botonRestar = itemView.findViewById(R.id.botonRestarItem);
            botonEliminar = itemView.findViewById(R.id.botonEliminarItem);
        }
    }
}
