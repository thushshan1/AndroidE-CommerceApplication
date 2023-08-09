package com.example.b07group19;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07group19.models.Order;

import java.util.List;
public class ShoppingCartAdaptor extends  RecyclerView.Adapter<ShoppingCartAdaptor.myViewholder> {
    List<Order> orderdata;
    public ShoppingCartAdaptor(@NonNull List<Order> orderData ) {
        this.orderdata=orderData;
    }


    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store,parent,false);

        return new myViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewholder holder, int position) {
        Order order=orderdata.get(position);
        holder.storeName.setText(order.getStoreName());
        ShoppingCartProductAdaptor adaptor;
        adaptor = new ShoppingCartProductAdaptor(order);
        holder.rv.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rv.setAdapter(adaptor);

     }

    @Override
    public int getItemCount() {
        return orderdata.size();
    }

    class myViewholder extends RecyclerView.ViewHolder{

        RecyclerView rv;
        TextView storeName;


        Button btnAdd;

        public myViewholder(@NonNull View itemView) {
            super(itemView);


            storeName = (TextView)itemView.findViewById(R.id.tv_store_name);
            rv = (RecyclerView)itemView.findViewById(R.id.rv_goods);
            if(rv == null)
            {
                storeName.setText("");
            }



        }
    }


}
