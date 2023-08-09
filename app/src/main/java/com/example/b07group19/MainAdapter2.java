package com.example.b07group19;

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
import com.example.b07group19.models.UserCart;
import com.example.b07group19.models.OrderItem;
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

        Glide.with(holder.img2.getContext())
                .load(model.getTurl())
                .placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img2);


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img2.getContext())
                        .setContentHolder(new ViewHolder(R.layout.viewitem))
                        .setExpanded(true, 2400)
                        .create();

                dialogPlus.show();

                View view1 = dialogPlus.getHolderView();
                ImageView photo_add= view1.findViewById(R.id.photo_add);
                ImageView photo_down= view1.findViewById(R.id.ptoto_down);
                TextView pro2 = view1.findViewById(R.id.nametext1);
                TextView bran1 = view1.findViewById(R.id.Brand1);
                TextView pri1 = view1.findViewById(R.id.Price1);
                TextView hei1 = view1.findViewById(R.id.Height1);
                TextView len1 = view1.findViewById(R.id.Length1);
                TextView wid1 = view1.findViewById(R.id.Width1);
                TextView quan = view1.findViewById(R.id.Quantity1);
                TextView count =view1.findViewById(R.id.Count);
                CircleImageView img3 = view1.findViewById(R.id.img3);
                Button add = view1.findViewById(R.id.AddtoCart);

                pro2.setText(model.getProduct());
                bran1.setText("Brand: " + model.getBrand());
                pri1.setText("Price: $" + model.getPrice());
                hei1.setText("Height: " + model.getHeight());
                len1.setText("Length: " + model.getLength());
                wid1.setText("Width: " + model.getWidth());
                quan.setText("Quantity: " + model.getQuantity());

                Glide.with(img3.getContext())
                        .load(model.getTurl())
                        .placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark)
                        .circleCrop()
                        .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                        .into(img3);


                photo_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int x = Integer.parseInt(count.getText().toString());
                        x++;
                        count.setText(x + "");
                    }
                });
                photo_down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int x=Integer.parseInt(count.getText().toString());
                                if(x>1) {
                                    x--;
                                }
                                else{
                                    Toast.makeText(view.getContext(), "You already reached minimum amount", Toast.LENGTH_LONG).show();}
                        count.setText(x+"");
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
                        Toast.makeText(view.getContext(), "Added!", Toast.LENGTH_SHORT).show();


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

        CircleImageView img2,img3;
        TextView pro, pro1, bran, price, hei, len, wid, qual,count;
        ImageView add, down;

        Button view;


        public myViewholder(@NonNull View itemView) {
            super(itemView);

            img2 = (CircleImageView)itemView.findViewById(R.id.img2);
            img3 = (CircleImageView)itemView.findViewById(R.id.img3);
            pro = (TextView)itemView.findViewById(R.id.pro);
            pro1 = (TextView)itemView.findViewById(R.id.nametext1);
            view = (Button)itemView.findViewById(R.id.view);
            bran = (TextView)itemView.findViewById(R.id.Brand1);
            price = (TextView)itemView.findViewById(R.id.Price1);
            hei = (TextView)itemView.findViewById(R.id.Height1);
            len = (TextView)itemView.findViewById(R.id.Length1);
            wid = (TextView)itemView.findViewById(R.id.Width1);
            qual = (TextView)itemView.findViewById(R.id.Quantity1);
            count = (TextView)itemView.findViewById(R.id.Count);
            add=(ImageView) view.findViewById(R.id.photo_add);
            down=(ImageView) view.findViewById(R.id.ptoto_down);

        }
    }

}
