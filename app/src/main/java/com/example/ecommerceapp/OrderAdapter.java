package com.example.ecommerceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private Context context;
    private OrderHistoryActivity activity;

    public OrderAdapter(List<Order> orders, OrderHistoryActivity activity) {
        this.orders = orders;
        this.activity = activity;
        this.context = activity;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_card, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView orderId, orderDate, storeName, orderItemsPreview, orderTotal, orderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            orderDate = itemView.findViewById(R.id.order_date);
            storeName = itemView.findViewById(R.id.store_name);
            orderItemsPreview = itemView.findViewById(R.id.order_items_preview);
            orderTotal = itemView.findViewById(R.id.order_total);
            orderStatus = itemView.findViewById(R.id.order_status);

            // Set click listeners
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    activity.onOrderClick(orders.get(position));
                }
            });

            itemView.findViewById(R.id.btn_track_order).setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    activity.onTrackOrderClick(orders.get(position));
                }
            });

            itemView.findViewById(R.id.btn_view_details).setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    activity.onOrderClick(orders.get(position));
                }
            });
        }

        public void bind(Order order) {
            orderId.setText("Order #" + order.getOrderId());
            orderDate.setText(order.getOrderDate());
            storeName.setText(order.getStoreName());
            orderItemsPreview.setText(order.getItemsPreview());
            orderTotal.setText(order.getFormattedTotal());
            orderStatus.setText(order.getStatusDisplayName());

            // Set status color
            switch (order.getStatus().toLowerCase()) {
                case "pending":
                    orderStatus.setBackgroundColor(context.getResources().getColor(R.color.warning_color));
                    break;
                case "completed":
                case "delivered":
                    orderStatus.setBackgroundColor(context.getResources().getColor(R.color.success_color));
                    break;
                case "cancelled":
                    orderStatus.setBackgroundColor(context.getResources().getColor(R.color.error_color));
                    break;
                default:
                    orderStatus.setBackgroundColor(context.getResources().getColor(R.color.primary_color));
                    break;
            }
        }
    }
}
