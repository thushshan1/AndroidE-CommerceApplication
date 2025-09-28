package com.example.ecommerceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.models.OrderItem;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private List<OrderItem> items;
    private Context context;

    public OrderItemAdapter(List<OrderItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_product, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<OrderItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName, productBrand, productPrice;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productBrand = itemView.findViewById(R.id.product_brand);
            productPrice = itemView.findViewById(R.id.product_price);
        }

        public void bind(OrderItem item) {
            productName.setText(item.getProductName());
            productBrand.setText(item.getProductBrand());
            productPrice.setText(item.getFormattedPrice());

            // Load product image from URL if available
            if (item.getPurl() != null && !item.getPurl().isEmpty()) {
                // In a real app, you would use Glide or Picasso to load images
                // Glide.with(context).load(item.getPurl()).into(productImage);
                // For now, use placeholder
                productImage.setImageResource(R.drawable.ic_image_placeholder);
            } else {
                productImage.setImageResource(R.drawable.ic_image_placeholder);
            }
        }
    }
}
