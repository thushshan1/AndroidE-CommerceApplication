package com.example.b07group19;

import androidx.annotation.NonNull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.b07group19.models.Order;
import com.example.b07group19.models.OrderDescription;

import java.util.List;

public class OrderDescriptionAdapter extends ArrayAdapter<OrderDescription> {
    private Context context;
    private int resource;

    public OrderDescriptionAdapter(Context context, int resource, List<OrderDescription> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;

    }

    private class ViewHolder {
        TextView tvCustomerName;
        TextView tvCreatedDate;
        TextView tvOrderID;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String customerName = getItem(position).getCustomerName();
        String createdDate = getItem(position).getCreatedDate();
        String orderID = getItem(position).getOrderID();

        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();

            holder.tvCustomerName = (TextView) convertView.findViewById(R.id.tvCustomerName);
            holder.tvCreatedDate = (TextView) convertView.findViewById(R.id.tvCreatedDate);
            holder.tvOrderID = (TextView) convertView.findViewById(R.id.tvOrderID);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvCustomerName.setText(customerName);
        holder.tvCreatedDate.setText(createdDate);
        holder.tvOrderID.setText(orderID);

        return convertView;
    }


}