package com.example.sahyadrifoodhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sahyadrifoodhub.adapter.AdapterFoodList;
import com.example.sahyadrifoodhub.database.Constants;
import com.example.sahyadrifoodhub.model.ModelFoodList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Breakfast_menu extends AppCompatActivity {

    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private List<ModelFoodList> mListFood;
    AdapterFoodList adapterFoodList;
    DatabaseReference mDatabaseReference;



    ModelClass[] modelClasses={
            new ModelClass("Poori Baji","Rs.35","Add To Cart",R.drawable.poori),
            new ModelClass("Chapathi","Rs.35","Add To Cart",R.drawable.chapathi),
            new ModelClass("Parota","Rs.35","Add To Cart",R.drawable.parota),
            new ModelClass("Idle Vada","Rs.35","Add to Cart",R.drawable.idlivada),
            new ModelClass("BisiBele Bath","Rs.35","Add to Cart",R.drawable.bisibele),
            new ModelClass("Sada Dosa","Rs.35","Add to Cart",R.drawable.sada),
            new ModelClass("Samosa","Rs.35","Add to Cart",R.drawable.samosa),
            new ModelClass("Masala Dosa","Rs.35","Add to Cart",R.drawable.breakfast)

    };

    /*ModelClass[] mealslist={
            new ModelClass("Poori Baji","Rs.35","Add To Cart",R.drawable.poori),
            new ModelClass("Chapathi","Rs.35","Add To Cart",R.drawable.chapathi),
            new ModelClass("Parota","Rs.35","Add To Cart",R.drawable.parota),
            new ModelClass("Idle Vada","Rs.35","Add to Cart",R.drawable.idlivada),
            new ModelClass("BisiBele Bath","Rs.35","Add to Cart",R.drawable.bisibele),
            new ModelClass("Sada Dosa","Rs.35","Add to Cart",R.drawable.sada),
            new ModelClass("Samosa","Rs.35","Add to Cart",R.drawable.samosa),
            new ModelClass("Masala Dosa","Rs.35","Add to Cart",R.id.masaladosa)

    };*/

    /*ModelClass[] juicelist={
            new ModelClass("Poori Baji","Rs.35","Add To Cart",R.drawable.poori),
            new ModelClass("Chapathi","Rs.35","Add To Cart",R.drawable.chapathi),
            new ModelClass("Parota","Rs.35","Add To Cart",R.drawable.parota),
            new ModelClass("Idle Vada","Rs.35","Add to Cart",R.drawable.idlivada),
            new ModelClass("BisiBele Bath","Rs.35","Add to Cart",R.drawable.bisibele),
            new ModelClass("Sada Dosa","Rs.35","Add to Cart",R.drawable.sada),
            new ModelClass("Samosa","Rs.35","Add to Cart",R.drawable.samosa),
            new ModelClass("Masala Dosa","Rs.35","Add to Cart",R.id.masaladosa)

    };*/

   /* ModelClass[] chatslist={
            new ModelClass("Poori Baji","Rs.35","Add To Cart",R.drawable.poori),
            new ModelClass("Chapathi","Rs.35","Add To Cart",R.drawable.chapathi),
            new ModelClass("Parota","Rs.35","Add To Cart",R.drawable.parota),
            new ModelClass("Idle Vada","Rs.35","Add to Cart",R.drawable.idlivada),
            new ModelClass("BisiBele Bath","Rs.35","Add to Cart",R.drawable.bisibele),
            new ModelClass("Sada Dosa","Rs.35","Add to Cart",R.drawable.sada),
            new ModelClass("Samosa","Rs.35","Add to Cart",R.drawable.samosa),
            new ModelClass("Masala Dosa","Rs.35","Add to Cart",R.id.masaladosa)

    };*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast_menu);

        mListFood = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_FOOD_LIST);
        ImageView cart;


        recyclerView=findViewById(R.id.breakfast_menu_recycler);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true); //to get a back arrow
       /*recyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);*/

        //DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        //recyclerView.addItemDecoration(dividerItemDecoration);
        cart=findViewById(R.id.cartimg);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Breakfast_menu.this,CartActivity.class);
                startActivity(i);
                finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        LayoutManager.setReverseLayout(true);
//        LayoutManager.setStackFromEnd(true);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
       recyclerView.hasFixedSize();





//
//        AdapterClass adapterClass=new AdapterClass(modelClasses);
//        recyclerView.setAdapter(adapterClass);

        fetchFoodList();

    }

    private void fetchFoodList() {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mListFood.clear();    //slideLists is an ArrayList
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ModelFoodList upload = snapshot.getValue(ModelFoodList.class);
                        Bundle extras = getIntent().getExtras();
                        if (extras != null) {
                            String value = extras.getString("Category");
                            if(upload.getFoodCategory().equals(value)){
                                mListFood.add(upload);
                            }
                            //The key argument here must match that used in the other activity
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

    @Override
    public boolean onSupportNavigateUp() {
        Intent i= new Intent(Breakfast_menu.this,Category_home.class);
        startActivity(i);
        finish();
        return true;
    }
}