package com.example.ecommerceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceapp.models.UserCart;
import com.example.ecommerceapp.models.OrderItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter2 extends FirebaseRecyclerAdapter<Products,MainAdapter2.myViewholder> {
    String storeName;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter2(@NonNull FirebaseRecyclerOptions<Products> options,String storeName) {
        super(options);
        this.storeName=storeName;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull Products model) {
        holder.pro.setText(model.getProduct());

        // Load product image
        if (model.getTurl() != null && !model.getTurl().isEmpty()) {
            Glide.with(holder.img2.getContext())
                    .load(model.getTurl())
                    .placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark)
                    .centerCrop()
                    .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                    .into(holder.img2);
        }


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img2.getContext())
                        .setContentHolder(new ViewHolder(R.layout.viewitem))
                        .setExpanded(false)
                        .setContentHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setContentWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT)
                        .create();

                dialogPlus.show();

                View view1 = dialogPlus.getHolderView();
                com.google.android.material.button.MaterialButton photo_add = view1.findViewById(R.id.photo_add);
                com.google.android.material.button.MaterialButton photo_down = view1.findViewById(R.id.ptoto_down);
                TextView pro2 = view1.findViewById(R.id.nametext1);
                TextView bran1 = view1.findViewById(R.id.Brand1);
                TextView pri1 = view1.findViewById(R.id.Price1);
                TextView hei1 = view1.findViewById(R.id.Height1);
                TextView len1 = view1.findViewById(R.id.Length1);
                TextView wid1 = view1.findViewById(R.id.Width1);
                TextView quan = view1.findViewById(R.id.Quantity1);
                TextView count = view1.findViewById(R.id.Count);
                ImageView img3 = view1.findViewById(R.id.img3);
                com.google.android.material.button.MaterialButton add = view1.findViewById(R.id.AddtoCart);

                pro2.setText(model.getProduct());
                bran1.setText("Brand: " + (model.getBrand() != null && !model.getBrand().isEmpty() ? model.getBrand() : "N/A"));
                pri1.setText("$" + (model.getPrice() != null ? model.getPrice() : "0.00"));
                hei1.setText("Height: " + (model.getHeight() != null && !model.getHeight().isEmpty() ? model.getHeight() : "N/A"));
                len1.setText("Length: " + (model.getLength() != null && !model.getLength().isEmpty() ? model.getLength() : "N/A"));
                wid1.setText("Width: " + (model.getWidth() != null && !model.getWidth().isEmpty() ? model.getWidth() : "N/A"));
                quan.setText("Available Quantity: " + (model.getQuantity() != null ? model.getQuantity() : "0"));

                // Load product image
                if (model.getTurl() != null && !model.getTurl().isEmpty()) {
                    Glide.with(img3.getContext())
                            .load(model.getTurl())
                            .placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark)
                            .centerCrop()
                            .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                            .into(img3);
                }


                photo_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            int x = Integer.parseInt(count.getText().toString());
                            x++;
                            count.setText(String.valueOf(x));
                        } catch (NumberFormatException e) {
                            count.setText("1");
                        }
                    }
                });
                photo_down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            int x = Integer.parseInt(count.getText().toString());
                            if (x > 1) {
                                x--;
                                count.setText(String.valueOf(x));
                            } else {
                                Toast.makeText(view.getContext(), "You already reached minimum amount", Toast.LENGTH_LONG).show();
                            }
                        } catch (NumberFormatException e) {
                            count.setText("1");
                        }
                    }
                });
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int positi1=position;
                        OrderItem detail = new OrderItem();
                        detail.setPrice(model.getPrice());
                        detail.setProductName(model.getProduct());
                        detail.setPurl(model.getTurl());
                        detail.setBrand(model.getBrand());
                        detail.setCount(count.getText().toString());
                        UserCart.addItem(storeName,detail);
                        Toast.makeText(view.getContext(), "Added to cart!", Toast.LENGTH_SHORT).show();
                        dialogPlus.dismiss(); // Close the dialog after adding to cart
                    }
                });




            }
        });



    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productview,parent,false);

        return new myViewholder(view);
    }

    class myViewholder extends RecyclerView.ViewHolder{

        ImageView img2;
        TextView pro;
        Button view;

        public myViewholder(@NonNull View itemView) {
            super(itemView);

            img2 = itemView.findViewById(R.id.img2);
            pro = itemView.findViewById(R.id.pro);
            view = itemView.findViewById(R.id.view);
        }
    }

}
