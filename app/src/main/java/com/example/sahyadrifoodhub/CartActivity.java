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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartActivity extends AppCompatActivity implements PaymentResultListener {

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private List<ModelFoodList> mListFood;
    AdapterFoodList adapterFoodList;
    DatabaseReference mDatabaseReference;
    List<String> listCartDetails;
    ImageView back;
    TextView checkout;
    TextView Amount;
    String TotalAmount;


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
                double price = Double.parseDouble(TotalAmount);

                Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_test_4MOhFGXnzl2zFR"); // Replace with your Razorpay Key ID

                try {
                    JSONObject options = new JSONObject();
                    options.put("name", "Your App Name");
                    options.put("description", "Payment for your product");
                    options.put("currency", "INR");
                    options.put("amount", (int) (price * 100)); // amount in paise (e.g., Rs. 10)
                    options.put("prefill.email", "customer@example.com");
                    options.put("prefill.contact", "9876543210");

                    checkout.open(CartActivity.this, options);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        Checkout.preload(getApplicationContext());

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
            TotalAmount = intent.getStringExtra("TotalAmount");
            Amount=findViewById(R.id.tvTotalAmount);
            Amount.setText(TotalAmount);
        }
    };

    private void intializepayment(){
//        Intent intent = new Intent(CartActivity.this,Reciept.class);
//        startActivity(intent);

    }
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        // Payment successful, handle success logic here
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();

        // Open another activity and pass the price value
        Intent intent = new Intent(CartActivity.this, Reciept.class);
        intent.putExtra("price", TotalAmount);
        startActivity(intent);
    }

    @Override
    public void onPaymentError(int code, String description) {
        // Payment failed, handle failure logic here
        Toast.makeText(this, "Payment Failed: " + description, Toast.LENGTH_SHORT).show();
    }


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

