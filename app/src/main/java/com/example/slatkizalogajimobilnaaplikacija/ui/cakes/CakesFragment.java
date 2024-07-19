package com.example.slatkizalogajimobilnaaplikacija.ui.cakes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slatkizalogajimobilnaaplikacija.adapters.CakeAdapter;
import com.example.slatkizalogajimobilnaaplikacija.databinding.FragmentCakesBinding;
import com.example.slatkizalogajimobilnaaplikacija.models.Cake;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CakesFragment extends Fragment {

    private FragmentCakesBinding binding;
    List<Cake> cakeModelList;
    CakeAdapter cakeAdapter;
    RecyclerView cakeRecycler;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCakesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //
        cakeRecycler = binding.recyclerViewCakes;

        cakeRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        cakeModelList = new ArrayList<>();
        cakeAdapter = new CakeAdapter(getActivity(), cakeModelList);
        cakeRecycler.setAdapter(cakeAdapter);

        populateCakeListFromDatabase();

        //



//
//        final TextView textView = binding.textSlideshow;
//        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }


    private void populateCakeListFromDatabase() {

        DatabaseReference databaseCakesReference = FirebaseDatabase.getInstance().getReference("cakes");

        // Dohvata torte iz firebase baze
        databaseCakesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot allCakes : dataSnapshot.getChildren()) {
                    //Cake cake = allCakes.getValue(Cake.class);
                    String cakeTitle = allCakes.child("title").getValue(String.class);
                    String cakeImage = allCakes.child("image").getValue(String.class);
                    Cake cake = new Cake(cakeTitle, cakeImage);
                    cakeModelList.add(cake);
                    cakeAdapter.notifyDataSetChanged();
                }
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