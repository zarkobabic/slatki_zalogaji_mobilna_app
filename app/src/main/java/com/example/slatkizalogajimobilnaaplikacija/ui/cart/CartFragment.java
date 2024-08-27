package com.example.slatkizalogajimobilnaaplikacija.ui.cart;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.slatkizalogajimobilnaaplikacija.R;
import com.example.slatkizalogajimobilnaaplikacija.adapters.CartAdapter;
import com.example.slatkizalogajimobilnaaplikacija.databinding.FragmentCartBinding;
import com.example.slatkizalogajimobilnaaplikacija.databinding.FragmentCookiesBinding;
import com.example.slatkizalogajimobilnaaplikacija.models.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment implements CartAdapter.OnTotalPriceChangeListener{

    private FragmentCartBinding binding;
    private List<CartItem> cartModelList;
    private CartAdapter cartAdapter;
    private RecyclerView cartRecycler;
    private TextView totalPriceCart;

    public CartFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        totalPriceCart = binding.totalPriceCart;

        cartRecycler = binding.recyclerViewCart;
        cartRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        cartModelList = new ArrayList<>();
        cartAdapter = new CartAdapter(getActivity(), cartModelList, this);
        cartRecycler.setAdapter(cartAdapter);

        updateUIFromCart();

        return root;
    }

    public void updateUIFromCart() {

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", getContext().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("cart", null);
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
        ArrayList<CartItem> cart = gson.fromJson(json, type);

        Integer totalPrice = 0;

        for(CartItem item:cart){
            cartModelList.add(item);
            String priceString = item.getProductPrice();
            Integer priceItem = Integer.parseInt(priceString.substring(0, priceString.length() - 3));
            totalPrice += item.getProductQuantity() * priceItem;
        }

        totalPriceCart.setText(totalPrice + " din");

        cartAdapter.notifyDataSetChanged();

    }

    @Override
    public void onTotalPriceChanged(int totalPrice) {
        totalPriceCart.setText(totalPrice + " din");
        //TODO proveriti da li ovo mora
        cartAdapter.notifyDataSetChanged();
    }
}