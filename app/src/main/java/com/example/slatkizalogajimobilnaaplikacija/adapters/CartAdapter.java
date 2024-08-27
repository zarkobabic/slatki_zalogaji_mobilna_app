package com.example.slatkizalogajimobilnaaplikacija.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slatkizalogajimobilnaaplikacija.DetailedActivity;
import com.example.slatkizalogajimobilnaaplikacija.R;
import com.example.slatkizalogajimobilnaaplikacija.databinding.FragmentCartBinding;
import com.example.slatkizalogajimobilnaaplikacija.models.Cake;
import com.example.slatkizalogajimobilnaaplikacija.models.CartItem;
import com.example.slatkizalogajimobilnaaplikacija.ui.cart.CartFragment;
import com.google.android.material.internal.ContextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartList;
    private OnTotalPriceChangeListener onTotalPriceChangeListener;

    public CartAdapter(Context context, List<CartItem> cartList, OnTotalPriceChangeListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.onTotalPriceChangeListener = listener;
    }

    // Interface za komunikaciju sa CartFragmentom
    public interface OnTotalPriceChangeListener {
        void onTotalPriceChanged(int totalPrice);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartList.get(position);
        updateUIWithInfo(cartItem, holder);

        holder.itemCartUpdateQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuantityForId(cartItem.getIdInCart(), Integer.parseInt(holder.itemCartNewQuantity.getText().toString().trim()));
                updateUIWithInfo(cartItem, holder);
                calculateTotalPriceAndSendToCartFragment();
            }
        });

        holder.itemCartDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItemForId(cartItem.getIdInCart());
                substractNextIdInCart();
                updateUIWithInfo(cartItem, holder);
                calculateTotalPriceAndSendToCartFragment();
            }
        });

    }

    private void substractNextIdInCart() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", context.MODE_PRIVATE);
        Integer nextIdInCart = sharedPreferences.getInt("nextIdInCart", -1);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("nextIdInCart", nextIdInCart - 1);
        editor.apply();
    }

    private void deleteItemForId(int idInCart) {
        cartList.remove(idInCart);

        for(CartItem item:cartList)
        {
            if(item.getIdInCart() > idInCart)
                item.setIdInCart(item.getIdInCart() - 1);
        }
        removeItemFromCartLocalStorage(idInCart);
    }

    private void removeItemFromCartLocalStorage(int idInCart) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = sharedPreferences.getString("cart", null);
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
        ArrayList<CartItem> cart = gson.fromJson(json, type);

        cart.remove(idInCart);
        for(CartItem item:cart)
        {
            if(item.getIdInCart() > idInCart)
                item.setIdInCart(item.getIdInCart() - 1);
        }

        Gson gson1 = new Gson();
        String json1 = gson1.toJson(cart);
        editor.putString("cart", json1);
        editor.apply();
    }

    private void calculateTotalPriceAndSendToCartFragment() {
        Integer totalPrice = 0;
        for(CartItem item:cartList){
            String priceString = item.getProductPrice();
            Integer priceItem = Integer.parseInt(priceString.substring(0, priceString.length() - 3));
            totalPrice += item.getProductQuantity() * priceItem;
        }
        onTotalPriceChangeListener.onTotalPriceChanged(totalPrice);
    }

    private void updateUIWithInfo(CartItem cartItem, @NonNull CartViewHolder holder) {

        StringBuilder sb = new StringBuilder();
        sb.append(cartItem.getProductQuantity()).append(" x ").append(cartItem.getProductTitle()).append(" (").append(cartItem.getProductPrice()).append(") ");
        holder.cartItemTitleQuantityPrice.setText(sb.toString());

        String priceString = cartItem.getProductPrice();
        Integer priceItem = Integer.parseInt(priceString.substring(0, priceString.length() - 3));
        holder.itemCartTotalPrice.setText(String.valueOf(priceItem * cartItem.getProductQuantity()) + " din");
    }

    private void updateQuantityForId(Integer id, Integer quantity) {

        cartList.get(id).setProductQuantity(quantity);
        updateItemInCart(id, quantity);
    }

    private void updateItemInCart(Integer id, Integer quantity) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = sharedPreferences.getString("cart", null);
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
        ArrayList<CartItem> cart =  gson.fromJson(json, type);

        cart.get(id).setProductQuantity(quantity);

        Gson gson1 = new Gson();
        String json1 = gson1.toJson(cart);
        editor.putString("cart", json1);
        editor.apply();
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        TextView cartItemTitleQuantityPrice, itemCartTotalPrice;
        EditText itemCartNewQuantity;
        Button itemCartUpdateQuantityButton, itemCartDeleteButton;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartItemTitleQuantityPrice = itemView.findViewById(R.id.cartItemTitleQuantityPrice);
            itemCartTotalPrice = itemView.findViewById(R.id.itemCartTotalPrice);
            itemCartNewQuantity = itemView.findViewById(R.id.itemCartNewQuantity);
            itemCartUpdateQuantityButton = itemView.findViewById(R.id.itemCartUpdateQuantityButton);
            itemCartDeleteButton = itemView.findViewById(R.id.itemCartDeleteButton);
        }
    }
}