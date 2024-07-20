package com.example.slatkizalogajimobilnaaplikacija.ui.personal_data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slatkizalogajimobilnaaplikacija.DetailedActivity;
import com.example.slatkizalogajimobilnaaplikacija.R;
import com.example.slatkizalogajimobilnaaplikacija.databinding.FragmentPersonalDataBinding;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PersonalDataFragment extends Fragment {

    FragmentPersonalDataBinding binding;
    EditText personalDataFirstName, personalDataLastName, personalDataContact, personalDataAddress;
    TextView personalDataModeText;
    Button changeToEditMode, updatePersonalDataButton;
    Boolean editMode = false;

    public PersonalDataFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonalDataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        personalDataFirstName = binding.personalDataFirstName;
        personalDataLastName = binding.personalDataLastName;
        personalDataContact = binding.personalDataContact;
        personalDataAddress = binding.personalDataAddress;
        changeToEditMode = binding.changeToEditMode;
        personalDataModeText = binding.personalDataModeText;
        updatePersonalDataButton = binding.updatePersonalDataButton;

        populateUserInfoFromDatabase();

        // Postavlja pocetno stanje bazirano na tome da li je editmode ukljucen
        updateUI();

        changeToEditMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMode = !editMode;
                updateUI(); // Updatuje UI kada je dugme kliknuto
            }
        });

        updatePersonalDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserProfile(personalDataFirstName.getText().toString().trim(), personalDataLastName.getText().toString().trim(),
                        personalDataAddress.getText().toString().trim(), personalDataContact.getText().toString().trim());
            }
        });

        return root;
    }

    public void updateUserProfile(String firstName, String lastName, String address, String contact) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", getContext().MODE_PRIVATE);
        Integer idLoggedUser = sharedPreferences.getInt("idLoggedUser", -1);

        if (idLoggedUser != null) {
            // Create a reference to the user node
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
            DatabaseReference userNodeRef = userRef.child(String.valueOf(idLoggedUser));

            // Kreiranje hashmape kojim ce se updatovati svojstva u bazi (svojstvo -> vrednost)
            Map<String, Object> updates = new HashMap<>();
            updates.put("firstName", firstName);
            updates.put("lastName", lastName);
            updates.put("address", address);
            updates.put("contact", contact);

            // Updatovanje profila korisnika u Firebase Realtime bazi
            userNodeRef.updateChildren(updates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Profil korisnika je azuriran", Toast.LENGTH_SHORT).show();
                            editMode = false;
                            updateUI();
                        } else {
                            // Update failed
                            Toast.makeText(getActivity(), "Profil nije uspesno azuriran", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "Prijavljeni korisnik nije pronadjen u bazi", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateUserInfoFromDatabase() {
        DatabaseReference databaseUserReference = FirebaseDatabase.getInstance().getReference("users");

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String localStorageUsername = sharedPreferences.getString("username", "Default Value");

        Query checkUserInfo = databaseUserReference.orderByChild("username").equalTo(localStorageUsername);

        checkUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot userJSON = snapshot.getChildren().iterator().next();

                    String firstNameFromDB = userJSON.child("firstName").getValue(String.class);
                    String lastNameFromDB = userJSON.child("lastName").getValue(String.class);
                    String addressFromDB = userJSON.child("address").getValue(String.class);
                    String contactFromDB = userJSON.child("contact").getValue(String.class);

                    personalDataFirstName.setText(firstNameFromDB);
                    personalDataLastName.setText(lastNameFromDB);
                    personalDataContact.setText(contactFromDB);
                    personalDataAddress.setText(addressFromDB);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void updateUI() {
        personalDataFirstName.setEnabled(editMode);
        personalDataLastName.setEnabled(editMode);
        personalDataContact.setEnabled(editMode);
        personalDataAddress.setEnabled(editMode);

        if (editMode) {
            personalDataModeText.setText("Mod za izmenu");
            changeToEditMode.setText("Promeni na rezim za citanje");
            updatePersonalDataButton.setVisibility(View.VISIBLE);
        } else {
            personalDataModeText.setText("Mod za citanje");
            //Ukoliko se nesto bezveze napise u modu za izmenu da uvek pregazi to
            populateUserInfoFromDatabase();
            changeToEditMode.setText("Promeni na rezim za izmenu");
            updatePersonalDataButton.setVisibility(View.GONE);
        }
    }
}