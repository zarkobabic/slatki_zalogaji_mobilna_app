package com.example.slatkizalogajimobilnaaplikacija.ui.cart;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slatkizalogajimobilnaaplikacija.R;
import com.example.slatkizalogajimobilnaaplikacija.adapters.CartAdapter;
import com.example.slatkizalogajimobilnaaplikacija.databinding.FragmentCartBinding;
import com.example.slatkizalogajimobilnaaplikacija.databinding.FragmentCookiesBinding;
import com.example.slatkizalogajimobilnaaplikacija.models.CartItem;
import com.example.slatkizalogajimobilnaaplikacija.models.Order;
import com.example.slatkizalogajimobilnaaplikacija.models.OrderItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private Button addOrderCartButton;
    private TextView cartEmptyText;
    private TextView totalPriceTitle;

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
        addOrderCartButton = binding.addOrderCartButton;
        cartEmptyText = binding.cartEmptyText;
        totalPriceTitle = binding.totalPriceTitle;

        addOrderCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");

                //Dohvatanje najvece idOrder-a
                ordersRef.orderByChild("idOrder")
                        .limitToLast(1)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int nextIdOrder = 0;

                                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Order lastOrder = snapshot.getValue(Order.class);
                                        if (lastOrder != null) {
                                            nextIdOrder = lastOrder.getIdOrder() + 1;
                                        }
                                    }
                                }

                                List<OrderItem> items = new ArrayList<>();

                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", getContext().MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                Gson gson = new Gson();
                                String json = sharedPreferences.getString("cart", null);
                                Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
                                ArrayList<CartItem> cart = gson.fromJson(json, type);

                                if (cart != null) {
                                    for (CartItem item : cart) {
                                        items.add(new OrderItem(item.getIdProduct(), item.getProductPrice(), item.getProductQuantity(), item.getProductTitle()));
                                    }

                                    cart.clear();
                                    String json1 = gson.toJson(cart);
                                    editor.putString("cart", json1);
                                }

                                editor.putInt("nextIdInCart", 0);
                                editor.apply();
                                cartModelList.clear();

                                addNewOrder(nextIdOrder, sharedPreferences.getInt("idLoggedUser", -1), items, "pending");

                                updateUIFromCart();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });

        updateUIFromCart();
        return root;
    }

    public void addNewOrder(int idOrder, int idUser, List<OrderItem> items, String status) {
        DatabaseReference databaseReference;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("orders");

        Order newOrder = new Order(idOrder, idUser, items, status);

        databaseReference.child(String.valueOf(newOrder.getIdOrder())).setValue(newOrder)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Porudzbina uspesno dodata!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Porudzbina nije uspesno dodata!", Toast.LENGTH_SHORT).show();
                });
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

        if(cartModelList.size() == 0)
        {
            cartEmptyText.setVisibility(View.VISIBLE);
            totalPriceCart.setVisibility(View.GONE);
            totalPriceTitle.setVisibility(View.GONE);
            addOrderCartButton.setVisibility(View.GONE);
        }

        cartAdapter.notifyDataSetChanged();
    }


    @Override
    public void onTotalPriceChanged(int totalPrice) {
        totalPriceCart.setText(totalPrice + " din");
        cartAdapter.notifyDataSetChanged();
    }
}