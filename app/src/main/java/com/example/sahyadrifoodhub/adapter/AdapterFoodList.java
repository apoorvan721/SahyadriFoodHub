package com.example.sahyadrifoodhub.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sahyadrifoodhub.ModelClass;
import com.example.sahyadrifoodhub.Payment;
import com.example.sahyadrifoodhub.R;
import com.example.sahyadrifoodhub.model.ModelFoodList;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdapterFoodList extends RecyclerView.Adapter<AdapterFoodList.AdapterFoodListViewHolder> {

    ModelClass[] modelClasses;
    int count = 0;
    int TotalAmount=0;

    private final List<ModelFoodList> mListFood;


    public AdapterFoodList(List<ModelFoodList> mListFood) {
        this.mListFood = mListFood;
    }

    @NonNull
    @Override
    public AdapterFoodList.AdapterFoodListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_search, parent, false);
        return new AdapterFoodListViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterFoodList.AdapterFoodListViewHolder holder, int position) {

        ModelFoodList model = mListFood.get(position);


        String url = model.getFoodImg();
        Glide
                .with(holder.FoodName.getContext())
                .load(url)
                .centerCrop()
                // .error(R.drawable.placeholder_hd)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //  .placeholder(R.drawable.loading_spinner)
                .into(holder.FoodImg);


        holder.FoodName.setText(model.getFoodName());
        holder.FoodPrice.setText(model.getFoodPrice());

        Query checkCount1 = FirebaseDatabase.getInstance().getReference("CartDetails").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).orderByChild("FoodId").equalTo(model.getFoodId());
        checkCount1.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count=0;
                if (snapshot.exists()) {
                    count=Integer.parseInt(snapshot.child(model.getFoodId()).child("FoodCount").getValue(String.class));
                    if (count>0){
                        TotalAmount=TotalAmount+(count *Integer.parseInt(model.getFoodPrice()));
                      //  Toast.makeText(holder.tvCount.getContext(),Integer.toString(TotalAmount),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent("custom-message");
                         //intent.putExtra("TotalAmount",Integer.parseInt(String.valueOf(TotalAmount)));
                        intent.putExtra("TotalAmount",String.valueOf(TotalAmount));
                        LocalBroadcastManager.getInstance(holder.tvCount.getContext()).sendBroadcast(intent);
                        holder.btn_Add_to_cart.setVisibility(View.GONE);
                        holder.lytCount.setVisibility(View.VISIBLE);
                        holder.tvCount.setText(Integer.toString(count));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        holder.btn_Add_to_cart.setOnClickListener(view -> {
            count=1;
            holder.tvCount.setText(Integer.toString(count));
            holder.btn_Add_to_cart.setVisibility(View.GONE);
            holder.lytCount.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            String uid = FirebaseAuth.getInstance().getUid();
            map.put("Uid", uid);
            map.put("FoodId", model.getFoodId());
            map.put("FoodName", model.getFoodName());
            map.put("FoodCount","1");

            FirebaseDatabase.getInstance().getReference().child("CartDetails").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child(model.getFoodId()).setValue(map);
        });


        holder.btnPlus.setOnClickListener(view -> {
            Query checkCount = FirebaseDatabase.getInstance().getReference("CartDetails").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).orderByChild("FoodId").equalTo(model.getFoodId());
            checkCount.addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    count=0;
                    if (snapshot.exists()) {
                        count=Integer.parseInt(snapshot.child(model.getFoodId()).child("FoodCount").getValue(String.class));
                        if (count>0){
                            holder.btn_Add_to_cart.setVisibility(View.GONE);
                            holder.lytCount.setVisibility(View.VISIBLE);
                            count = count + 1;
                            holder.tvCount.setText(Integer.toString(count));
                            Map<String, Object> map = new HashMap<>();
                            map.put("FoodId", model.getFoodId());
                            map.put("FoodName", model.getFoodName());
                            map.put("FoodCount", Integer.toString(count));

                            FirebaseDatabase.getInstance().getReference().child("CartDetails").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child(model.getFoodId()).updateChildren(map);
                            holder.tvCount.setText(Integer.toString(count));

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });


        holder.btnMinus.setOnClickListener(view -> {
            Query checkCount = FirebaseDatabase.getInstance().getReference("CartDetails").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).orderByChild("FoodId").equalTo(model.getFoodId());
            checkCount.addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        count=Integer.parseInt(snapshot.child(model.getFoodId()).child("FoodCount").getValue(String.class));
                        if (count>0){
                            holder.btn_Add_to_cart.setVisibility(View.GONE);
                            holder.lytCount.setVisibility(View.VISIBLE);
                            if (count<=1) {
                                holder.btn_Add_to_cart.setVisibility(View.VISIBLE);
                                holder.lytCount.setVisibility(View.GONE);
                                FirebaseDatabase.getInstance().getReference().child("CartDetails").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child(model.getFoodId()).removeValue();
                            } else {
                                count = count - 1;
                                holder.tvCount.setText(Integer.toString(count));
                                Map<String, Object> map = new HashMap<>();
                                map.put("FoodId", model.getFoodId());
                                map.put("FoodName", model.getFoodName());
                                map.put("FoodCount", Integer.toString(count));

                                FirebaseDatabase.getInstance().getReference().child("CartDetails").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child(model.getFoodId()).updateChildren(map);
                            }
                            holder.tvCount.setText(Integer.toString(count));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        });


count=0;

    }

    @Override
    public int getItemCount() {
        return mListFood.size();
    }

    public class AdapterFoodListViewHolder extends RecyclerView.ViewHolder {
        TextView FoodName, FoodPrice;
        ImageView FoodImg;
        TextView btn_Add_to_cart;
        LinearLayout lytCount;
        ImageView btnMinus;
        TextView tvCount;
        ImageView btnPlus;

        public AdapterFoodListViewHolder(@NonNull View itemView) {
            super(itemView);
            FoodName = itemView.findViewById(R.id.tvName);
            FoodPrice = itemView.findViewById(R.id.tvPrice);
            FoodImg = itemView.findViewById(R.id.imgProduct);
            btn_Add_to_cart = itemView.findViewById(R.id.tvAdd);
            lytCount = itemView.findViewById(R.id.lytCount);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            tvCount = itemView.findViewById(R.id.tvCount);
            btnPlus = itemView.findViewById(R.id.btnPlus);
        }
    }
}
