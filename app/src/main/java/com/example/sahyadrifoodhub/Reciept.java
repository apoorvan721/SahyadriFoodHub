package com.example.sahyadrifoodhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.sahyadrifoodhub.adapter.FoodAdapter;
import com.example.sahyadrifoodhub.model.ModelFoodCount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Reciept extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private List<ModelFoodCount> foodList;
    private FoodAdapter adapter;

    private TextView Amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciept);

        String price = getIntent().getStringExtra("price");
        Amount=findViewById(R.id.amount);

        Amount.setText("Total Amount: "+price);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.reciept_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize food list
        foodList = new ArrayList<>();

        // Create adapter and set it to the RecyclerView
        adapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(adapter);

        // Fetch data from Firebase
        fetchDataFromFirebase();
    }

    private void fetchDataFromFirebase() {
        // Replace "userId" with the actual user ID
        String userId = FirebaseAuth.getInstance().getUid();

        // Get the reference to the "cartlist" child under the user ID
        DatabaseReference cartListRef = databaseReference.child("CartDetails").child(userId);

        cartListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve foodName and quantity from each child snapshot
                    String foodName = snapshot.child("FoodName").getValue(String.class);
                    String quantity = snapshot.child("FoodCount").getValue(String.class);

                    // Create a new ModelFoodList object and add it to the foodList
                    ModelFoodCount food = new ModelFoodCount(foodName, quantity);
                    foodList.add(food);
                }

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}