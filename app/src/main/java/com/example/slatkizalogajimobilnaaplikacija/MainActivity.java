package com.example.slatkizalogajimobilnaaplikacija;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.slatkizalogajimobilnaaplikacija.models.PromotionModel;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.slatkizalogajimobilnaaplikacija.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private DatabaseReference reference;
    private List<PromotionModel> promotionsList;
    //inicijalizacija imageSlidera
    private ImageSlider imageSlider;
    private ArrayList<SlideModel> slideModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        imageSlider = findViewById(R.id.imageSlider);
        slideModels = new ArrayList<>();

        //Dovlacenje promocija sa firebase baze
        reference = FirebaseDatabase.getInstance().getReference();
        promotionsList = new ArrayList<>();
        readPromotions();


//        slideModels.add(new SlideModel(R.mipmap.promotion1, "Promotion1\nKOKO", ScaleTypes.FIT));
//        slideModels.add(new SlideModel(R.mipmap.promotion2, "Promotion2", ScaleTypes.FIT));
//        slideModels.add(new SlideModel(R.mipmap.promotion3, "Promotion3", ScaleTypes.FIT));
//        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        //Kraj inicijalizacije


        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cookies, R.id.nav_cakes, R.id.nav_personalDataFragment, R.id.nav_changePasswordFragment,
                R.id.nav_contactFragment, R.id.nav_notificationFragment, R.id.nav_cartFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        TextView myTextView = findViewById(R.id.panelUsername);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String localStorageUsername = sharedPreferences.getString("username", "Default Value");
        String localStorageFirstName = sharedPreferences.getString("firstName","Default Value");
        String localStorageLastName = sharedPreferences.getString("lastName","Default Value");
        String localStorageFullName = localStorageFirstName + " " + localStorageLastName;
        myTextView.setText(localStorageFullName);

        //Postavljanje listenera za dugme za odjavljivanje jer se ono pravi kada se pravi side meni
        Button logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        // Odjavljivanje i prelazak na LoginActivity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Zavrsava trenutni activity
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //funkcija koja cita promocije iz firebase baze
    private void readPromotions() {
        reference.child("promotions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                promotionsList.clear(); // Clear the list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PromotionModel promotion = snapshot.getValue(PromotionModel.class);
                    promotionsList.add(promotion);
                }

                for (PromotionModel promotion : promotionsList) {
                    int resourceId = getResources().getIdentifier(promotion.getImage(), "mipmap", getPackageName());
                    String promotionTitleAndDescription = promotion.getTitle() + "\n\n" + promotion.getDescription();
                    slideModels.add(new SlideModel(resourceId, promotionTitleAndDescription, ScaleTypes.FIT));
                }
                imageSlider.setImageList(slideModels, ScaleTypes.FIT);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}