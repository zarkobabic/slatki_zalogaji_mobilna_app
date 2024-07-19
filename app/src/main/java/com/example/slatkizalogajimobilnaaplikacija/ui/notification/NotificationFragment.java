package com.example.slatkizalogajimobilnaaplikacija.ui.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.slatkizalogajimobilnaaplikacija.R;
import com.example.slatkizalogajimobilnaaplikacija.adapters.CakeAdapter;
import com.example.slatkizalogajimobilnaaplikacija.adapters.NotificationAdapter;
import com.example.slatkizalogajimobilnaaplikacija.databinding.FragmentCakesBinding;
import com.example.slatkizalogajimobilnaaplikacija.databinding.FragmentNotificationBinding;
import com.example.slatkizalogajimobilnaaplikacija.models.Cake;
import com.example.slatkizalogajimobilnaaplikacija.models.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private FragmentNotificationBinding binding;
    List<Notification> notificationModelList;
    NotificationAdapter notificationAdapter;
    RecyclerView notificationRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //
        notificationRecycler = binding.recyclerViewNotifications;
        notificationRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        notificationModelList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(getActivity(), notificationModelList);
        notificationRecycler.setAdapter(notificationAdapter);

        populateNotificationListFromDatabase();
        //



        return root;
    }

    private void populateNotificationListFromDatabase() {


        DatabaseReference databaseNotificationReference = FirebaseDatabase.getInstance().getReference("notifications");

        // Dohvatanje id-ja ulogovanog korisnika koji smo sacuvali u localstorage (ovde se zove SharedPreferences) prilikom logina u loginActivity
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Integer idLoggedUser = sharedPreferences.getInt("idLoggedUser", -1);

        // Dohvatanje notifikacija samo za trenutno ulogovanog korisnika sa id-jem id
        databaseNotificationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationModelList.clear();
                for (DataSnapshot allNotifications : dataSnapshot.getChildren()) {
                    Integer idUser = allNotifications.child("idUser").getValue(Integer.class);
                    if (idUser != null && idUser.equals(idLoggedUser)) {
                        Boolean notificationIsSuccess = allNotifications.child("isSuccess").getValue(Boolean.class);
                        Integer idNotification = allNotifications.child("idNotification").getValue(Integer.class);

                        // Dohvatanje itema i quantitija
                        List<String> items = new ArrayList<>();
                        for (DataSnapshot item : allNotifications.child("items").getChildren()) {
                            items.add(item.getValue(String.class));
                        }

                        List<Integer> quantities = new ArrayList<>();
                        for (DataSnapshot quantity : allNotifications.child("quantities").getChildren()) {
                            quantities.add(quantity.getValue(Integer.class));
                        }

                        // Kreiranje nofication objekta sa dovucenim informacijama
                        Notification notification = new Notification(idNotification, idUser, notificationIsSuccess, items, quantities);
                        notificationModelList.add(notification);
                    }
                }
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}