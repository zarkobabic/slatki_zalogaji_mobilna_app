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
import com.example.slatkizalogajimobilnaaplikacija.models.Cake;
import java.util.List;

public class CakeAdapter extends RecyclerView.Adapter<CakeAdapter.CakeViewHolder> {

    private Context context;
    private List<Cake> cakeList;

    public CakeAdapter(Context context, List<Cake> cakeList) {
        this.context = context;
        this.cakeList = cakeList;
    }

    @NonNull
    @Override
    public CakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cake, parent, false);
        return new CakeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CakeViewHolder holder, int position) {
        Cake cake = cakeList.get(position);
        holder.titleTextView.setText(cake.getTitle());
//        holder.descriptionTextView.setText(cake.getDescription());
//        holder.priceTextView.setText(cake.getPrice());
        // Ensure cake.getImage() returns the correct resource name
        String imageNameWithoutExtension = cake.getImage().split("\\.")[0];
        int imageResourceID = context.getResources().getIdentifier(imageNameWithoutExtension, "mipmap", context.getPackageName());
        holder.imageViewCake.setImageResource(imageResourceID);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailedActivity.class);
                //proveriti ovo ispod za getAdapterPosition
                intent.putExtra("detail", cakeList.get(holder.getAdapterPosition()));
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
        return cakeList.size();
    }

    public class CakeViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewCake;
        TextView titleTextView;
        //descriptionTextView, priceTextView, compositionTextView, commentsTextView;

        public CakeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCake = itemView.findViewById(R.id.imageViewCake);
            titleTextView = itemView.findViewById(R.id.titleTextView);
//            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
//            priceTextView = itemView.findViewById(R.id.priceTextView);
//            compositionTextView = itemView.findViewById(R.id.compositionTextView);
//            commentsTextView = itemView.findViewById(R.id.commentsTextView);
        }
    }
}