package com.example.slatkizalogajimobilnaaplikacija.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slatkizalogajimobilnaaplikacija.R;
import com.example.slatkizalogajimobilnaaplikacija.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<Notification> notificationList;

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        if(notification.getSuccess()){
            holder.notificationSuccessTextView.setText("je prihvacena");
        }
        else{
            holder.notificationSuccessTextView.setText("je odbijena");
        }

        StringBuilder sb = new StringBuilder();
        List<String> notificaitonItems = notification.getItems();
        List<Integer> notificationQuantities = notification.getQuantities();

        for(int i = 0; i < notificaitonItems.size(); i++){
            sb.append(notificationQuantities.get(i)).append(" x ").append(notificaitonItems.get(i)).append("\n");
        }

        holder.notificationAllItemsTextView.setText(sb.toString());

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView notificationSuccessTextView;
        TextView notificationAllItemsTextView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationSuccessTextView = itemView.findViewById(R.id.notificationSuccessTextView);
            notificationAllItemsTextView = itemView.findViewById(R.id.notificationAllItemsTextView);
        }
    }
}
