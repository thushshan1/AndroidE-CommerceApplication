package com.example.ecommerceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceapp.models.Order;
import com.example.ecommerceapp.models.OrderItem;

public class ShoppingCartProductAdaptor extends  RecyclerView.Adapter<ShoppingCartProductAdaptor.myViewholder> {
    Order orders;
    ShoppingCartAdaptor shoppingCartAdaptor;
    int pos;
    public ShoppingCartProductAdaptor(@NonNull Order orderData, int position, ShoppingCartAdaptor shoppingCartAdaptor) {
        this.orders=orderData;
        this.shoppingCartAdaptor=shoppingCartAdaptor;
        this.pos = position;

    }


    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_good,parent,false);

        return new myViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewholder holder, int position) {
        OrderItem order=orders.getItems().get(position);
//        holder.storeName.setText(order.getStoreName());
        holder.name.setText(order.getProductName());
        holder.price.setText(String.valueOf(order.getPrice()));
        holder.count.setText(order.getCount());
        Glide.with(holder.goodImg.getContext())
                .load(order.getPurl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.goodImg);
        holder.btnInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = Integer.parseInt(holder.count.getText().toString());
                x++;
                holder.count.setText(x + "");
            }
        });
        holder.btnDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x=Integer.parseInt(holder.count.getText().toString());
                if(x>1) {
                    x--;
                    holder.count.setText(x+"");
                }
                else{
                    orders.getItems().remove(position);
                    if (orders.getItems().size()==0) {
                        shoppingCartAdaptor.orderdata.remove(pos);
                        shoppingCartAdaptor.notifyDataSetChanged();
                    }
                    notifyDataSetChanged();
                    Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_LONG).show();}
            }
        });

     }

    @Override
    public int getItemCount() {
        return orders.getItems().size();
    }

    class myViewholder extends RecyclerView.ViewHolder{


        TextView name,price,count,storeName;
        ImageView goodImg;



        TextView btnInc,btnDesc;

        public myViewholder(@NonNull View itemView) {
            super(itemView);
            name =(TextView) itemView.findViewById(R.id.tv_good_name);
            price =(TextView) itemView.findViewById(R.id.tv_goods_price);
            count =(TextView) itemView.findViewById(R.id.tv_goods_num);
            goodImg =(ImageView) itemView.findViewById(R.id.iv_goods);
            btnInc =(TextView) itemView.findViewById(R.id.tv_increase_goods_num);
            btnDesc =(TextView) itemView.findViewById(R.id.tv_reduce_goods_num);

        }
    }

}
