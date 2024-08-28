package com.example.slatkizalogajimobilnaaplikacija;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slatkizalogajimobilnaaplikacija.adapters.CommentAdapter;
import com.example.slatkizalogajimobilnaaplikacija.databinding.ActivityMainBinding;
import com.example.slatkizalogajimobilnaaplikacija.models.Cake;
import com.example.slatkizalogajimobilnaaplikacija.models.CartItem;
import com.example.slatkizalogajimobilnaaplikacija.models.Comment;
import com.example.slatkizalogajimobilnaaplikacija.models.Cookie;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailedActivity extends AppCompatActivity {

    ImageView imageDetailed;
    TextView titleDetailed, descriptionDetailed, compositionDetailed, priceDetailed, emptyComments;
    EditText inputCommentDetailed, quantityDetailed;
    Button buttonAddComment,buttonAddToCart;
    Cake cakeToShow = null;
    Cookie cookieToShow = null;
    Integer idProduct = -1;
    Boolean isCake = false;

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
        emptyComments = findViewById(R.id.emptyComments);

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
                isCake = true;
                idProduct = cakeToShow.getIdProduct();
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
                isCake = false;
                idProduct = cookieToShow.getIdProduct();
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

        if(commentModelList.size() == 0) {
            emptyComments.setVisibility(View.VISIBLE);
        }


        //Button listeners


        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = inputCommentDetailed.getText().toString().trim();
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                String localStorageFirstName = sharedPreferences.getString("firstName","Default Value");
                String localStorageLastName = sharedPreferences.getString("lastName","Default Value");
                String loggedUserFullName = localStorageFirstName + " " + localStorageLastName;

                if (!commentText.isEmpty()) {
                    addCommentToProduct(isCake, idProduct, commentText, loggedUserFullName);
                } else {
                    Toast.makeText(getParent(), "Molimo unesite komentar", Toast.LENGTH_SHORT).show();
                }
            }
        });


        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String quantityString = quantityDetailed.getText().toString().trim();

                if (!quantityString.isEmpty()) {
                    Integer quantity = Integer.parseInt(quantityString);

                    Integer idInCart = getNextIdInCart();
                    CartItem cartItem;
                    if (isCake) {
                        cartItem = new CartItem(idInCart, idProduct, cakeToShow.getPrice(), quantity, cakeToShow.getTitle());
                    } else {
                        cartItem = new CartItem(idInCart, idProduct, cookieToShow.getPrice(), quantity, cookieToShow.getTitle());
                    }

                    putItemToCart(cartItem);
                    quantityDetailed.setText("");
                    Toast.makeText(DetailedActivity.this, "Uspesno dodato u korpu!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailedActivity.this, "Kolicina ne sme biti prazna, unesite broj!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        //

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void putItemToCart(CartItem cartItem) {

        //dohvatanje korpe iz SharedPreferences

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = sharedPreferences.getString("cart", null);
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
        ArrayList<CartItem> cart =  gson.fromJson(json, type);

        cart.add(cartItem);

        Gson gson1 = new Gson();
        String json1 = gson1.toJson(cart);
        editor.putString("cart", json1);
        editor.apply();
    }

    private Integer getNextIdInCart() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        Integer nextIdInCart = sharedPreferences.getInt("nextIdInCart", -1);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("nextIdInCart", nextIdInCart + 1);
        editor.apply();
        return nextIdInCart;
    }

//    private void addCommentToProduct(Boolean isCake, Integer idProduct, String commentDescription, String userName) {
//
//        DatabaseReference databaseRef;
//        if(isCake){
//            //cake
//            databaseRef = FirebaseDatabase.getInstance().getReference("cakes");;
//        }else{
//            //cookie
//            databaseRef = FirebaseDatabase.getInstance().getReference("cookies");
//        }
//
//        Query query = databaseRef.orderByChild("idProduct").equalTo(idProduct);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
//                        DatabaseReference commentRef = productSnapshot.getRef().child("comments");
//                        String commentId = commentRef.push().getKey();
//                        Comment newComment = new Comment(commentDescription, userName);
//
//                        if (commentId != null) {
//                            commentRef.child(commentId).setValue(newComment);
//                            inputCommentDetailed.setText("");
//                            commentAdapter.notifyDataSetChanged();
//                        }
//                    }
//                } else {
//                    Toast.makeText(getParent(), "Product not found", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getParent(), "Failed to add comment: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//    }


    private void addCommentToProduct(Boolean isCake, Integer idProduct, String commentDescription, String userName) {
        DatabaseReference databaseRef;
        if (isCake) {
            databaseRef = FirebaseDatabase.getInstance().getReference("cakes");
        } else {
            databaseRef = FirebaseDatabase.getInstance().getReference("cookies");
        }

        Query query = databaseRef.orderByChild("idProduct").equalTo(idProduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        DatabaseReference commentRef = productSnapshot.getRef().child("comments");
                        String commentId = commentRef.push().getKey();
                        Comment newComment = new Comment(commentDescription, userName);

                        if (commentId != null) {
                            commentRef.child(commentId).setValue(newComment).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    commentModelList.add(newComment); // Add the new comment to the local list
                                    commentAdapter.notifyDataSetChanged(); // Notify the adapter about the change
                                    inputCommentDetailed.setText(""); // Clear the input field
                                    Toast.makeText(DetailedActivity.this, "Komentar je dodat", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DetailedActivity.this, "Neuspesno unosenje komentara", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                } else {
                    Toast.makeText(DetailedActivity.this, "Proizvod nije nadjen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailedActivity.this, "Neuspesno dodavanje komentara: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}