package com.example.slatkizalogajimobilnaaplikacija.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slatkizalogajimobilnaaplikacija.DetailedActivity;
import com.example.slatkizalogajimobilnaaplikacija.R;
import com.example.slatkizalogajimobilnaaplikacija.models.Cookie;
import com.example.slatkizalogajimobilnaaplikacija.ui.contact.ContactFragment;
import com.example.slatkizalogajimobilnaaplikacija.ui.personal_data.PersonalDataFragment;

import java.util.List;

public class CookieAdapter extends RecyclerView.Adapter<CookieAdapter.CookieViewHolder> {

    private Context context;
    private List<Cookie> cookieList;

    public CookieAdapter(Context context, List<Cookie> cookieList) {
        this.context = context;
        this.cookieList = cookieList;
    }

    @NonNull
    @Override
    public CookieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cookie, parent, false);
        return new CookieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CookieViewHolder holder, int position) {
        Cookie cookie = cookieList.get(position);
        holder.itemTitleCookie.setText(cookie.getTitle());
//        holder.descriptionTextView.setText(cake.getDescription());
//        holder.priceTextView.setText(cake.getPrice());
        // Ensure cake.getImage() returns the correct resource name
        String imageNameWithoutExtension = cookie.getImage().split("\\.")[0];
        int imageResourceID = context.getResources().getIdentifier(imageNameWithoutExtension, "mipmap", context.getPackageName());
        holder.itemImageCookie.setImageResource(imageResourceID);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailedActivity.class);
                //proveriti ovo ispod za getAdapterPosition
                intent.putExtra("detail", cookieList.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });



//        // Example to show composition
//        StringBuilder compositionText = new StringBuilder();
//        for (String ingredient : cake.getComposition()) {
//            compositionText.append(ingredient).append(", ");
//        }
//        holder.compositionTextView.setText(compositionText.toString());

//        // Example to show comments
//        StringBuilder commentsText = new StringBuilder();
//        for (Comment comment : cake.getComments()) {
//            commentsText.append(comment.getUserName()).append(": ").append(comment.getCommentDescription()).append("\n");
//        }
//        holder.commentsTextView.setText(commentsText.toString());
    }

    @Override
    public int getItemCount() {
        return cookieList.size();
    }

    public class CookieViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImageCookie;
        TextView itemTitleCookie;
        //descriptionTextView, priceTextView, compositionTextView, commentsTextView;

        public CookieViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageCookie = itemView.findViewById(R.id.itemImageCookie);
            itemTitleCookie = itemView.findViewById(R.id.itemTitleCookie);
//            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
//            priceTextView = itemView.findViewById(R.id.priceTextView);
//            compositionTextView = itemView.findViewById(R.id.compositionTextView);
//            commentsTextView = itemView.findViewById(R.id.commentsTextView);
        }
    }
}