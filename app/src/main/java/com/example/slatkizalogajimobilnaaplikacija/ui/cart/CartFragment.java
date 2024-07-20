package com.example.slatkizalogajimobilnaaplikacija.ui.cart;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.slatkizalogajimobilnaaplikacija.R;
import com.example.slatkizalogajimobilnaaplikacija.models.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class CartFragment extends Fragment {

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", getContext().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("cart", null);
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
        ArrayList<CartItem> cart =  gson.fromJson(json, type);

        return inflater.inflate(R.layout.fragment_cart, container, false);
    }
}