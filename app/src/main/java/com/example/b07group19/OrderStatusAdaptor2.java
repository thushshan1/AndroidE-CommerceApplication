package com.example.b07group19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.b07group19.models.Order;
import com.example.b07group19.models.OrderStatus;

import java.util.List;

public class OrderStatusAdaptor2 extends ArrayAdapter<OrderStatus> {

    private Context context;
    private int resource;


    public OrderStatusAdaptor2(@NonNull Context context, int resource, @NonNull List<OrderStatus> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    private class ViewHolder {
        TextView prod;
        TextView pri;
        TextView quan;
    }

    @NonNull
    @Override
    public View getView(int position, View converView, ViewGroup parent){
        OrderStatus order = getItem(position);

        String product = String.valueOf(order.getProductName());
        String price = String.valueOf(order.getPrice());
        String quantity = String.valueOf(order.getCount());

        ViewHolder holder;
        if(converView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            converView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.prod = (TextView) converView.findViewById(R.id.prod);
            holder.pri = (TextView) converView.findViewById(R.id.Pri);
            holder.quan = (TextView) converView.findViewById(R.id.Quan);

            converView.setTag(holder);

        }else{
            holder = (ViewHolder) converView.getTag();
        }

        holder.prod.setText(product);
        holder.pri.setText(price);
        holder.quan.setText(quantity);

        return converView;
    }
}
