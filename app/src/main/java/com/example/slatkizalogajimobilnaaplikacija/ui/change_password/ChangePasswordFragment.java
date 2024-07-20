package com.example.slatkizalogajimobilnaaplikacija.ui.change_password;

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
import android.widget.Toast;

import com.example.slatkizalogajimobilnaaplikacija.R;
import com.example.slatkizalogajimobilnaaplikacija.databinding.FragmentChangePasswordBinding;
import com.example.slatkizalogajimobilnaaplikacija.databinding.FragmentCookiesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordFragment extends Fragment {

    FragmentChangePasswordBinding binding;
    EditText oldPasswordInput, newPasswordInput, newPasswordConfirmedInput;
    Button changePasswordButton;


    public ChangePasswordFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //
            oldPasswordInput = binding.oldPassword;
            newPasswordInput = binding.newPassword;
            newPasswordConfirmedInput = binding.newPasswordConfirmed;
            changePasswordButton = binding.changePasswordButton;
        //

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(newPasswordInput.getText().toString().trim().equals(newPasswordConfirmedInput.getText().toString().trim())){
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                    Integer idLoggedUser = sharedPreferences.getInt("idLoggedUser", -1);
                    String oldPassword = oldPasswordInput.getText().toString().trim();
                    String newPassword = newPasswordInput.getText().toString().trim();
                    checkOldPasswordValidityAndUpdatePassword(idLoggedUser, oldPassword, newPassword);
                }
                else{
                    Toast.makeText(getActivity(), "Greska: Nova sifra i potvrda nove sifre moraju biti identicne!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return root;
    }

    private void checkOldPasswordValidityAndUpdatePassword(Integer idUser, String oldPassword, String newPassword) {

        if (idUser != null) {
            // Create a reference to the user node
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
            DatabaseReference userNodeRef = userRef.child(String.valueOf(idUser));

            userNodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String currentPassword = dataSnapshot.child("password").getValue(String.class);
                        if (currentPassword != null && currentPassword.equals(oldPassword)) {
                            updatePassword(userNodeRef, newPassword);
                        } else {
                            Toast.makeText(getActivity(), "Greska: Stara sifra nije tacna!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Greska: Korisnik nije pronadjen u bazi", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "Greska: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Prijavljeni korisnik nije pronadjen u bazi", Toast.LENGTH_SHORT).show();
        }

    }

    private void updatePassword(DatabaseReference userNodeRef, String newPassword) {
        userNodeRef.child("password").setValue(newPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), "Sifra je uspesno promenjena!", Toast.LENGTH_SHORT).show();
                newPasswordInput.setText("");
                oldPasswordInput.setText("");
                newPasswordConfirmedInput.setText("");
            } else {
                Toast.makeText(getActivity(), "Greska pri promeni sifre!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}