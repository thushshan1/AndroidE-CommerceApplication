package com.example.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ecommerceapp.models.Store;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ManageProducts extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private ListView productsList;
    private LinearLayout emptyState;
    private TextView emptyStateText;

    private String storeName;
    private String currentUserID;
    private Model model;
    private List<Products> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        
        productsList = findViewById(R.id.products_list);
        emptyState = findViewById(R.id.empty_products_state);

        storeName = getIntent().getStringExtra("storeName");
        
        // Get current user ID
        currentUserID = FirebaseAuth.getInstance().getUid();
        if (currentUserID == null) {
            Log.e("ManageProducts", "User ID is null, redirecting to login");
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        
        model = Model.getInstance();
        
        // Set up back button with smooth transition
        View backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        
        // Load products
        loadProducts();
    }
    
    private void loadProducts() {
        model.getStoreByOwner(currentUserID, (Store store) -> {
            if (store != null && store.products != null && !store.products.isEmpty()) {
                // Products exist - display them
                products = store.products;
                ProductAdapter adapter = new ProductAdapter(products);
                productsList.setAdapter(adapter);
                productsList.setVisibility(View.VISIBLE);
                emptyState.setVisibility(View.GONE);
                
                // Set click listener for editing products
                productsList.setOnItemClickListener((parent, view, position, id) -> {
                    Products product = products.get(position);
                    Intent intent = new Intent(ManageProducts.this, AddProduct.class);
                    intent.putExtra("storeName", storeName);
                    intent.putExtra("currentUserID", currentUserID);
                    intent.putExtra("editMode", true);
                    intent.putExtra("productName", product.product);
                    intent.putExtra("productPrice", product.price);
                    intent.putExtra("productBrand", product.brand != null ? product.brand : "");
                    intent.putExtra("productQuantity", product.quantity != null ? product.quantity : "");
                    intent.putExtra("productLength", product.length != null ? product.length : "");
                    intent.putExtra("productWidth", product.width != null ? product.width : "");
                    intent.putExtra("productHeight", product.height != null ? product.height : "");
                    intent.putExtra("productImageUrl", product.turl != null ? product.turl : "");
                    intent.putExtra("productPosition", position);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                });
            } else {
                // No products - show empty state
                products = new ArrayList<>();
                productsList.setVisibility(View.GONE);
                emptyState.setVisibility(View.VISIBLE);
            }
        });
    }
    
    // Custom adapter for product list with images
    private class ProductAdapter extends BaseAdapter {
        private List<Products> productList;
        
        public ProductAdapter(List<Products> products) {
            this.productList = products;
        }
        
        @Override
        public int getCount() {
            return productList.size();
        }
        
        @Override
        public Products getItem(int position) {
            return productList.get(position);
        }
        
        @Override
        public long getItemId(int position) {
            return position;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            
            if (convertView == null) {
                convertView = LayoutInflater.from(ManageProducts.this)
                    .inflate(R.layout.item_product_manage, parent, false);
                holder = new ViewHolder();
                holder.productImage = convertView.findViewById(R.id.product_image);
                holder.productName = convertView.findViewById(R.id.product_name);
                holder.productPrice = convertView.findViewById(R.id.product_price);
                holder.productQuantity = convertView.findViewById(R.id.product_quantity);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            Products product = productList.get(position);
            holder.productName.setText(product.product != null ? product.product : "Unnamed Product");
            holder.productPrice.setText("$" + (product.price != null ? product.price : "0.00"));
            
            if (product.quantity != null && !product.quantity.isEmpty()) {
                holder.productQuantity.setText("Qty: " + product.quantity);
                holder.productQuantity.setVisibility(View.VISIBLE);
            } else {
                holder.productQuantity.setVisibility(View.GONE);
            }
            
            // Load product image using Glide
            if (product.turl != null && !product.turl.isEmpty()) {
                Glide.with(ManageProducts.this)
                    .load(product.turl)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_placeholder)
                    .centerCrop()
                    .into(holder.productImage);
            } else {
                holder.productImage.setImageResource(R.drawable.ic_image_placeholder);
            }
            
            return convertView;
        }
        
        private class ViewHolder {
            ImageView productImage;
            TextView productName;
            TextView productPrice;
            TextView productQuantity;
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Reload products when returning from AddProduct
        loadProducts();
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Smooth fade transition animation
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.button) {
            Intent intent = new Intent(this, AddProduct.class);
            intent.putExtra("storeName", storeName);
            intent.putExtra("currentUserID", currentUserID);
            startActivity(intent);
            // Smooth fade transition when opening AddProduct
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}
