package com.example.sahyadrifoodhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sahyadrifoodhub.adapter.AdapterFoodList;
import com.example.sahyadrifoodhub.database.Constants;
import com.example.sahyadrifoodhub.model.ModelFoodList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private List<ModelFoodList> mListFood;
    AdapterFoodList adapterFoodList;
    DatabaseReference mDatabaseReference;
    List<String> listCartDetails;
    ImageView back;
    TextView checkout;
    TextView Amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-message".
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

        checkout=findViewById(R.id.tvCheckout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CartActivity.this,Payment.class);
                startActivity(intent);
                finish();
            }
        });

        back=findViewById(R.id.img_aerrow_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });

        mListFood = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_FOOD_LIST);


        recyclerView = findViewById(R.id.rvCart);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true); //to get a back arrow
       /*recyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);*/

        //DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        //recyclerView.addItemDecoration(dividerItemDecoration);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        LayoutManager.setReverseLayout(true);
//        LayoutManager.setStackFromEnd(true);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();


//
//        AdapterClass adapterClass=new AdapterClass(modelClasses);
//        recyclerView.setAdapter(adapterClass);
        recyclerView.setVisibility(View.VISIBLE);

       checkCartDetails();

    }

    private void fetchFoodList() {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mListFood.clear();    //slideLists is an ArrayList
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ModelFoodList upload = snapshot.getValue(ModelFoodList.class);
                        for (String id : listCartDetails){
                            if (Objects.requireNonNull(upload).getFoodId().equals(id)){
                                mListFood.add(upload);
                            }
                        }




                    }
                    //  Toasty.success(getContext(), "All data fetched", Toasty.LENGTH_SHORT).show();

                    adapterFoodList = new AdapterFoodList(mListFood);
                    recyclerView.setAdapter(adapterFoodList);


                } else {
                    Toast.makeText(getApplicationContext(), "No Food List", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String TotalAmount = intent.getStringExtra("TotalAmount");
            Amount=findViewById(R.id.tvTotalAmount);
            Amount.setText(TotalAmount);
        }
    };

    private void checkCartDetails() {
        listCartDetails = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("CartDetails")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCartDetails.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    listCartDetails.add(snapshot.getKey());
                }
                if(listCartDetails.isEmpty())
                {
                    //show animation
                    findViewById(R.id.lyt404).setVisibility(View.VISIBLE);
                    findViewById(R.id.cvCheckout).setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
                else
                {
                    findViewById(R.id.lyt404).setVisibility(View.GONE);
                    findViewById(R.id.cvCheckout).setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    fetchFoodList();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

