package com.example.hu_8;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener { void onItemClick(int position); }

    public ProductAdapter(List<Product> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product p = list.get(position);
        holder.tvTitle.setText(p.title);
        holder.tvCategory.setText(p.category);
        holder.tvPrice.setText("$" + p.price);

        Glide.with(holder.itemView.getContext())
                .load(p.imageUrl)
                .centerCrop()
                .into(holder.ivProduct);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvPrice;
        ImageView ivProduct;
        public ViewHolder(View v) {
            super(v);
            ivProduct = v.findViewById(R.id.ivProduct);
            tvTitle = v.findViewById(R.id.tvItemTitle);
            tvCategory = v.findViewById(R.id.tvItemCategory);
            tvPrice = v.findViewById(R.id.tvItemPrice);
        }
    }
}