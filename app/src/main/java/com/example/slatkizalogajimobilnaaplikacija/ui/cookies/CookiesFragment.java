package com.example.slatkizalogajimobilnaaplikacija.ui.cookies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slatkizalogajimobilnaaplikacija.adapters.CakeAdapter;
import com.example.slatkizalogajimobilnaaplikacija.adapters.CookieAdapter;
import com.example.slatkizalogajimobilnaaplikacija.databinding.FragmentCookiesBinding;
import com.example.slatkizalogajimobilnaaplikacija.models.Cake;
import com.example.slatkizalogajimobilnaaplikacija.models.Cookie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CookiesFragment extends Fragment {

    private FragmentCookiesBinding binding;
    List<Cookie> cookieModelList;
    CookieAdapter cookieAdapter;
    RecyclerView cookieRecycler;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCookiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cookieRecycler = binding.recyclerViewCookies;

        cookieRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        cookieModelList = new ArrayList<>();
        cookieAdapter = new CookieAdapter(getActivity(), cookieModelList);
        cookieRecycler.setAdapter(cookieAdapter);

        populateCookieListFromDatabase();


        return root;
    }

    private void populateCookieListFromDatabase() {
        DatabaseReference databaseCakesReference = FirebaseDatabase.getInstance().getReference("cookies");

        // Dohvata kolace iz firebase baze
        databaseCakesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot allCookies : dataSnapshot.getChildren()) {
                    //Cake cake = allCakes.getValue(Cake.class);
                    String cookieTitle = allCookies.child("title").getValue(String.class);
                    String cookieImage = allCookies.child("image").getValue(String.class);
                    Cookie cookie = new Cookie(cookieTitle, cookieImage);
                    cookieModelList.add(cookie);
                }
                cookieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}