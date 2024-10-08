package com.example.slatkizalogajimobilnaaplikacija.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.slatkizalogajimobilnaaplikacija.R;
import com.example.slatkizalogajimobilnaaplikacija.databinding.FragmentHomeBinding;
import com.example.slatkizalogajimobilnaaplikacija.models.PromotionModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    //private
    private ImageSlider imageSlider;
    private ArrayList<SlideModel> slideModels;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //
        imageSlider = binding.imageSlider;
        slideModels = new ArrayList<>();

        // Ucitavanje podataka i setovanje slidera
        loadPromotions();
        //

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadPromotions() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("promotions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                slideModels.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PromotionModel promotion = snapshot.getValue(PromotionModel.class);
                    int resourceId = getResources().getIdentifier(promotion.getImage(), "mipmap", getContext().getPackageName());
                    String promotionTitleAndDescription = promotion.getTitle() + "\n\n" + promotion.getDescription();
                    slideModels.add(new SlideModel(resourceId, promotionTitleAndDescription, ScaleTypes.FIT));
                }
                imageSlider.setImageList(slideModels, ScaleTypes.FIT);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }
}