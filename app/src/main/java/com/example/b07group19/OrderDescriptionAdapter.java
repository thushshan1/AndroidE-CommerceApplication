package com.example.b07group19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

    }

//    @NonNull
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent){
//        String customerName = getItem(position).customerName;
//        String createdDate = getItem(position).createdDate;
//
//        ViewHolder holder;
//        if(convertView == null){
//            LayoutInflater inflater = LayoutInflater.from(context);
//            convertView = inflater.inflate(resource, parent, false);
//            holder = new ViewHolder();
//
//            holder.tvCustomerName = (TextView) convertView.findViewById(R.layout.tv)
//        }
//    }

}