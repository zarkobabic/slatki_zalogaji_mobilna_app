package com.example.slatkizalogajimobilnaaplikacija.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.slatkizalogajimobilnaaplikacija.models.Cake;
import com.example.slatkizalogajimobilnaaplikacija.models.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartList;

    public CartAdapter(Context context, List<CartItem> cartList) {
        this.context = context;
        this.cartList = cartList;
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

        StringBuilder sb = new StringBuilder();
        sb.append(cartItem.getProductQuantity()).append(" x ").append(cartItem.getProductTitle()).append(" (").append(cartItem.getProductPrice()).append(") ");
        holder.cartItemTitleQuantityPrice.setText(sb.toString());

        String priceString = cartItem.getProductPrice();
        Integer priceItem = Integer.parseInt(priceString.substring(0, priceString.length() - 3));
        holder.itemCartTotalPrice.setText(String.valueOf(priceItem * cartItem.getProductQuantity()) + " din");




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