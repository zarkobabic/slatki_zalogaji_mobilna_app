package com.example.slatkizalogajimobilnaaplikacija;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slatkizalogajimobilnaaplikacija.adapters.CommentAdapter;
import com.example.slatkizalogajimobilnaaplikacija.models.Cake;
import com.example.slatkizalogajimobilnaaplikacija.models.Comment;
import com.example.slatkizalogajimobilnaaplikacija.models.Cookie;

import java.util.ArrayList;
import java.util.List;

public class DetailedActivity extends AppCompatActivity {

    ImageView imageDetailed;
    TextView titleDetailed, descriptionDetailed, compositionDetailed, priceDetailed;
    EditText inputCommentDetailed, quantityDetailed;
    Button buttonAddComment,buttonAddToCart;
    Cake cakeToShow = null;
    Cookie cookieToShow = null;

    RecyclerView commentRecycler;
    CommentAdapter commentAdapter;
    List<Comment> commentModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detailed);

        //
        imageDetailed = findViewById(R.id.imageDetailed);
        titleDetailed = findViewById(R.id.titleDetailed);
        descriptionDetailed = findViewById(R.id.descriptionDetailed);
        compositionDetailed = findViewById(R.id.compositionDetailed);
        priceDetailed = findViewById(R.id.priceDetailed);
        inputCommentDetailed = findViewById(R.id.inputCommentDetailed);
        quantityDetailed = findViewById(R.id.quantityDetailed);
        buttonAddComment = findViewById(R.id.buttonAddComment);
        buttonAddToCart = findViewById(R.id.buttonAddToCart);

        commentRecycler = findViewById(R.id.recyclerViewComments);
        commentRecycler.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));
        commentModelList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this,commentModelList);
        commentRecycler.setAdapter(commentAdapter);
        //

        final Object productToShowOnPage = getIntent().getSerializableExtra("detail");
        if(productToShowOnPage instanceof Cake){
            //kad je torta
            cakeToShow = (Cake) productToShowOnPage;
            if(cakeToShow != null){

                String imageNameWithoutExtension = cakeToShow.getImage().split("\\.")[0];
                int imageResourceID = getResources().getIdentifier(imageNameWithoutExtension, "mipmap", getPackageName());
                imageDetailed.setImageResource(imageResourceID);

                titleDetailed.setText(cakeToShow.getTitle());
                descriptionDetailed.setText(cakeToShow.getDescription());

                StringBuilder compositionBuilder = new StringBuilder();
                for(String composition: cakeToShow.getComposition()){
                    compositionBuilder.append("• ").append(composition).append("\n");
                }
                compositionDetailed.setText(compositionBuilder.toString());

                priceDetailed.setText(cakeToShow.getPrice());
                for(Comment comment:cakeToShow.getComments()){
                    commentModelList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }
        }else{
            //kad je kolac
            cookieToShow = (Cookie) productToShowOnPage;
            if(cookieToShow != null){

                String imageNameWithoutExtension = cookieToShow.getImage().split("\\.")[0];
                int imageResourceID = getResources().getIdentifier(imageNameWithoutExtension, "mipmap", getPackageName());
                imageDetailed.setImageResource(imageResourceID);

                titleDetailed.setText(cookieToShow.getTitle());
                descriptionDetailed.setText(cookieToShow.getDescription());

                StringBuilder compositionBuilder = new StringBuilder();
                for(String composition: cookieToShow.getComposition()){
                    compositionBuilder.append("• ").append(composition).append("\n");
                }
                compositionDetailed.setText(compositionBuilder.toString());

                priceDetailed.setText(cookieToShow.getPrice());
                for(Comment comment:cookieToShow.getComments()){
                    commentModelList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}